package nz.co.logicons.tlp.mobile.stobyapp.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import nz.co.logicons.tlp.mobile.stobyapp.AppExecutors;
import nz.co.logicons.tlp.mobile.stobyapp.domain.data.DataState;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.Manifest;
import nz.co.logicons.tlp.mobile.stobyapp.network.model.ManifestDtoMapper;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;

/*
 * @author by Allen
 */
public class ManifestServiceClient {
    private RetroApiService retroApiService;
    private ManifestDtoMapper manifestDtoMapper;

    private MutableLiveData<DataState<?>>  testData = new MutableLiveData<>();
    private TestRunnable testRunnable;

    public ManifestServiceClient(ManifestDtoMapper manifestDtoMapper) {
//        this.retroApiService = retroApiService;
        this.manifestDtoMapper = manifestDtoMapper;
    }

    public void execute(long id, boolean isNetworkAvailable){
        Log.d(Constants.TAG, "execute: "+ retroApiService +" isNetworkAvailable "+isNetworkAvailable);
        if (testRunnable != null){
            testRunnable = null;
        }
        testRunnable = new TestRunnable();
        final Future myHandler = AppExecutors.getInstance().networkIO().submit(testRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                myHandler.cancel(true);
            }
        }, 5000, TimeUnit.MILLISECONDS);
    }

    public LiveData<DataState<?>> getTestData(){
        return testData;
    }

    private class TestRunnable implements  Runnable{
        @Override
        public void run() {
            try {
                testData.postValue(new DataState(true));
                // mimic invoke web service
                Thread.sleep(2500);
                Manifest manifest = new Manifest();
                testData.postValue(new DataState(manifest));
            } catch (InterruptedException e) {
                e.printStackTrace();
                testData.postValue(new DataState("error"));
            }
        }
    }
}
