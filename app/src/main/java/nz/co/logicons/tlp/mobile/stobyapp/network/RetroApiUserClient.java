package nz.co.logicons.tlp.mobile.stobyapp.network;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.io.IOException;

import nz.co.logicons.tlp.mobile.stobyapp.AppExecutors;
import nz.co.logicons.tlp.mobile.stobyapp.cache.UserDao;
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.UserEntity;
import nz.co.logicons.tlp.mobile.stobyapp.cache.model.UserEntityMapper;
import nz.co.logicons.tlp.mobile.stobyapp.data.Result;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.UserDto;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.UserDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;
import retrofit2.Call;
import retrofit2.Response;

/*
 * @author by Allen
 */
public class RetroApiUserClient extends  AbstractRetroApiClient{
//    private RetroApiService retroApiService;
    private UserDtoMapper userDtoMapper;
    private SharedPreferences sharedPreferences;
    private UserDao userDao;
    private UserEntityMapper userEntityMapper;
//    private MutableLiveData<DataState<?>>  userData = new MutableLiveData<>();
    private MutableLiveData<Result<User>>  userData = new MutableLiveData<>();

//    public LiveData<DataState<?>> getUserData(){
//        return userData;
//    }
    public LiveData<Result<User>> getUserData(){
        return userData;
    }
    public RetroApiUserClient(){}
    public RetroApiUserClient(SharedPreferences sharedPreferences, UserDtoMapper userDtoMapper,
            UserDao userDao, UserEntityMapper userEntityMapper) {
        this.sharedPreferences = sharedPreferences;
        this.userDtoMapper = userDtoMapper;
//        Log.d("RetroApiUserClient ", "RetroApiUserClient: sharedPreferences "
//                +sharedPreferences.getString(PreferenceKeys.BASE_URL, null).toString());
        this.userDao = userDao;
        this.userEntityMapper = userEntityMapper;
    }

    private User getUserFromCache(User user){
        Log.d(Constants.TAG, "getUserFromCache: "+user);
        UserEntity userEntity = userDao.getUserEntity(user.getUsername());
        if (userEntity == null) return null;
        return userEntityMapper.mapToDomainModel(userDao.getUserEntity(user.getUsername()));
    }
    public void saveFcmToken(User user){
        if (user.getUsername().isEmpty()){
            Log.d(Constants.TAG, "saveFcmToken: empty username ");
            return;
        }
        initRetroApi(sharedPreferences);
        AppExecutors.getInstance().networkIO().execute(() -> {
            try {
                retroApiService.saveFcmToken(user.getFcmToken(),
                        user.getUsername(),
                        user.getPassword()).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    private LoginUserRunnable loginUserRunnable;
    public void checkUser(boolean isNetworkAvailable, User user){
        Log.d(Constants.TAG, "execute: "+
                user + " sharedPreferences "+
                sharedPreferences.getString(PreferenceKeys.BASE_URL, null).toString());
        if (loginUserRunnable != null){
            loginUserRunnable = null;
        }
        loginUserRunnable = new LoginUserRunnable(isNetworkAvailable, user);
        AppExecutors.getInstance().networkIO().execute(loginUserRunnable);

//        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
//            @Override
//            public void run() {
//                myHandler.cancel(true);
//            }
//        }, 5000, TimeUnit.MILLISECONDS);
    }

    private class LoginUserRunnable implements Runnable {
        private boolean isNetworkAvailable;
        private User user;

        public LoginUserRunnable(boolean isNetworkAvailable, User user){
            this.isNetworkAvailable = isNetworkAvailable;
            this.user = user;
        }
        @Override
        public void run() {
            try {
                // TODO offline storage
                userData.postValue(new Result.Loading(true));

//                Thread.sleep(5000);
//                User uzer = getUserFromCache(user);
//                if (uzer != null){
//                    userData.postValue(new Result.Success<>(userDtoMapper.mapFromDomainModel(user)));
//                    return;
//                }else{
                    if (isNetworkAvailable){
                        Response response = getToken(user).execute();

                        if (response.isSuccessful()){
                            Log.d(Constants.TAG, "RetroApiUserClient : "+response.body());
                            userData.postValue(new Result.Success(
                                    userDtoMapper.mapToDomainModel((UserDto) response.body())));

                            User userResp = userDtoMapper.mapToDomainModel((UserDto) response.body());
                            // user response no password
                            userResp.setPassword(user.getPassword());
                            Log.d(Constants.TAG, "RetroApiUserClient : "+userResp);
                            userDao.insert(userEntityMapper.mapFromDomainModel(userResp));
                        }else if (response.errorBody() != null){
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            String errMsg = (String) jObjError.get("message");
                            Log.v(Constants.TAG, "Error "+errMsg);
                            Exception exception = TextUtils.isEmpty(errMsg) ? null
                                    : new Exception(errMsg);
                            userData.postValue(new Result.Error(exception));
                        }
                    }
//                }
//
//                uzer = getUserFromCache(user);
//                if (uzer != null){
////                    userData.postValue(new DataState<>(userDtoMapper.mapFromDomainModel(user)));
//                    userData.postValue(new Result.Success<>(userDtoMapper.mapFromDomainModel(user)));
//                }

            } catch (Exception e) {
                e.printStackTrace();
//                userData.postValue(new DataState("error"));
                userData.postValue(new Result.Error(e));
            }
        }

        private Call<UserDto> getToken(User user){
            return retroApiService.getUser(user);

        }

    }
}
