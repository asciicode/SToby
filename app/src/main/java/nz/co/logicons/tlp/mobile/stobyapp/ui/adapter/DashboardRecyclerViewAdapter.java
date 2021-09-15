package nz.co.logicons.tlp.mobile.stobyapp.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.R;
import nz.co.logicons.tlp.mobile.stobyapp.ui.holder.DashboardRecyclerViewHolder;
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.DashboardButtonListener;
import nz.co.logicons.tlp.mobile.stobyapp.ui.model.DashboardRecyclerModel;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<DashboardRecyclerViewHolder> {
    private DashboardButtonListener response;
    private List<DashboardRecyclerModel> listDashboardModel;

    public DashboardRecyclerViewAdapter(List<DashboardRecyclerModel> list, DashboardButtonListener response) {
        this.listDashboardModel = list;
        this.response = response;
    }

    @Override
    public int getItemCount() {
        return listDashboardModel.size();
    }

    @Override
    @NonNull
    public DashboardRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewItem = layoutInflater.inflate(R.layout.recycler_main, parent, false);
        return new DashboardRecyclerViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(DashboardRecyclerViewHolder holder, final int position) {
        DashboardRecyclerModel model = listDashboardModel.get(position);
        holder.getImageButtonDashboard().setImageResource(model.getImgId());
        holder.getTextViewDashboard().setTextColor(Color.parseColor(model.getImgTextColor()));
        holder.getTextViewDashboard().setText(model.getImgText());
        View.OnClickListener clickListener = getClickListener(model);
        holder.getImageButtonDashboard().setOnClickListener(clickListener);
        holder.getLayoutDashboard().setOnClickListener(clickListener);
        holder.itemView.setSelected(false);

    }

    private View.OnClickListener getClickListener(DashboardRecyclerModel model) {
        return view -> response.onClick(model);
    }

}
