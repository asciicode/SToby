package nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.network.RetroApiUserClient;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;

/*
 * @author by Allen
 */
@HiltViewModel
public class AccessViewModel extends ViewModel {
    private RetroApiUserClient retroApiUserClient;
    private ConnectivityManager connectivityManager;

    @Inject
    public AccessViewModel(RetroApiUserClient retroApiUserClient, ConnectivityManager connectivityManager) {
        this.retroApiUserClient = retroApiUserClient;
        this.connectivityManager = connectivityManager;
    }

    public RetroApiUserClient getRetroApiUserClient() {
        return retroApiUserClient;
    }

//    public void checkUser(User user){
//        retroApiUserClient.checkUser(connectivityManager.isNetworkAvailable, user);
//    }
}
