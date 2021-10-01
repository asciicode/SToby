package nz.co.logicons.tlp.mobile.stobyapp.network;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.AppExecutors;
import nz.co.logicons.tlp.mobile.stobyapp.cache.ManifestItemDao;
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.ManifestItemEntity;
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.ManifestItemEntityMapper;
import nz.co.logicons.tlp.mobile.stobyapp.data.Result;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.Manifest;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.ManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestItemDto;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestItemDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import retrofit2.Call;
import retrofit2.Response;

/*
 * @author by Allen
 */
public class RetroApiManifestItemClient extends AbstractRetroApiClient {
    private SharedPreferences sharedPreferences;
    private ManifestItemDtoMapper manifestItemDtoMapper;
    private ManifestItemDao manifestItemDao;
    private ManifestItemEntityMapper manifestItemEntityMapper;

    private MutableLiveData<Result<List<ManifestItem>>> manifestItems = new MutableLiveData<>();

    public LiveData<Result<List<ManifestItem>>> getManifestItems() {
        return manifestItems;
    }

    private MutableLiveData<Result<ManifestItem>> manifestItem = new MutableLiveData<>();

    public LiveData<Result<ManifestItem>> getManifestItem() {
        return manifestItem;
    }

    public RetroApiManifestItemClient(SharedPreferences sharedPreferences,
            ManifestItemDtoMapper manifestItemDtoMapper, ManifestItemDao manifestItemDao,
            ManifestItemEntityMapper manifestItemEntityMapper) {
        this.sharedPreferences = sharedPreferences;
        this.manifestItemDtoMapper = manifestItemDtoMapper;
        this.manifestItemDao = manifestItemDao;
        this.manifestItemEntityMapper = manifestItemEntityMapper;
    }

    private Call<List<ManifestItemDto>> getAllocatedManifestItems(Manifest manifest,
            String username, String password) {
        return retroApiService.getAllocatedManifestItems(manifest, username, password);
    }

    private CheckManifestItemRunnable checkManifestItemRunnable;

    public void checkManifestItem(User user, ManifestItem manifestItem) {
        initRetroApi(sharedPreferences);
        if (checkManifestItemRunnable != null) {
            checkManifestItemRunnable = null;
        }
        checkManifestItemRunnable = new CheckManifestItemRunnable(user, manifestItem);
        AppExecutors.getInstance().networkIO().execute(checkManifestItemRunnable);
    }

    private class CheckManifestItemRunnable implements Runnable {
        private User user;
        private ManifestItem inputItem;

        public CheckManifestItemRunnable(User user, ManifestItem inputItem) {
            this.user = user;
            this.inputItem = inputItem;
        }

