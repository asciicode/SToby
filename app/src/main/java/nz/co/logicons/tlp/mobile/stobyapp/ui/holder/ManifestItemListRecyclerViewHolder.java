package nz.co.logicons.tlp.mobile.stobyapp.ui.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nz.co.logicons.tlp.mobile.stobyapp.R;


public class ManifestItemListRecyclerViewHolder extends RecyclerView.ViewHolder{
    public TextView tvManifestId;
    public TextView tvJobId;
    public TextView tvProductId;
    public TextView tvCustomerId;
    public TextView tvLoaded;
    public CheckBox cbLoaded;
    public LinearLayout layoutRecyclerInwards;
    public ImageView imageView;

    public ManifestItemListRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvManifestId = itemView.findViewById(R.id.tvManifestId);
        this.tvJobId = itemView.findViewById(R.id.tvJobId);
//        this.tvProductId = itemView.findViewById(R.id.tvProductId);
//        this.tvCustomerId = itemView.findViewById(R.id.tvCustomerId);
//        this.tvLoaded = itemView.findViewById(R.id.tvLoaded);
//        this.imageView = itemView.findViewById(R.id.imageIndicator);
    }

}
