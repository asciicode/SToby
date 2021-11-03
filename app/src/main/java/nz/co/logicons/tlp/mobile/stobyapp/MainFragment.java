package nz.co.logicons.tlp.mobile.stobyapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import nz.co.logicons.tlp.mobile.stobyapp.ui.Loading;
import nz.co.logicons.tlp.mobile.stobyapp.ui.adapter.TabsPagerAdapter;
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.IgnoreBackPressedListener;
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.OnBackPressedListener;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

@AndroidEntryPoint
public class MainFragment extends Fragment implements OnBackPressedListener {
    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    SharedPreferences sharedPreferences;
    private Loading loading;
    private ActionBarDrawerToggle drawerToggle;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String action = bundle.getString("action");
            if (!TextUtils.isEmpty(action)) {
                Toast.makeText(getActivity(), action, Toast.LENGTH_SHORT).show();
            }
        }
        Log.d(Constants.TAG, "onCreate: Main Frag");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(this.getChildFragmentManager());
        pagerAdapter.addFragment(new AllocatedDashboardFragment(), "Allocated");
        pagerAdapter.addFragment(new AvailableDashboardFragment(), "Available");
        ViewPager viewPager = rootView.findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        ((MainActivity) getActivity()).setOnBackPressedListener(new IgnoreBackPressedListener(getActivity()));

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton menuButton = getActivity().findViewById(R.id.btnMenu);
        if (menuButton != null)
            menuButton.setVisibility(View.VISIBLE);

        Log.d(Constants.TAG,
                "MainFrag onViewCreated: " + sharedPreferences.getString(PreferenceKeys.BASE_URL, ""));

    }

    @Override
    public void onPause() {
        super.onPause();
//        Log.d(Constants.TAG, "onPause: ");
        ((MainActivity) getActivity()).setOnBackPressedListener(null);
    }

    @Override
    public void onStop() {
        super.onStop();
//        Log.d(Constants.TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.d(Constants.TAG, "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        Log.d(Constants.TAG, "onDetach: ");
    }

    @Override
    public void doBack() {

    }
}