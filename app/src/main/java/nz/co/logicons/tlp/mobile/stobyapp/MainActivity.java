package nz.co.logicons.tlp.mobile.stobyapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.OnBackPressedListener;
import nz.co.logicons.tlp.mobile.stobyapp.util.ColorUtil;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    SharedPreferences.Editor editor;
    protected OnBackPressedListener onBackPressedListener;

    @Override
    protected void onStart() {
        super.onStart();
        connectivityManager.registerConnectionObserver(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectivityManager.unregisterConnectionObserver(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrieveCustomAppBar(this, getSupportActionBar(), false, "");

//        Log.d(Constants.TAG, "onCreate: " + sharedPreferences);

        String baseUrl = sharedPreferences.getString(PreferenceKeys.BASE_URL, "").toString();
        if (TextUtils.isEmpty(baseUrl)) {
            editor.putString(PreferenceKeys.BASE_URL, "http://192.168.1.114:8080");
            editor.apply();
        }
        NavigationView navigationView = this.findViewById(R.id.navigationview_main);
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_profile)
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
            else if (item.getItemId() == R.id.navigation_settings)
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            else if (item.getItemId() == R.id.navigation_logout) {
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentContainerView);
                DrawerLayout drawer = findViewById(R.id.fragment_main);
//                Log.d(Constants.TAG, "onCreate: nhf2 "+ navHostFragment.getNavController());
                if (drawer.isDrawerOpen(GravityCompat.END))
                    drawer.closeDrawer(GravityCompat.END);
                NavController navController = navHostFragment.getNavController();
                navController.popBackStack(R.id.loginFragment, true);
                navController.navigate(R.id.loginFragment);
            }
            return true;
        });
        TextView navTextHeader = navigationView.getHeaderView(0).findViewById(R.id.navigation_welcome);
        navTextHeader.setText(String.format("Welcome %s!", sharedPreferences.getString(PreferenceKeys.USERNAME, "").toString()));
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
    }

    public void retrieveCustomAppBar(Activity activity, ActionBar actionBar, Boolean isShowMenu, String title) {
        if (actionBar != null) {
            ColorDrawable colorDrawable = new ColorDrawable(ColorUtil.getColorIntFromStyle(activity, R.attr.colorPrimaryDark));
            actionBar.setBackgroundDrawable(colorDrawable);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View actionBarView = (layoutInflater != null) ? layoutInflater.inflate(R.layout.app_bar_main, null) : null;
            actionBar.setCustomView(actionBarView);
            TextView appBarText = activity.findViewById(R.id.text_app_bar_title);
            appBarText.setText(title);
            ImageButton menuButton = activity.findViewById(R.id.btnMenu);
            DrawerLayout drawer = activity.findViewById(R.id.fragment_main);
//            Log.d(Constants.TAG, "retrieveCustomAppBar: drawer in main activity " + drawer);
            if (drawer != null) {
                menuButton.setOnClickListener(
                        view -> {
//                            Log.d(Constants.TAG, "retrieveCustomAppBar: menu buton click");
                            if (drawer.isDrawerOpen(GravityCompat.END))
                                drawer.closeDrawer(GravityCompat.END);
                            else
                                drawer.openDrawer(GravityCompat.END);
                        }
                );
            }
        }
    }
}