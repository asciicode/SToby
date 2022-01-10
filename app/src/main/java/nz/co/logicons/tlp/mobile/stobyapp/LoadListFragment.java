package nz.co.logicons.tlp.mobile.stobyapp;

import static nz.co.logicons.tlp.mobile.stobyapp.util.Constants.NO_INET_CONNECTION;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import nz.co.logicons.tlp.mobile.stobyapp.data.Result;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.Manifest;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.ManifestItem;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.ui.adapter.ManifestItemListRecyclerViewAdapter;
import nz.co.logicons.tlp.mobile.stobyapp.ui.model.ManifestItemListRecyclerModel;
import nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel.ManifestItemViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

@AndroidEntryPoint
public class LoadListFragment extends Fragment {
    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    SharedPreferences sharedPreferences;
    private TextView txtViewFragmentInwards;
    private TextView txtViewFragmentInwardsCount;
    private ManifestItemListRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<ManifestItemListRecyclerModel> list;
    private String manifestId;
    private ManifestItemViewModel manifestItemViewModel;

    public LoadListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtViewFragmentInwards = view.findViewById(R.id.txtview_fragment_child_title);
        recyclerView = view.findViewById(R.id.recyclerview_fragment_child);
        txtViewFragmentInwardsCount = view.findViewById(R.id.tvRecordCount);

        initializeRecycler(view);
        initControls();

        manifestItemViewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner())
                .get(ManifestItemViewModel.class);
        if (connectivityManager.isNetworkAvailable) {
            String username = sharedPreferences.getString(PreferenceKeys.USERNAME, "").toString();
            String password = sharedPreferences.getString(PreferenceKeys.PASSWORD, "").toString();
            User user = new User(username, password);
            manifestItemViewModel.getRetroApiManifestItemClient().getAllocatedManifestItems(
                    connectivityManager.isNetworkAvailable, user, new Manifest(manifestId));
        } else {
            Toast.makeText(getActivity(), NO_INET_CONNECTION, Toast.LENGTH_SHORT).show();
        }

        // should be here after invoking web service
        observeAnyChange();
    }

    private void observeAnyChange() {
        manifestItemViewModel.getRetroApiManifestItemClient().getManifestItems()
            .observe(getViewLifecycleOwner(), listResult -> {
                Log.d(Constants.TAG, "onChanged: LoadListFragment " + listResult);

                if (listResult instanceof Result.Error) {
                    Toast.makeText(getActivity(), Constants.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    list.clear();
                    updateControls();
                    adapter.notifyDataSetChanged();
                } else if (listResult instanceof Result.Success) {
                    List<ManifestItem> results = (List<ManifestItem>) ((Result.Success<?>) listResult).getData();
                    Log.d(Constants.TAG, "onChanged:results " + results);
                    list.clear();
                    for (ManifestItem res : results) {
                        ManifestItemListRecyclerModel recyclerModel = new ManifestItemListRecyclerModel();
                        recyclerModel.setId(res.getMovementId());
                        recyclerModel.setProductId(res.getProductId());
                        recyclerModel.setCustomerId(res.getCustomerId());
                        recyclerModel.setLoaded(res.isLoaded());
                        recyclerModel.setJobId(res.getJobId());
//                            recyclerModel.setDriver(res.getDriver());
                        recyclerModel.setQuantity(res.getQuantity());
                        list.add(recyclerModel);
                    }
                    updateControls();
                    adapter.notifyDataSetChanged();
                }
            });
    }

    private void updateControls() {
        txtViewFragmentInwardsCount.setText(String.format("%s record(s) found.", this.list.size()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manifestId = getArguments().getString("manifestId");
        Log.d(Constants.TAG, "onCreate: manifestId pass " + getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_child, container, false);
    }

    private void initControls() {
        /* Initialize title */
//        String title = String.format("%s %s", sharedService.isAllocatedSelection() ? "ALLOCATED" : "AVAILABLE", tag);
        txtViewFragmentInwards.setText(String.format("Load Allocated Manifest %s Item List", manifestId));
    }

    private void initializeRecycler(View view) {
        list = new ArrayList<>();
        adapter = new ManifestItemListRecyclerViewAdapter(list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swiperefreshlayout_fragment_child);
        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));
    }

}