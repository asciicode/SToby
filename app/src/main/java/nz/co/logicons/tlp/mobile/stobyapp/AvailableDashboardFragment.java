package nz.co.logicons.tlp.mobile.stobyapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.ui.adapter.DashboardRecyclerViewAdapter;
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.DashboardButtonListener;
import nz.co.logicons.tlp.mobile.stobyapp.ui.model.DashboardRecyclerModel;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;

public class AvailableDashboardFragment extends Fragment implements DashboardButtonListener {
    private List<DashboardRecyclerModel> list;
    private DashboardRecyclerViewAdapter adapter;

    public AvailableDashboardFragment() {
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
        return inflater.inflate(R.layout.fragment_available_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeRecycler(view);
    }

    private void initializeRecycler(View view) {
        list = new ArrayList<>();
//        list.add(new DashboardRecyclerModel(R.drawable.ic_inwards_white, "INWARDS", "#ffffff", 1, getActivity()));
//        list.add(new DashboardRecyclerModel(R.drawable.ic_outwards_white, "OUTWARDS", "#ffffff",2, getActivity()));
//        list.add(new DashboardRecyclerModel(R.drawable.ic_transfers_white, "TRANSFERS", "#ffffff",3, getActivity()));
        list.add(new DashboardRecyclerModel(R.drawable.ic_stocktake_white, "Stock Take", "#ffffff",4, null));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_available);
        adapter = new DashboardRecyclerViewAdapter(list, this);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(DashboardRecyclerModel model) {
        Log.d(Constants.TAG, "onClick: available ");
    }
}