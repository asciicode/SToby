package nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.network.ManifestServiceClient;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;

/*
 * @author by Allen
 */
@HiltViewModel
public class TestViewModel extends ViewModel {
    private ManifestServiceClient manifestServiceClient;
    private ConnectivityManager connectivityManager;

    @Inject
    public TestViewModel(ManifestServiceClient manifestServiceClient, ConnectivityManager connectivityManager) {
        this.manifestServiceClient = manifestServiceClient;
        this.connectivityManager = connectivityManager;
    }

    public ManifestServiceClient getManifestServiceClient() {
        return manifestServiceClient;
    }
}
