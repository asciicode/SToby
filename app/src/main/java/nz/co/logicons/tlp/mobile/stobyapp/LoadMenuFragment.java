package nz.co.logicons.tlp.mobile.stobyapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.Manifest;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.ui.adapter.DashboardRecyclerViewAdapter;
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.DashboardButtonListener;
import nz.co.logicons.tlp.mobile.stobyapp.ui.model.DashboardRecyclerModel;
import nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel.ManifestItemViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

@AndroidEntryPoint
public class LoadMenuFragment extends Fragment implements DashboardButtonListener {
    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    SharedPreferences sharedPreferences;
    private List<DashboardRecyclerModel> list;
    private DashboardRecyclerViewAdapter adapter;
    private String manifestId;
    public static final String LIST_ITEMS = "List";
    public static final String SCAN_ITEMS = "Scan";
    private ManifestItemViewModel manifestItemViewModel;

    public LoadMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manifestId = getArguments().getString("manifestId");
        Log.d(Constants.TAG, "onCreate: manifestId pass "+getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_load_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRecycler(view);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(String.format("Load Items of Manifest %s ", manifestId));

        manifestItemViewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner())
                .get(ManifestItemViewModel.class);
        String username = sharedPreferences.getString(PreferenceKeys.USERNAME, "").toString();
        String password = sharedPreferences.getString(PreferenceKeys.PASSWORD, "").toString();
        User user = new User(username, password);
        manifestItemViewModel.getRetroApiManifestItemClient()
                .saveToCacheAllocatedManifestItems(user, new Manifest(manifestId));
    }

    private void initializeRecycler(View view) {
        Log.d(Constants.TAG, "initializeRecycler: load menu frag ");
        list = new ArrayList<>();
        list.add(new DashboardRecyclerModel(R.drawable.ic_transfers_white, LIST_ITEMS, "#ffffff", 1, getActivity()));
        list.add(new DashboardRecyclerModel(R.drawable.ic_outwards_white, SCAN_ITEMS, "#ffffff",2, getActivity()));
//        list.add(new DashboardRecyclerModel(R.drawable.ic_inwards_white, "Inward", "#ffffff",3, getActivity()));
//        list.add(new DashboardRecyclerModel(R.drawable.ic_stocktake_white, "STOCK TAKE", "#ffffff",4, getActivity()));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_allocated_dashboard);
        adapter = new DashboardRecyclerViewAdapter(list, this);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onClick(DashboardRecyclerModel model) {
        Log.d(Constants.TAG, "onClick: allocated "+model);
        NavController navController = Navigation.findNavController(this.getView());
        Bundle bundle = new Bundle();
        bundle.putString("manifestId", manifestId);
        if (TextUtils.equals(model.getImgText(), LIST_ITEMS)){
            navController.navigate(R.id.action_loadMenuFragment_to_loadListFragment, bundle);
        } else  if (TextUtils.equals(model.getImgText(), SCAN_ITEMS)){
            navController.navigate(R.id.action_loadMenuFragment_to_loadScanFragment, bundle);
        }
    }
}