        @Override
        public void run() {
            try {
                manifestItem.postValue(new Result.Loading(true));
                List<ManifestItemEntity> manifestItemEntities = manifestItemDao
                        .getByManifestId(inputItem.getManifestId());
                if (manifestItemEntities.isEmpty()) {
                    manifestItem.postValue(new Result.Error(new Exception("Invalid barcode")));
                } else {
                    ManifestItemEntity barcodeEntity = manifestItemEntities.stream()
                            .filter(rec -> TextUtils.equals(inputItem.getBarCode(),
                                    rec.getBarCode())).findFirst().orElse(null);
                    if (barcodeEntity == null) {
                        manifestItem.postValue(new Result.Error(new Exception("Invalid barcode")));
                        return;
                    } else {
                        if (!barcodeEntity.isLoaded()) {
                            barcodeEntity.setLoaded(true);
                            manifestItemDao.update(barcodeEntity);

                            manifestItem.postValue(
                                    new Result.Success<>(
                                            manifestItemEntityMapper.mapToDomainModel(barcodeEntity)));
                            return;
                        }
                    }
                    ManifestItemEntity tempEntity = manifestItemEntities.stream()
                            .filter(rec -> rec.isLoaded() == false).findFirst().orElse(null);
                    if (tempEntity == null) {
                        // all loaded
                        manifestItem.postValue(new Result.Success<>(
                                "All items loaded. Click Load Complete."));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                manifestItem.postValue(new Result.Error(e));
            }
        }
    }

    private SaveManifestItemRunnable saveManifestItemRunnable;

    public void saveToCacheAllocatedManifestItems(User user, Manifest manifest) {
        initRetroApi(sharedPreferences);
        if (saveManifestItemRunnable != null) {
            saveManifestItemRunnable = null;
        }
        saveManifestItemRunnable = new SaveManifestItemRunnable(user, manifest);
        AppExecutors.getInstance().networkIO().execute(saveManifestItemRunnable);
    }

    private class SaveManifestItemRunnable implements Runnable {
        private User user;
        private Manifest manifest;

        public SaveManifestItemRunnable(User user, Manifest manifest) {
            this.user = user;
            this.manifest = manifest;
        }

        @Override
        public void run() {
            try {
                Response response = getAllocatedManifestItems(manifest, user.getUsername(), user.getPassword()).execute();
                if (response.isSuccessful()) {
                    Log.d(Constants.TAG, "Response from server : " + response.body());
                    List<ManifestItemDto> items = (List<ManifestItemDto>) response.body();
                    List<ManifestItem> domainItems = manifestItemDtoMapper.toDomainList(items);
                    for (ManifestItem item : domainItems) {
                        ManifestItemEntity manifestItemEntity = manifestItemEntityMapper.mapFromDomainModel(item);
                        manifestItemDao.insert(manifestItemEntity);
                    }
                } else if (response.errorBody() != null) {
                    Log.v(Constants.TAG, "Error " + response.errorBody());
                    manifestItems.postValue(new Result.Error(new Exception(response.errorBody().string())));
                }
            } catch (Exception e) {
                e.printStackTrace();
                manifestItems.postValue(new Result.Error(e));
            }
        }
    }

    private ManifestItemRunnable manifestItemRunnable;

    public void getAllocatedManifestItems(boolean isNetworkAvailable, User user, Manifest manifest) {
        initRetroApi(sharedPreferences);
        if (manifestItemRunnable != null) {
            manifestItemRunnable = null;
        }
        manifestItemRunnable = new ManifestItemRunnable(isNetworkAvailable, user, manifest);
        AppExecutors.getInstance().networkIO().execute(manifestItemRunnable);
    }

    private class ManifestItemRunnable implements Runnable {
        private boolean isNetworkAvailable;
        private User user;
        private Manifest manifest;

        public ManifestItemRunnable(boolean isNetworkAvailable, User user, Manifest manifest) {
            this.isNetworkAvailable = isNetworkAvailable;
            this.user = user;
            this.manifest = manifest;
        }

        @Override
        public void run() {
            try {
                // TODO offline storage
                if (isNetworkAvailable) {
                    manifestItems.postValue(new Result.Loading(true));

                    List<ManifestItemEntity> manifestItemEntities = manifestItemDao
                            .getByManifestId(manifest.getId());
                    if (manifestItemEntities.isEmpty()) {
                        Response response = getAllocatedManifestItems(manifest, user.getUsername(), user.getPassword()).execute();
                        if (response.isSuccessful()) {
                            Log.d(Constants.TAG, "RetroApiManifestItemClient : " + response.body());
                            manifestItems.postValue(new Result.Success(
                                    manifestItemDtoMapper.toDomainList((List<ManifestItemDto>) response.body())));


                        } else if (response.errorBody() != null) {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String errMsg = (String) jObjError.get("message");
                            Log.v(Constants.TAG, "Error " + response.errorBody());
                            Exception exception = TextUtils.isEmpty(errMsg) ? null
                                    : new Exception(errMsg);
                            manifestItems.postValue(new Result.Error(exception));
                        }
                    } else {
                        Log.d(Constants.TAG, "run: getting items in cache ");
                        manifestItems.postValue(new Result.Success(
                                manifestItemEntityMapper.toDomainList(manifestItemEntities)));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                manifestItems.postValue(new Result.Error(e));
            }
        }
    }

}
