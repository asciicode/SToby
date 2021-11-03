package nz.co.logicons.tlp.mobile.stobyapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.ui.adapter.DashboardRecyclerViewAdapter;
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.DashboardButtonListener;
import nz.co.logicons.tlp.mobile.stobyapp.ui.model.DashboardRecyclerModel;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;

public class AllocatedDashboardFragment extends Fragment implements DashboardButtonListener {
    private List<DashboardRecyclerModel> list;
    private DashboardRecyclerViewAdapter adapter;

    public AllocatedDashboardFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_allocated_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeRecycler(view);
    }

    private void initializeRecycler(View view) {
        list = new ArrayList<>();
        list.add(new DashboardRecyclerModel(R.drawable.load_cargo, "Load", "#ffffff", 1, null));
        list.add(new DashboardRecyclerModel(R.drawable.cargo, "Make", "#ffffff",2, null));
        list.add(new DashboardRecyclerModel(R.drawable.delivery, "Inward", "#ffffff",3, null));

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

        if (TextUtils.equals(model.getImgText(), "Load")){
            bundle.putBoolean("storeLoad", false);
            navController.navigate(R.id.action_mainFragment_to_manifestListFragment, bundle);
        } else  if (TextUtils.equals(model.getImgText(), "Make")){
            bundle.putBoolean("storeLoad", true);
            navController.navigate(R.id.action_mainFragment_to_manifestListFragment, bundle);
        } else  if (TextUtils.equals(model.getImgText(), "Inward")){
            navController.navigate(R.id.action_mainFragment_to_inwardScanFragment);
        }

    }
}