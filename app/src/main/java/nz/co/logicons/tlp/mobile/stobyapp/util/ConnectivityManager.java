package nz.co.logicons.tlp.mobile.stobyapp.util;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;

/*
 * @author by Allen
 */
@Singleton
public class ConnectivityManager {
    // observe this flag in activity
//    public MutableLiveData<Boolean> isNetworkAvailable = new MutableLiveData<>(false);
    public boolean isNetworkAvailable = false;
    private ConnectionLiveData connectionLiveData;
    Application application;

    @Inject
    public ConnectivityManager(Application application) {
        this.application = application;
        connectionLiveData = new ConnectionLiveData(application);
    }

    public void registerConnectionObserver(LifecycleOwner lifecycleOwner) {
        connectionLiveData.observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(Constants.TAG, "onChanged: "+aBoolean);
//                isNetworkAvailable.postValue(aBoolean);
                isNetworkAvailable = aBoolean.booleanValue();
            }
        });
    }

    public void unregisterConnectionObserver(LifecycleOwner lifecycleOwner) {
        connectionLiveData.removeObservers(lifecycleOwner);
    }
}
