package nz.co.logicons.tlp.mobile.stobyapp.network;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.AppExecutors;
import nz.co.logicons.tlp.mobile.stobyapp.cache.ManifestDao;
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.ManifestEntityMapper;
import nz.co.logicons.tlp.mobile.stobyapp.data.Result;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.Manifest;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestDto;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import retrofit2.Call;
import retrofit2.Response;

/*
 * @author by Allen
 */
public class RetroApiManifestClient extends AbstractRetroApiClient {
    private SharedPreferences sharedPreferences;
    private ManifestDtoMapper manifestDtoMapper;
    private ManifestDao manifestDao;
    private ManifestEntityMapper manifestEntityMapper;

    private MutableLiveData<Result<List<Manifest>>> manifests = new MutableLiveData<>();

    public LiveData<Result<List<Manifest>>> getManifests() {
        return manifests;
    }

    public RetroApiManifestClient(SharedPreferences sharedPreferences,
            ManifestDtoMapper manifestDtoMapper, ManifestDao manifestDao, ManifestEntityMapper manifestEntityMapper) {
        this.sharedPreferences = sharedPreferences;
        this.manifestDtoMapper = manifestDtoMapper;
        this.manifestDao = manifestDao;
        this.manifestEntityMapper = manifestEntityMapper;
    }

    private AllocatedManifestRunnable allocatedManifestRunnable;

    public void allocatedManifest(boolean isNetworkAvailable, Manifest manifest, User user) {
        initRetroApi(sharedPreferences);
        if (allocatedManifestRunnable != null) {
            allocatedManifestRunnable = null;
        }
        allocatedManifestRunnable = new AllocatedManifestRunnable(isNetworkAvailable, manifest, user);
        AppExecutors.getInstance().networkIO().execute(allocatedManifestRunnable);
    }

    private class AllocatedManifestRunnable implements Runnable {
        private boolean isNetworkAvailable;
        private User user;
        private Manifest manifest;

        public AllocatedManifestRunnable(boolean isNetworkAvailable, Manifest manifest, User user) {
            this.isNetworkAvailable = isNetworkAvailable;
            this.user = user;
            this.manifest = manifest;
        }

        @Override
        public void run() {

            try {
                // TODO offline storage
                if (isNetworkAvailable) {
                    manifests.postValue(new Result.Loading(true));

                    Response response = getAllocatedManifest(manifest,
                            user.getUsername(), user.getPassword()).execute();
//                    Log.d(Constants.TAG, "RetroApiManifestClient response : " + response);

                    if (response.isSuccessful()) {
                        Log.d(Constants.TAG, "RetroApiManifestClient : " + response.body());
                        manifests.postValue(new Result.Success(
                                manifestDtoMapper.toDomainList((List<ManifestDto>) response.body())));
                    } else if (response.errorBody() != null){
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errMsg = (String) jObjError.get("message");
                        Log.v(Constants.TAG, "Error "+response.errorBody());
                        Exception exception = TextUtils.isEmpty(errMsg) ? null
                                : new Exception(errMsg);
                        manifests.postValue(new Result.Error(exception));
                    }
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, "client runnable : " + e.getMessage());
                manifests.postValue(new Result.Error(e));
            }
        }

        private Call<List<ManifestDto>> getAllocatedManifest(Manifest manifest, String username, String password) {
            return retroApiService.getAllocatedManifest(manifest, username, password);
        }

    }
}
