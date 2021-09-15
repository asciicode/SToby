package nz.co.logicons.tlp.mobile.stobyapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.ui.adapter.ManifestItemListRecyclerViewAdapter;
import nz.co.logicons.tlp.mobile.stobyapp.ui.model.ManifestItemListRecyclerModel;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;

public class LoadListFragment extends Fragment {
    private TextView txtViewFragmentInwards;
    private TextView txtViewFragmentInwardsCount;
    private ManifestItemListRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<ManifestItemListRecyclerModel> list;
    private String manifestId;

    public LoadListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtViewFragmentInwards = view.findViewById(R.id.txtview_fragment_child_title);
        recyclerView = view.findViewById(R.id.recyclerview_fragment_child);
        initializeRecycler(view);
        initControls();
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
        return inflater.inflate(R.layout.fragment_recycler_child, container, false);
    }
    private void initControls() {
        /* Initialize title */
//        String title = String.format("%s %s", sharedService.isAllocatedSelection() ? "ALLOCATED" : "AVAILABLE", tag);
        txtViewFragmentInwards.setText(String.format("Load Allocated Manifest %s Item List", manifestId));
    }
    private void initializeRecycler(View view) {
        list = new ArrayList<>();
        ManifestItemListRecyclerModel manifestItemListRecyclerModel = new ManifestItemListRecyclerModel();
        manifestItemListRecyclerModel.setId("1");
        manifestItemListRecyclerModel.setDepot("Auckland");
        list.add(manifestItemListRecyclerModel);
        manifestItemListRecyclerModel = new ManifestItemListRecyclerModel();
        manifestItemListRecyclerModel.setId("2");
        manifestItemListRecyclerModel.setDepot("Welly");
        list.add(manifestItemListRecyclerModel);
        adapter = new ManifestItemListRecyclerViewAdapter(list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

}