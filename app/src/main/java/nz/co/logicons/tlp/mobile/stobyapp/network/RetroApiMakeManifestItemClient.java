package nz.co.logicons.tlp.mobile.stobyapp.network;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import nz.co.logicons.tlp.mobile.stobyapp.AppExecutors;
import nz.co.logicons.tlp.mobile.stobyapp.cache.ManifestItemDao;
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.ManifestItemEntityMapper;
import nz.co.logicons.tlp.mobile.stobyapp.data.Result;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.MakeManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.MakeManifestItemDto;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.MakeManifestItemDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestItemDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import retrofit2.Call;
import retrofit2.Response;

/*
 * @author by Allen
 */
public class RetroApiMakeManifestItemClient extends AbstractRetroApiClient{
    private SharedPreferences sharedPreferences;
    private ManifestItemDtoMapper manifestItemDtoMapper;
    private ManifestItemDao manifestItemDao;
    private ManifestItemEntityMapper manifestItemEntityMapper;
    private MakeManifestItemDtoMapper makeManifestItemDtoMapper;

    private MutableLiveData<Result<MakeManifestItem>> resultMakeManifestItem = new MutableLiveData<>();
    public LiveData<Result<MakeManifestItem>> getMakeManifestItem(){
        return resultMakeManifestItem;
    }

    public RetroApiMakeManifestItemClient(SharedPreferences sharedPreferences,
            ManifestItemDtoMapper manifestItemDtoMapper, ManifestItemDao manifestItemDao,
            ManifestItemEntityMapper manifestItemEntityMapper,
            MakeManifestItemDtoMapper makeManifestItemDtoMapper) {
        this.sharedPreferences =sharedPreferences;
        this.manifestItemDtoMapper = manifestItemDtoMapper;
        this.manifestItemDao = manifestItemDao;
        this.manifestItemEntityMapper = manifestItemEntityMapper;
        this.makeManifestItemDtoMapper = makeManifestItemDtoMapper;
    }

    private Call<MakeManifestItemDto> checkMakeManifestItem(MakeManifestItem makeManifestItem,
            String username, String password){
        return retroApiService.checkMakeManifestItem(makeManifestItem, username, password);
    }
    private Call<MakeManifestItemDto> removeMakeManifestItem(MakeManifestItem makeManifestItem,
            String username, String password){
        return retroApiService.removeMakeManifestItem(makeManifestItem, username, password);
    }
    private RemoveMakeManifestItemRunnable removeMakeManifestItemRunnable;
    public void removeMakeManifestItem(MakeManifestItem makeManifestItem, User user){
        initRetroApi(sharedPreferences);
        if (checkMakeManifestItemRunnable != null){
            checkMakeManifestItemRunnable = null;
        }
        removeMakeManifestItemRunnable = new RemoveMakeManifestItemRunnable(makeManifestItem, user);
        AppExecutors.getInstance().networkIO().execute(removeMakeManifestItemRunnable);
    }
    private class RemoveMakeManifestItemRunnable implements Runnable {
        private User user;
        private MakeManifestItem makeManifestItem;
        public RemoveMakeManifestItemRunnable(MakeManifestItem makeManifestItem, User user) {
            this.user = user;
            this.makeManifestItem = makeManifestItem;
        }
        @Override
        public void run() {
            try {
                Response response = removeMakeManifestItem(makeManifestItem,
                        user.getUsername(), user.getPassword()).execute();
                if (response.isSuccessful()){
                    Log.d(Constants.TAG, "RetroApiMakeManifestItemClient : "+response.body());
                    MakeManifestItemDto makeManifestItemDto = (MakeManifestItemDto) response.body();
                    resultMakeManifestItem.postValue(new Result.Success<>(
                            makeManifestItemDtoMapper.mapToDomainModel(makeManifestItemDto)));
                }else if (response.errorBody() != null){
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    String errMsg = (String) jObjError.get("message");
                    Log.v(Constants.TAG, "Error "+response.errorBody());
                    Exception exception = TextUtils.isEmpty(errMsg) ? null
                            : new Exception(errMsg);
                    resultMakeManifestItem.postValue(new Result.Error(exception));
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultMakeManifestItem.postValue(new Result.Error(e));
            }
        }
    }

    private CheckMakeManifestItemRunnable checkMakeManifestItemRunnable;
    public void checkMakeManifestItem(MakeManifestItem makeManifestItem, User user){
        initRetroApi(sharedPreferences);
        if (checkMakeManifestItemRunnable != null){
            checkMakeManifestItemRunnable = null;
        }
        checkMakeManifestItemRunnable = new CheckMakeManifestItemRunnable(makeManifestItem, user);
        AppExecutors.getInstance().networkIO().execute(checkMakeManifestItemRunnable);
    }
    private class CheckMakeManifestItemRunnable implements Runnable {
        private User user;
        private MakeManifestItem makeManifestItem;
        public CheckMakeManifestItemRunnable(MakeManifestItem makeManifestItem, User user) {
            this.user = user;
            this.makeManifestItem = makeManifestItem;
        }
        @Override
        public void run() {
            try {
                Response response = checkMakeManifestItem(makeManifestItem,
                        user.getUsername(), user.getPassword()).execute();
                if (response.isSuccessful()){
                    Log.d(Constants.TAG, "RetroApiMakeManifestItemClient : "+response.body());
                    MakeManifestItemDto makeManifestItemDto = (MakeManifestItemDto) response.body();
                    resultMakeManifestItem.postValue(new Result.Success<>(
                            makeManifestItemDtoMapper.mapToDomainModel(makeManifestItemDto)));
                }else if (response.errorBody() != null){
                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                    String errMsg = (String) jObjError.get("message");
                    Log.v(Constants.TAG, "Error "+response.errorBody());
                    Exception exception = TextUtils.isEmpty(errMsg) ? null
                            : new Exception(errMsg);
                    resultMakeManifestItem.postValue(new Result.Error(exception));
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultMakeManifestItem.postValue(new Result.Error(e));
            }
        }
    }
}
