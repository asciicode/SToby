package nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.network.RetroApiMakeManifestItemClient;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;

/*
 * @author by Allen
 */
@HiltViewModel
public class MakeManifestItemViewModel extends ViewModel {
    private RetroApiMakeManifestItemClient retroApiMakeManifestItemClient;
    private ConnectivityManager connectivityManager;

    @Inject
    public MakeManifestItemViewModel(RetroApiMakeManifestItemClient retroApiMakeManifestItemClient,
            ConnectivityManager connectivityManager) {
        this.retroApiMakeManifestItemClient = retroApiMakeManifestItemClient;
        this.connectivityManager = connectivityManager;
    }

    public RetroApiMakeManifestItemClient getRetroMakeApiManifestItemClient() {
        return retroApiMakeManifestItemClient;
    }

    public ConnectivityManager getConnectivityManager() {
        return connectivityManager;
    }
}
