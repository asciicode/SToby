package nz.co.logicons.tlp.mobile.stobyapp.ui.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import nz.co.logicons.tlp.mobile.stobyapp.R;

public class DashboardRecyclerViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout layoutDashboard;
    private TextView textViewDashboard;
    private ImageButton imageButtonDashboard;

    public LinearLayout getLayoutDashboard() {
        return layoutDashboard;
    }

    public ImageButton getImageButtonDashboard() {
        return imageButtonDashboard;
    }

    public TextView getTextViewDashboard() {
        return textViewDashboard;
    }

    public DashboardRecyclerViewHolder(View itemView) {
        super(itemView);
        this.layoutDashboard = itemView.findViewById(R.id.layoutDashboard);
        this.imageButtonDashboard = itemView.findViewById(R.id.imageButtonDashboard);
        this.textViewDashboard = itemView.findViewById(R.id.textViewDashboard);
    }

}