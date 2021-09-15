package nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.network.RetroApiManifestClient;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;

/*
 * @author by Allen
 */
@HiltViewModel
public class ManifestViewModel extends ViewModel {
    private RetroApiManifestClient retroApiManifestClient;
    private ConnectivityManager connectivityManager;

    @Inject
    public ManifestViewModel(RetroApiManifestClient retroApiManifestClient, ConnectivityManager connectivityManager) {
        this.retroApiManifestClient = retroApiManifestClient;
        this.connectivityManager = connectivityManager;
    }

    public RetroApiManifestClient getRetroApiManifestClient() {
        return retroApiManifestClient;
    }

    public ConnectivityManager getConnectivityManager() {
        return connectivityManager;
    }
}
