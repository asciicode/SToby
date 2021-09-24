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

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import nz.co.logicons.tlp.mobile.stobyapp.data.Result;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel.AccessViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import nz.co.logicons.tlp.mobile.stobyapp.util.KeyboardUtil;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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
                Log.d(Constants.TAG, "onChanged: "+result);

                if (result instanceof Result.Success){
                    User user = ((Result.Success<User>) result).getData();
                    Log.d(Constants.TAG,
                            this + "onChanged: real type return expected " + user.toString());
//                    loading.dismiss();
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_loginFragment_to_mainFragment);
                }else if (result instanceof Result.Error){
                    String str = ((Result.Error) result).getError() == null ?
                            Constants.SERVER_ERROR : ((Result.Error) result).getError().getMessage();
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
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

    private void signIn(){
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        editor.putString(PreferenceKeys.USERNAME, username);
        editor.putString(PreferenceKeys.PASSWORD, password);
        editor.apply();

        User user = new User(username, password);
        // online as of now
        if (connectivityManager.isNetworkAvailable){
            accessViewModel.getRetroApiUserClient().reinitRetroApi(sharedPreferences);
            accessViewModel.getRetroApiUserClient().checkUser(connectivityManager.isNetworkAvailable, user);
        }else{
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
        }

    }
}