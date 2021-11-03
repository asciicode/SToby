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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nz.co.logicons.tlp.mobile.stobyapp.ui.adapter.DashboardRecyclerViewAdapter;
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.DashboardButtonListener;
import nz.co.logicons.tlp.mobile.stobyapp.ui.model.DashboardRecyclerModel;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;

public class MakeMenuFragment extends Fragment implements DashboardButtonListener {
    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    SharedPreferences sharedPreferences;
    private List<DashboardRecyclerModel> list;
    private DashboardRecyclerViewAdapter adapter;
    private String manifestId;

    public MakeMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manifestId = getArguments().getString("manifestId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_make_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRecycler(view);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(String.format("Make Manifest %s ", manifestId));
    }

    private void initializeRecycler(View view) {
        Log.d(Constants.TAG, "initializeRecycler: make menu frag ");
        list = new ArrayList<>();
        list.add(new DashboardRecyclerModel(R.drawable.list_cargo, Constants.LIST_ITEMS, "#ffffff", 1, getActivity()));
        list.add(new DashboardRecyclerModel(R.drawable.barcode, Constants.SCAN_ITEMS, "#ffffff",2, getActivity()));

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
        if (TextUtils.equals(model.getImgText(), Constants.LIST_ITEMS)){
            navController.navigate(R.id.action_makeMenuFragment_to_makeListFragment, bundle);
        } else  if (TextUtils.equals(model.getImgText(), Constants.SCAN_ITEMS)){
            navController.navigate(R.id.action_makeMenuFragment_to_makeScanFragment, bundle);
        }
    }
}