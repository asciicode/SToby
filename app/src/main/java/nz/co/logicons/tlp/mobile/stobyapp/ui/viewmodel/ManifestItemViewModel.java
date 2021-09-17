package nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.network.RetroApiManifestItemClient;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;

/*
 * @author by Allen
 */
@HiltViewModel
public class ManifestItemViewModel extends ViewModel {
    private RetroApiManifestItemClient retroApiManifestItemClient;
    private ConnectivityManager connectivityManager;

    @Inject
    public ManifestItemViewModel(RetroApiManifestItemClient retroApiManifestItemClient, ConnectivityManager connectivityManager) {
        this.retroApiManifestItemClient = retroApiManifestItemClient;
        this.connectivityManager = connectivityManager;
    }

    public RetroApiManifestItemClient getRetroApiManifestItemClient() {
        return retroApiManifestItemClient;
    }

    public ConnectivityManager getConnectivityManager() {
        return connectivityManager;
    }
}
