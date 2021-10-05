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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import nz.co.logicons.tlp.mobile.stobyapp.data.Result;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.Manifest;
import nz.co.logicons.tlp.mobile.stobyapp.domain.model.User;
import nz.co.logicons.tlp.mobile.stobyapp.ui.adapter.ManifestListRecyclerViewAdapter;
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.ManifestListListener;
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.ManifestListOnItemListener;
import nz.co.logicons.tlp.mobile.stobyapp.ui.model.ManifestListRecyclerModel;
import nz.co.logicons.tlp.mobile.stobyapp.ui.viewmodel.ManifestViewModel;
import nz.co.logicons.tlp.mobile.stobyapp.util.ConnectivityManager;
import nz.co.logicons.tlp.mobile.stobyapp.util.Constants;
import nz.co.logicons.tlp.mobile.stobyapp.util.PreferenceKeys;

@AndroidEntryPoint
public class ManifestListFragment extends Fragment implements ManifestListListener, ManifestListOnItemListener {
    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    SharedPreferences sharedPreferences;
    private TextView txtViewFragmentInwards;
    private TextView txtViewFragmentInwardsCount;
    private ManifestListRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<ManifestListRecyclerModel> list = new ArrayList<>();
    private ManifestViewModel manifestViewModel;
    private boolean storeLoad;

    public ManifestListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storeLoad = getArguments().getBoolean("storeLoad");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_child, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview_fragment_child);
        txtViewFragmentInwards = view.findViewById(R.id.txtview_fragment_child_title);
        txtViewFragmentInwardsCount = view.findViewById(R.id.tvRecordCount);
        initializeRecycler(view);
        initControls();


        String username = sharedPreferences.getString(PreferenceKeys.USERNAME, "").toString();
        String password = sharedPreferences.getString(PreferenceKeys.PASSWORD, "").toString();
        User user = new User(username, password);

        manifestViewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner()).get(ManifestViewModel.class);
        if (connectivityManager.isNetworkAvailable){
            Manifest manifest = new Manifest();
            manifest.setStoreLoad(storeLoad);
            manifestViewModel.getRetroApiManifestClient().allocatedManifest(
                    connectivityManager.isNetworkAvailable, manifest, user);
        }else{
            Toast.makeText(getActivity(), NO_INET_CONNECTION, Toast.LENGTH_SHORT).show();
        }

        // should be here after invoking web service
        observeAnyChange();
    }

    private void observeAnyChange() {
        manifestViewModel.getRetroApiManifestClient().getManifests()
                .observe(getViewLifecycleOwner(), new Observer<Result<List<Manifest>>>() {
            @Override
            public void onChanged(Result<List<Manifest>> listResult) {
                Log.d(Constants.TAG, "onChanged: "+listResult);

                if (listResult instanceof Result.Error){
                    String str = ((Result.Error) listResult).getError() == null ?
                            Constants.SERVER_ERROR : ((Result.Error) listResult).getError().getMessage();
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                    list.clear();
                    updateControls();
                    adapter.notifyDataSetChanged();
                }else if (listResult instanceof  Result.Success){
                    List<Manifest> results = (List<Manifest>) ((Result.Success<?>) listResult).getData();
                    Log.d(Constants.TAG, "onChanged:results "+results);
                    list.clear();
                    for (Manifest res : results){
                        ManifestListRecyclerModel manifestListRecyclerModel = new ManifestListRecyclerModel();
                        manifestListRecyclerModel.setId(res.getId());
                        manifestListRecyclerModel.setDriver(res.getDriver());
                        manifestListRecyclerModel.setService(res.getService());
                        manifestListRecyclerModel.setWorkType(res.getWorkType());
                        manifestListRecyclerModel.setFrom(res.getFrom());
                        manifestListRecyclerModel.setTo(res.getTo());
                        list.add(manifestListRecyclerModel);
                    }
                    updateControls();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void updateControls(){
        txtViewFragmentInwardsCount.setText(String.format("%s record(s) found.", this.list.size()));
    }
    private void initControls() {
        txtViewFragmentInwards.setText(String.format("%s Allocated Manifest",
                storeLoad ? "Make" :"Load"));
    }

    private void initializeRecycler(View view) {
        adapter = new ManifestListRecyclerViewAdapter(list,
                (ManifestListListener)this, (ManifestListOnItemListener) this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onClick(ManifestListRecyclerModel model) {
        Log.d(Constants.TAG, "onClick: Arrow "+model.getId());
        Bundle bundle = new Bundle();
        bundle.putString("manifestId", model.getId());
        NavController navController = Navigation.findNavController(this.getView());
        if (storeLoad){
            navController.navigate(R.id.action_manifestListFragment_to_makeMenuFragment, bundle);
        }else{
            navController.navigate(R.id.action_manifestListFragment_to_loadMenuFragment, bundle);
        }

    }

    @Override
    public void onItemClick(ManifestListRecyclerModel model) {
        Log.d(Constants.TAG, "onItemClick: "+model);
        onClick(model);
    }
}