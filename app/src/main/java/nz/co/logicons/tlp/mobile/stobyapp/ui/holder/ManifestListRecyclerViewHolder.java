package nz.co.logicons.tlp.mobile.stobyapp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nz.co.logicons.tlp.mobile.stobyapp.R;
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.ManifestListOnItemListener;
import nz.co.logicons.tlp.mobile.stobyapp.ui.model.ManifestListRecyclerModel;


public class ManifestListRecyclerViewHolder extends RecyclerView.ViewHolder{
    public TextView tvManifestId;
    public TextView tvService;
    public TextView tvWorkType;
    public TextView tvDriver;
    public TextView tvFrom;
    public TextView tvTo;
    public LinearLayout layoutRecyclerInwards;
    public ImageView imageView;

    public ManifestListRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.tvManifestId = itemView.findViewById(R.id.tvManifestId);
        this.tvService = itemView.findViewById(R.id.tvService);
        this.tvWorkType = itemView.findViewById(R.id.tvWorkType);
        this.tvFrom = itemView.findViewById(R.id.tvFrom);
        this.tvTo = itemView.findViewById(R.id.tvTo);
//        this.layoutRecyclerInwards = itemView.findViewById(R.id.layout_recycler_inwards_border);
        this.imageView = itemView.findViewById(R.id.imageIndicator);
    }


    public void bind(ManifestListRecyclerModel model, ManifestListOnItemListener manifestListOnItemListener){
        itemView.setOnClickListener(view ->  {
                manifestListOnItemListener.onItemClick(model);
        });
    }
}
