package nz.co.logicons.tlp.mobile.stobyapp.util;

import static android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.HashSet;
import java.util.Set;

import nz.co.logicons.tlp.mobile.stobyapp.AppExecutors;

/*
 * @author by Allen
 * from @youtuber codingwithmitch kotlin converted to java
 */
public class ConnectionLiveData extends LiveData<Boolean> {
    private Context context;
    private ConnectivityManager.NetworkCallback networkCallback;
    private ConnectivityManager cm;
    private Set<Network> validNetworks = new HashSet();

    public ConnectionLiveData(Context context) {
        this.context = context;
        this.cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private void checkValidNetworks() {
        postValue(validNetworks.size() > 0);
    }

    @Override
    protected void onActive() {
        networkCallback = createNetworkCallback();
        NetworkRequest nr = new NetworkRequest.Builder().addCapability(NET_CAPABILITY_INTERNET).build();
        cm.registerNetworkCallback(nr, networkCallback);
    }

    @Override
    protected void onInactive() {
        cm.unregisterNetworkCallback(networkCallback);
    }

    private ConnectivityManager.NetworkCallback createNetworkCallback() {
        return new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
//                Log.d(Constants.TAG, "onAvailable: " + network);
                NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(network);
                boolean hasInternetCapability =
                        networkCapabilities.hasCapability(NET_CAPABILITY_INTERNET);
//                Log.d(Constants.TAG, "onAvailable: " + network + " " + hasInternetCapability);
                if (hasInternetCapability){
                    // check if this network actually has internet
                    AppExecutors.getInstance().networkIO().submit(new Runnable() {
                        @Override
                        public void run() {
                            boolean hasInternet =
                                    DoesNetworkHaveInternet.execute(network.getSocketFactory());
                            validNetworks.add(network);
                            checkValidNetworks();
                        }
                    });
                }
            }

            @Override
            public void onLost(@NonNull Network network) {
                Log.d(Constants.TAG, "onLost: " + network);
                validNetworks.remove(network);
                checkValidNetworks();
            }

        };
    }
}
