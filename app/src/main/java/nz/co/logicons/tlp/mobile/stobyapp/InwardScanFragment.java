package nz.co.logicons.tlp.mobile.stobyapp;

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
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import nz.co.logicons.tlp.mobile.stobyapp.data.Result;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.ActionManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel.MakeManifestItemViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

@AndroidEntryPoint
public class InwardScanFragment extends Fragment {
    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    SharedPreferences sharedPreferences;
    private static final int CAMERA_REQUEST_CODE = 101;
    private CodeScanner mCodeScanner;
    ToneGenerator toneGenerator;
    private MakeManifestItemViewModel makeManifestItemViewModel;
    User user;
    private TextView tvScan;

    public InwardScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupPermission();
        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inward_scan, container, false);
        codeScanner(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String username = sharedPreferences.getString(PreferenceKeys.USERNAME, "").toString();
        String password = sharedPreferences.getString(PreferenceKeys.PASSWORD, "").toString();
        user = new User(username, password);
        tvScan = view.findViewById(R.id.tvScan);
        YoYo.with(Techniques.SlideInLeft).duration(1000)
                .repeat(Animation.INFINITE).playOn(tvScan);

        makeManifestItemViewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner())
                .get(MakeManifestItemViewModel.class);

        observeAnyChange();
    }

    private void observeAnyChange() {
        makeManifestItemViewModel.getRetroMakeApiManifestItemClient().getActionManifestItem().observe(
                getViewLifecycleOwner(), makeManifestItemResult -> {
                    Log.d(Constants.TAG, "observeAnyChange: makeManifestItemResult " + makeManifestItemResult);
                    if (makeManifestItemResult instanceof Result.Error) {
                        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP2, 1000);
                        String str = ((Result.Error) makeManifestItemResult).getError() == null ?
                                Constants.SERVER_ERROR : ((Result.Error) makeManifestItemResult).getError().getMessage();
                        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                    }else if (makeManifestItemResult instanceof Result.Success){
                        ActionManifestItem actionManifestItem = (ActionManifestItem)
                                ((Result.Success)makeManifestItemResult).getData();
                        if (TextUtils.equals(actionManifestItem.getAction(), "InwardCompleted")){
                            Toast.makeText(getActivity(), Constants.INWARD_COMPLETED, Toast.LENGTH_SHORT).show();
                            toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 1000);
                        }
                    }
                    // adding delay to scanner decoding
                    Handler handler = new Handler();
                    handler.postDelayed(() -> mCodeScanner.setScanMode(ScanMode.CONTINUOUS), 1000);
                });
    }

    private void setupPermission() {
        int permission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA);
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
        mCodeScanner.setAutoFocusInterval(2000L);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final com.google.zxing.Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(Constants.TAG, "run: result.getText() " + result.getText());
                        String decodedBarcode = result.getText();
//                        String decodedBarcode = "Item000014010001";
                        ActionManifestItem actionManifestItem = new ActionManifestItem();
                        actionManifestItem.setBarcode(decodedBarcode);
                        mCodeScanner.setScanMode(ScanMode.PREVIEW);
                        makeManifestItemViewModel.getRetroMakeApiManifestItemClient()
                                .completeInwardManifest(actionManifestItem, user);
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

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
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