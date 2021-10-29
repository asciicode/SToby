package nz.co.logicons.tlp.mobile.stobyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import nz.co.logicons.tlp.mobile.stobyapp.data.Result;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.ui.Loading;
import nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel.AccessViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import nz.co.logicons.tlp.mobile.stobyapp.util.KeyboardUtil;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

@AndroidEntryPoint
public class LoginFragment extends Fragment {
    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    SharedPreferences.Editor editor;
    @Inject
    SharedPreferences sharedPreferences;
    private EditText usernameEditText, passwordEditText;
    private AccessViewModel accessViewModel;
    private Loading loading;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new Loading(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameEditText = view.findViewById(R.id.txtLoginUsername);
        passwordEditText = view.findViewById(R.id.txtLoginPassword);

        setupSettingsButton(view);
        setupLoginButton(view);

        accessViewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner()).get(AccessViewModel.class);
        accessViewModel.getRetroApiUserClient().getUserData().observe(getViewLifecycleOwner(), new Observer<Result<User>>() {
            @Override
            public void onChanged(Result<User> result) {
                Log.d(Constants.TAG, "onChanged: " + result);

                if (result instanceof Result.Success) {
                    User user = ((Result.Success<User>) result).getData();
//                    loading.dismiss();
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_loginFragment_to_mainFragment);
                } else if (result instanceof Result.Error) {
                    String str = ((Result.Error) result).getError() == null ?
                            Constants.SERVER_ERROR : ((Result.Error) result).getError().getMessage();
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
//                    loading.dismiss();
                } else if (result instanceof Result.Loading) {
//                    loading.start();
                }
            }
        });


    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                //if (result.getResultCode() == Activity.RESULT_OK) {
                // Intent intent = result.getData();
                // Handle the Intent
                //}
            });

    private void setupSettingsButton(@NonNull View view) {
        ImageButton btn = view.findViewById(R.id.btnLoginSetting);
        btn.setOnClickListener(
                temp -> {
                    Intent intent = new Intent(getActivity(), LoginSettingActivity.class);
                    mStartForResult.launch(intent);
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupLoginButton(@NonNull View view) {
        Button btn = view.findViewById(R.id.btnLoginMain);
        btn.setOnClickListener(
                temp -> {
                    KeyboardUtil.hideKeyboard(view);
                    signIn();
                }
        );
    }

    private void signIn() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        editor.putString(PreferenceKeys.USERNAME, username);
        editor.putString(PreferenceKeys.PASSWORD, password);
        editor.apply();
        String fcmToken = sharedPreferences.getString(PreferenceKeys.FCM_TOKEN, "").toString();
        User user = new User(username, password, fcmToken);
        // online as of now
        if (connectivityManager.isNetworkAvailable) {
//            loading.start();
            accessViewModel.getRetroApiUserClient().reinitRetroApi(sharedPreferences);
            accessViewModel.getRetroApiUserClient().checkUser(connectivityManager.isNetworkAvailable, user);

            // can't make it work
            // only happen when running fresh install stoby e.g. wipe data
            // call get token in two places - this is expected to fail
            FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(Constants.TAG, "LoginFragment onViewCreated " +
                                    "FCM registration token failed", task.getException());
                            return;
                        }
                        Log.d(Constants.TAG, "LoginFragment FCM onComplete: " + task.isSuccessful());
                        // saving token not needed here see FirebaseMessagingService
                        // Get new FCM registration token
//                        String token = task.getResult();
//                        saveFcmToken(token);
                    }
                });
        } else {
            Toast.makeText(getContext(), Constants.NO_INET_CONNECTION, Toast.LENGTH_LONG).show();
        }

    }
}