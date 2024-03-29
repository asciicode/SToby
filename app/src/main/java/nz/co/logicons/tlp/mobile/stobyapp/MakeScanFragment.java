package nz.co.logicons.tlp.mobile.stobyapp;

import static nz.co.logicons.tlp.mobile.stobyapp.util.Constants.LOAD_COMPLETED;
import static nz.co.logicons.tlp.mobile.stobyapp.util.Constants.NO_INET_CONNECTION;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
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
import androidx.transition.TransitionInflater;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import nz.co.logicons.tlp.mobile.stobyapp.data.Result;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.ActionManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.ui.Loading;
import nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel.MakeManifestItemViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

@AndroidEntryPoint
public class MakeScanFragment extends Fragment {
    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    SharedPreferences sharedPreferences;
    private static final int CAMERA_REQUEST_CODE = 101;
    private CodeScanner mCodeScanner;
    ToneGenerator toneGenerator;
    private MakeManifestItemViewModel makeManifestItemViewModel;
    private String manifestId;
    Dialog dialog;
    User user;
    private TextView tvTitle;
    private TextView tvScan;
    private ActionManifestItem currentActionManifestItem;
    private Loading loading;

    public MakeScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new Loading(getActivity());
        manifestId = getArguments().getString("manifestId");
        Log.d(Constants.TAG, "onCreate: manifestId pass " + getArguments());
        setupPermission();
        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        dialog = new BottomSheetDialog(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_remove_layout, null);
        dialog.setContentView(view);
        dialog.findViewById(R.id.btnYes).setOnClickListener(view1 -> {
            dialog.hide();
            if (connectivityManager.isNetworkAvailable) {
                makeManifestItemViewModel.getRetroMakeApiManifestItemClient()
                        .removeMakeManifestItem(currentActionManifestItem, user);
            } else {
                Toast.makeText(getActivity(), NO_INET_CONNECTION, Toast.LENGTH_SHORT).show();
            }
            mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        });

        dialog.findViewById(R.id.btnNo).setOnClickListener(view1 -> {
            dialog.hide();
            mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        });
        dialog.setOnCancelListener(dialogInterface -> {
            mCodeScanner.setScanMode(ScanMode.CONTINUOUS);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_make_scan, container, false);
        codeScanner(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String username = sharedPreferences.getString(PreferenceKeys.USERNAME, "").toString();
        String password = sharedPreferences.getString(PreferenceKeys.PASSWORD, "").toString();
        user = new User(username, password);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(String.format("Make Manifest %s ", manifestId));
        tvScan = view.findViewById(R.id.tvScan);
        YoYo.with(Techniques.SlideInLeft).duration(1000)
                .repeat(Animation.INFINITE).playOn(tvScan);
        setupButton(view);

        makeManifestItemViewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner())
                .get(MakeManifestItemViewModel.class);

        observeAnyChange();
    }
    private void setupButton(@NonNull View view) {
        Button btn = view.findViewById(R.id.btnLoadComplete);
        btn.setOnClickListener(
            temp -> {
                ActionManifestItem actionManifestItem = new ActionManifestItem();
                actionManifestItem.setManifestId(manifestId);
                actionManifestItem.setStoreLoad(true);
                makeManifestItemViewModel.getRetroMakeApiManifestItemClient()
                        .loadCompleteActionManifestItem(actionManifestItem, user);
            }
        );
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
                // adding delay to scanner decoding
                Handler handler = new Handler();
                handler.postDelayed(() -> mCodeScanner.setScanMode(ScanMode.CONTINUOUS), 1000);
                loading.dismiss();
            }else if (makeManifestItemResult instanceof Result.Success){
                ActionManifestItem actionManifestItem = (ActionManifestItem)
                        ((Result.Success)makeManifestItemResult).getData();
                if (TextUtils.equals(actionManifestItem.getAction(), "ItemAdded")){
                    Toast.makeText(getActivity(), Constants.ITEM_LOADED, Toast.LENGTH_SHORT).show();
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 1000);
                }else if (TextUtils.equals(actionManifestItem.getAction(), "ItemRemove")){
                    // delay for loading purpose
                    SystemClock.sleep(1000);
                    currentActionManifestItem = actionManifestItem;
                    dialog.show();
                    toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000);
                }else if (TextUtils.equals(actionManifestItem.getAction(), "ItemRemoved")){
                    dialog.hide();
                    Toast.makeText(getActivity(), Constants.ITEM_REMOVED, Toast.LENGTH_SHORT).show();
                }else if (TextUtils.equals(actionManifestItem.getAction(), "LoadCompleted")){
                    NavController navController = Navigation.findNavController(this.getView());
                    Bundle bundle = new Bundle();
                    bundle.putString("action", LOAD_COMPLETED);
//                          find workaround animation issue if using popUpTo
                    TransitionInflater inflater = TransitionInflater.from(requireContext());
                    setExitTransition(inflater.inflateTransition(R.transition.slide_right));

                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.mainFragment, true)
//                                    .setPopEnterAnim(R.anim.slide_in_right)
//                                    .setPopExitAnim(R.anim.slide_out_left)
//                                    .setEnterAnim(R.anim.slide_in_left)
//                                    .setExitAnim(R.anim.slide_out_right)
                            .build();
                    navController.navigate(R.id.action_makeScanFragment_to_mainFragment, bundle, navOptions);
                }
                // adding delay to scanner decoding
                Handler handler = new Handler();
                handler.postDelayed(() -> mCodeScanner.setScanMode(ScanMode.CONTINUOUS), 1000);
                loading.dismiss();
            } else if (makeManifestItemResult instanceof Result.Loading) {
                loading.start();
            }
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
                        actionManifestItem.setManifestId(manifestId);
                        mCodeScanner.setScanMode(ScanMode.PREVIEW);
                        makeManifestItemViewModel.getRetroMakeApiManifestItemClient()
                                .checkMakeManifestItem(actionManifestItem, user);
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