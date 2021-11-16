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
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.ManifestItemEntityMapper;
import nz.co.logicons.tlp.mobile.stobyapp.data.Result;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.ActionManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.Manifest;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.ManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ActionManifestItemDto;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ActionManifestItemDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestItemDto;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestItemDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import retrofit2.Call;
import retrofit2.Response;

/*
 * @author by Allen
 */
public class RetroApiMakeManifestItemClient extends AbstractRetroApiClient {
    private SharedPreferences sharedPreferences;
    private ManifestItemDtoMapper manifestItemDtoMapper;
    private ManifestItemDao manifestItemDao;
    private ManifestItemEntityMapper manifestItemEntityMapper;
    private ActionManifestItemDtoMapper actionManifestItemDtoMapper;

    private MutableLiveData<Result<List<ManifestItem>>> manifestItems = new MutableLiveData<>();

    public LiveData<Result<List<ManifestItem>>> getManifestItems() {
        return manifestItems;
    }

    private MutableLiveData<Result<ActionManifestItem>> resultActionManifestItem = new MutableLiveData<>();

    public LiveData<Result<ActionManifestItem>> getActionManifestItem() {
        return resultActionManifestItem;
    }

    public RetroApiMakeManifestItemClient(SharedPreferences sharedPreferences,
            ManifestItemDtoMapper manifestItemDtoMapper, ManifestItemDao manifestItemDao,
            ManifestItemEntityMapper manifestItemEntityMapper,
            ActionManifestItemDtoMapper actionManifestItemDtoMapper) {
        this.sharedPreferences = sharedPreferences;
        this.manifestItemDtoMapper = manifestItemDtoMapper;
        this.manifestItemDao = manifestItemDao;
        this.manifestItemEntityMapper = manifestItemEntityMapper;
        this.actionManifestItemDtoMapper = actionManifestItemDtoMapper;
    }

    private Call<ActionManifestItemDto> loadCompleteActionManifestItem(ActionManifestItem actionManifestItem,
            String username, String password) {
        return retroApiService.loadCompleteActionManifestItem(actionManifestItem, username, password);
    }

    private Call<ActionManifestItemDto> completeInwardManifest(ActionManifestItem actionManifestItem,
            String username, String password) {
        return retroApiService.completeInwardManifest(actionManifestItem, username, password);
    }

    private Call<ActionManifestItemDto> checkMakeManifestItem(ActionManifestItem actionManifestItem,
            String username, String password) {
        return retroApiService.checkMakeManifestItem(actionManifestItem, username, password);
    }

    private Call<ActionManifestItemDto> removeMakeManifestItem(ActionManifestItem actionManifestItem,
            String username, String password) {
        return retroApiService.removeMakeManifestItem(actionManifestItem, username, password);
    }

    private Call<List<ManifestItemDto>> getAllocatedManifestItems(Manifest manifest,
            String username, String password) {
        return retroApiService.getAllocatedManifestItems(manifest, username, password);
    }

    private CompleteInwardManifestRunnable completeInwardManifestRunnable;

    public void completeInwardManifest(ActionManifestItem actionManifestItem, User user) {
        initRetroApi(sharedPreferences);
        if (checkMakeManifestItemRunnable != null) {
            checkMakeManifestItemRunnable = null;
        }
        completeInwardManifestRunnable = new CompleteInwardManifestRunnable(actionManifestItem, user);
        AppExecutors.getInstance().networkIO().execute(completeInwardManifestRunnable);
    }

    private class CompleteInwardManifestRunnable implements Runnable {
        private User user;
        private ActionManifestItem actionManifestItem;

        public CompleteInwardManifestRunnable(ActionManifestItem actionManifestItem, User user) {
            this.user = user;
            this.actionManifestItem = actionManifestItem;
        }

        @Override
        public void run() {
            try {
                Response response = completeInwardManifest(actionManifestItem,
                        user.getUsername(), user.getPassword()).execute();
                if (response.isSuccessful()) {
                    Log.d(Constants.TAG, "RetroApiMakeManifestItemClient : " + response.body());
                    ActionManifestItemDto actionManifestItemDto = (ActionManifestItemDto) response.body();
                    resultActionManifestItem.postValue(new Result.Success<>(
                            actionManifestItemDtoMapper.mapToDomainModel(actionManifestItemDto)));
                } else if (response.errorBody() != null) {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    String errMsg = (String) jObjError.get("message");
                    Log.v(Constants.TAG, "Error " + response.errorBody());
                    Exception exception = TextUtils.isEmpty(errMsg) ? null
                            : new Exception(errMsg);
                    resultActionManifestItem.postValue(new Result.Error(exception));
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, "client runnable : " + e.getMessage());
                resultActionManifestItem.postValue(new Result.Error(e));
            }
        }
    }

    private RemoveMakeManifestItemRunnable removeMakeManifestItemRunnable;

    public void removeMakeManifestItem(ActionManifestItem actionManifestItem, User user) {
        initRetroApi(sharedPreferences);
        if (checkMakeManifestItemRunnable != null) {
            checkMakeManifestItemRunnable = null;
        }
        removeMakeManifestItemRunnable = new RemoveMakeManifestItemRunnable(actionManifestItem, user);
        AppExecutors.getInstance().networkIO().execute(removeMakeManifestItemRunnable);
    }

    private class RemoveMakeManifestItemRunnable implements Runnable {
        private User user;
        private ActionManifestItem actionManifestItem;

        public RemoveMakeManifestItemRunnable(ActionManifestItem actionManifestItem, User user) {
            this.user = user;
            this.actionManifestItem = actionManifestItem;
        }

