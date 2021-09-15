package nz.co.logicons.tlp.mobile.stobyapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

@AndroidEntryPoint
public class LoginSettingActivity extends AppCompatActivity {

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    SharedPreferences.Editor editor;

    private EditText baseUrlEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_setting);

        baseUrlEditText = findViewById(R.id.txtSettingsBaseUrl);
        baseUrlEditText.setText(sharedPreferences.getString(PreferenceKeys.BASE_URL, "").toString());

        setupSaveButton();
        setupCancelButton();
    }

    private void setupSaveButton() {
        Button saveButton = findViewById(R.id.btnLoginSettingsSave);
        saveButton.setOnClickListener(
                v -> {
                    String baseUrl = baseUrlEditText.getText().toString();
                    Log.i("LoginSettingActivity",">>> baseUrl="+ baseUrl);
                    if (TextUtils.isEmpty(baseUrl))
                        baseUrl = sharedPreferences.getString(PreferenceKeys.BASE_URL, "").toString();
                    editor.putString(PreferenceKeys.BASE_URL, baseUrl);
                    editor.apply();
                    setResult(RESULT_OK);
                    finish();
                }
        );
    }

    private void setupCancelButton() {
        Button cancelButton = findViewById(R.id.btnLoginSettingsCancel);
        cancelButton.setOnClickListener(
                v -> {
                    setResult(RESULT_CANCELED);
                    finish();
                }
        );
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}