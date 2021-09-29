package nz.co.logicons.tlp.mobile.stobyapp;

import static nz.co.logicons.tlp.mobile.stobyapp.util.Constants.LOAD_COMPLETED;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import nz.co.logicons.tlp.mobile.stobyapp.data.Result;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.MakeManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.Manifest;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.ManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel.MakeManifestItemViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel.ManifestItemViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

@AndroidEntryPoint
public class LoadScanFragment extends Fragment {
    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    SharedPreferences sharedPreferences;
    private static final int CAMERA_REQUEST_CODE = 101;
    private CodeScanner mCodeScanner;
    ToneGenerator toneGenerator;
    private ManifestItemViewModel manifestItemViewModel;
    private MakeManifestItemViewModel makeManifestItemViewModel;
    private String manifestId;
    //    Dialog dialog;
    User user;
    private Button button;

    public LoadScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manifestId = getArguments().getString("manifestId");
        Log.d(Constants.TAG, "onCreate: manifestId pass " + getArguments());
        setupPermission();
        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_load_scan, container, false);
        view.findViewById(R.id.btnLoadComplete);
        codeScanner(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String username = sharedPreferences.getString(PreferenceKeys.USERNAME, "").toString();
        String password = sharedPreferences.getString(PreferenceKeys.PASSWORD, "").toString();
        user = new User(username, password);

        setupButton(view);

        manifestItemViewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner())
                .get(ManifestItemViewModel.class);
        manifestItemViewModel.getRetroApiManifestItemClient().getAllocatedManifestItems(
                connectivityManager.isNetworkAvailable, user, new Manifest(manifestId));

        makeManifestItemViewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner())
                .get(MakeManifestItemViewModel.class);

        observeAnyChange();
    }

    private void setupButton(@NonNull View view) {
        Button btn = view.findViewById(R.id.btnLoadComplete);
        btn.setOnClickListener(
                temp -> {
                    MakeManifestItem makeManifestItem = new MakeManifestItem();
                    makeManifestItem.setManifestId(manifestId);
                    makeManifestItemViewModel.getRetroMakeApiManifestItemClient()
                            .loadCompleteMakeManifestItem(makeManifestItem, user);
                }
        );
    }

    private void observeAnyChange() {
        // per scan observer
        manifestItemViewModel.getRetroApiManifestItemClient().getManifestItem().observe(getViewLifecycleOwner(),
                manifestItemResult -> {
                    Log.d(Constants.TAG, "observeAnyChange:ManifestItem " + manifestItemResult);
                    if (manifestItemResult instanceof Result.Error) {
                        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP2, 1000);
                        String str = ((Result.Error) manifestItemResult).getError() == null ?
                                Constants.SERVER_ERROR : ((Result.Error) manifestItemResult).getError().getMessage();
                        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                    } else if (manifestItemResult instanceof Result.Success) {
                        // check if all loaded
                        Result.Success manifestItemRes = (Result.Success) manifestItemResult;
                        if (manifestItemRes.getData() instanceof String) {
                            Log.d(Constants.TAG, "observeAnyChange: all items loaded");
                            Toast.makeText(getActivity(), (String) manifestItemRes.getData(), Toast.LENGTH_SHORT).show();
                            toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000);
                        } else {
                            Toast.makeText(getActivity(), Constants.ITEM_LOADED, Toast.LENGTH_SHORT).show();
                            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 1000);
                        }
                    }
                    // adding delay to scanner decoding
                    Handler handler = new  Handler();
                    handler.postDelayed(() -> mCodeScanner.setScanMode(ScanMode.CONTINUOUS), 1000);
                });
        // load complete observer
        makeManifestItemViewModel.getRetroMakeApiManifestItemClient().getMakeManifestItem().observe(
                getViewLifecycleOwner(), makeManifestItemResult -> {
                    Log.d(Constants.TAG, "observeAnyChange: makeManifestItemResult " + makeManifestItemResult);
                    if (makeManifestItemResult instanceof Result.Error) {
                        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP2, 1000);
                        String str = ((Result.Error) makeManifestItemResult).getError() == null ?
                                Constants.SERVER_ERROR : ((Result.Error) makeManifestItemResult).getError().getMessage();
                        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                    }else if (makeManifestItemResult instanceof Result.Success){
                        MakeManifestItem makeManifestItem = (MakeManifestItem)
                                ((Result.Success)makeManifestItemResult).getData();
                       if (TextUtils.equals(makeManifestItem.getAction(), "LoadCompleted")){
                            NavController navController = Navigation.findNavController(this.getView());
                            Bundle bundle = new Bundle();
                            bundle.putString("action", LOAD_COMPLETED);
                            NavOptions navOptions = new NavOptions.Builder()
                                    .setPopUpTo(R.id.mainFragment, true).build();
                            navController.navigate(R.id.action_loadScanFragment_to_mainFragment, bundle, navOptions);
                        }
                    }
                });
    }

    private void setupPermission() {
        int permission = ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
    }

    private void makeRequest() {
        String[] permissions = {android.Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(getActivity(), permissions, CAMERA_REQUEST_CODE);
    }

    private void codeScanner(View view) {
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
        mCodeScanner.setFormats(CodeScanner.ALL_FORMATS);
        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        mCodeScanner.setFlashEnabled(false);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final com.google.zxing.Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(Constants.TAG, "run: result.getText() " + result.getText());

                        ManifestItem manifestItem = new ManifestItem();
                        manifestItem.setManifestId(manifestId);
                        manifestItem.setBarCode(result.getText());
                        mCodeScanner.setScanMode(ScanMode.PREVIEW);
                        manifestItemViewModel.getRetroApiManifestItemClient()
                                .checkManifestItem(user, manifestItem);
                    }
                });
            }
        });
        mCodeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull Exception error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Camera init error: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

//        scannerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mCodeScanner.startPreview();
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}