        @Override
        public void run() {
            try {
                resultActionManifestItem.postValue(new Result.Loading(true));
                Response response = removeMakeManifestItem(actionManifestItem,
                        user.getUsername(), user.getPassword()).execute();
                if (response.isSuccessful()) {
                    Log.d(Constants.TAG, "RetroApiMakeManifestItemClient : " + response.body());
                    ActionManifestItemDto actionManifestItemDto = (ActionManifestItemDto) response.body();
                    resultActionManifestItem.postValue(new Result.Success<>(
                            actionManifestItemDtoMapper.mapToDomainModel(actionManifestItemDto)));
                } else if (response.errorBody() != null) {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    String errMsg = (String) jObjError.get("message");
                    Log.v(Constants.TAG, "Error " + response.errorBody());
                    Exception exception = TextUtils.isEmpty(errMsg) ? null
                            : new Exception(errMsg);
                    resultActionManifestItem.postValue(new Result.Error(exception));
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, "client runnable : " + e.getMessage());
                resultActionManifestItem.postValue(new Result.Error(e));
            }
        }
    }

    private CheckMakeManifestItemRunnable checkMakeManifestItemRunnable;

    public void checkMakeManifestItem(ActionManifestItem actionManifestItem, User user) {
        initRetroApi(sharedPreferences);
        if (checkMakeManifestItemRunnable != null) {
            checkMakeManifestItemRunnable = null;
        }
        checkMakeManifestItemRunnable = new CheckMakeManifestItemRunnable(actionManifestItem, user);
        AppExecutors.getInstance().networkIO().execute(checkMakeManifestItemRunnable);
    }

    private class CheckMakeManifestItemRunnable implements Runnable {
        private User user;
        private ActionManifestItem actionManifestItem;

        public CheckMakeManifestItemRunnable(ActionManifestItem actionManifestItem, User user) {
            this.user = user;
            this.actionManifestItem = actionManifestItem;
        }

        @Override
        public void run() {
            try {
                resultActionManifestItem.postValue(new Result.Loading(true));
                Response response = checkMakeManifestItem(actionManifestItem,
                        user.getUsername(), user.getPassword()).execute();
                if (response.isSuccessful()) {
                    Log.d(Constants.TAG, "RetroApiMakeManifestItemClient : " + response.body());
                    ActionManifestItemDto actionManifestItemDto = (ActionManifestItemDto) response.body();
                    resultActionManifestItem.postValue(new Result.Success<>(
                            actionManifestItemDtoMapper.mapToDomainModel(actionManifestItemDto)));
                } else if (response.errorBody() != null) {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    String errMsg = (String) jObjError.get("message");
                    Log.v(Constants.TAG, "Error " + response.errorBody());
                    Exception exception = TextUtils.isEmpty(errMsg) ? null
                            : new Exception(errMsg);
                    resultActionManifestItem.postValue(new Result.Error(exception));
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, "client runnable : " + e.getMessage());
                resultActionManifestItem.postValue(new Result.Error(e));
            }
        }
    }

    private LoadCompleteActionManifestItemRunnable loadCompleteActionManifestItemRunnable;

    public void loadCompleteActionManifestItem(ActionManifestItem actionManifestItem, User user) {
        initRetroApi(sharedPreferences);
        if (checkMakeManifestItemRunnable != null) {
            checkMakeManifestItemRunnable = null;
        }
        loadCompleteActionManifestItemRunnable =
                new LoadCompleteActionManifestItemRunnable(actionManifestItem, user);
        AppExecutors.getInstance().networkIO().execute(loadCompleteActionManifestItemRunnable);
    }

    private class LoadCompleteActionManifestItemRunnable implements Runnable {
        private User user;
        private ActionManifestItem actionManifestItem;

        public LoadCompleteActionManifestItemRunnable(ActionManifestItem actionManifestItem, User user) {
            this.user = user;
            this.actionManifestItem = actionManifestItem;
        }

        @Override
        public void run() {
            try {
                Response response = loadCompleteActionManifestItem(actionManifestItem,
                        user.getUsername(), user.getPassword()).execute();
                if (response.isSuccessful()) {
                    Log.d(Constants.TAG, "RetroApiMakeManifestItemClient : " + response.body());
                    ActionManifestItemDto actionManifestItemDto = (ActionManifestItemDto) response.body();
                    resultActionManifestItem.postValue(new Result.Success<>(
                            actionManifestItemDtoMapper.mapToDomainModel(actionManifestItemDto)));
                    manifestItemDao.deleteByManifestId(actionManifestItem.getManifestId());
                } else if (response.errorBody() != null) {
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    String errMsg = (String) jObjError.get("message");
                    Log.v(Constants.TAG, "Error " + response.errorBody());
                    Exception exception = TextUtils.isEmpty(errMsg) ? null
                            : new Exception(errMsg);
                    resultActionManifestItem.postValue(new Result.Error(exception));
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, "client runnable : " + e.getMessage());
                resultActionManifestItem.postValue(new Result.Error(e));
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
                if (isNetworkAvailable) {
                    manifestItems.postValue(new Result.Loading(true));

                    Response response = getAllocatedManifestItems(manifest, user.getUsername(), user.getPassword()).execute();
                    if (response.isSuccessful()) {
                        Log.d(Constants.TAG, "RetroApiMakeManifestItemClient : " + response.body());
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
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, "client runnable : " + e.getMessage());
                manifestItems.postValue(new Result.Error(e));
            }
        }
    }
}
