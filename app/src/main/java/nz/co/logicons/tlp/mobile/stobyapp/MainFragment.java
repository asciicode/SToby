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
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

@AndroidEntryPoint
public class MainFragment extends Fragment {
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

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        Log.d(Constants.TAG, "onPrepareOptionsMenu: Main Frag");
//        menu.clear();
//    }

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
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton menuButton = getActivity().findViewById(R.id.btnMenu);
        if (menuButton != null)
            menuButton.setVisibility(View.VISIBLE);

//        DrawerLayout drawerLayout = view.findViewById(R.id.fragment_main);
//        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.nav_open_text, R.string.nav_close_text);
//        Log.d(Constants.TAG, "onViewCreated: "+drawerLayout+" "+drawerToggle);
//
//        drawerToggle.setDrawerIndicatorEnabled(true);
//        drawerLayout.addDrawerListener(drawerToggle);
//        drawerToggle.syncState();
//        if (drawerLayout != null) {
//            menuButton.setOnClickListener(
//                    tmpview -> {
//                        if (drawerLayout.isDrawerOpen(GravityCompat.END))
//                            drawerLayout.closeDrawer(GravityCompat.END);
//                        else
//                            drawerLayout.openDrawer(GravityCompat.END);
//                    }
//            );
//        }
//
//        NavigationView navigationView = view.findViewById(R.id.navigationview_main);
//        navigationView.setNavigationItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.navigation_profile)
//                Toast.makeText(getContext(), "Profile", Toast.LENGTH_SHORT).show();
//            else if (item.getItemId() == R.id.navigation_settings)
//                Toast.makeText(getContext(), "Settings", Toast.LENGTH_SHORT).show();
//            else if (item.getItemId() == R.id.navigation_logout){
//
//            }
//            return true;
//        });
//
//        TextView navTextHeader = navigationView.getHeaderView(0).findViewById(R.id.navigation_welcome);
//        navTextHeader.setText(String.format("Welcome %s!", sharedPreferences.getString(PreferenceKeys.USERNAME, "").toString()));

//        NavController navController = Navigation.findNavController(view);
//        Button btn = view.findViewById(R.id.btnLoad);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.action_mainFragment_to_loadMenuFragment);
//
//                Log.d(Constants.TAG, "isNetworkAvailable: " + connectivityManager.isNetworkAvailable);
//            }
//        });
//
//        btn = view.findViewById(R.id.btnMake);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                navController.navigate(R.id.action_mainFragment_to_makeMenuFragment);
//            }
//        });
//        // create once expensive loading class
//        if (loading == null){
//            loading = new Loading(getActivity());
//        }
//        // Create the observer which updates the UI.
//        final Observer<DataState<?>> nameObserver = new Observer<DataState<?>>() {
//            @Override
//            public void onChanged(DataState<?> manifestDataState) {
//                Log.d(Constants.TAG,
//                        "onChanged: manifestDataState " + manifestDataState);
//                Log.d(Constants.TAG,
//                        this + "onChanged: Data " + manifestDataState.getData());
//                if (manifestDataState.getData() instanceof Manifest){
//                    Log.d(Constants.TAG,
//                            this + "onChanged: real type return expected " + manifestDataState.isLoading());
//                    loading.dismiss();
//                }else if (manifestDataState.getData() instanceof String){
//                    Log.d(Constants.TAG, "onChanged: string " + manifestDataState.isLoading());
//                    loading.dismiss();
//                }else{
//                    Log.d(Constants.TAG, "onChanged: boolean " + manifestDataState.isLoading());
//                    loading.start();
//                }
//
//            }
//        };
//        TestViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner()).get(TestViewModel.class);
//        ManifestServiceClient manifestServiceClient = viewModel.getManifestServiceClient();
//        manifestServiceClient.getTestData().observe(getViewLifecycleOwner(), nameObserver);
//
//        btn = view.findViewById(R.id.btnInward);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(Constants.TAG, "onClick: ");
//                manifestServiceClient.execute(1, connectivityManager.isNetworkAvailable);
//            }
//        });
//
//        btn = view.findViewById(R.id.btnAvailable);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(Constants.TAG, "onClick: btnAvailable ");
//                Snackbar snackbar = Snackbar.make(view, "Allen take a snacks", Snackbar.LENGTH_LONG);
//                snackbar.show();
//            }
//        });
        Log.d(Constants.TAG,
                "MainFrag onViewCreated: " + sharedPreferences.getString(PreferenceKeys.BASE_URL, ""));


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(Constants.TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(Constants.TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constants.TAG, "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(Constants.TAG, "onDetach: ");
    }
}