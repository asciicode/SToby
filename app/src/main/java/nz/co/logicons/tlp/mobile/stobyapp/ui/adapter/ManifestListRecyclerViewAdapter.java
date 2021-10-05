package nz.co.logicons.tlp.mobile.stobyapp.ui.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.R;
import nz.co.logicons.tlp.mobile.stobyapp.ui.holder.ManifestListRecyclerViewHolder;
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.ManifestListListener;
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.ManifestListOnItemListener;
import nz.co.logicons.tlp.mobile.stobyapp.ui.model.ManifestListRecyclerModel;

/*
 * @author by Allen
 */
public class ManifestListRecyclerViewAdapter extends RecyclerView.Adapter<ManifestListRecyclerViewHolder> {
    private List<ManifestListRecyclerModel> list;
    private ImageView imageIndicator;
    private ManifestListListener manifestListListener;
    private ManifestListOnItemListener itemOnClickListener;

    public ManifestListRecyclerViewAdapter(List<ManifestListRecyclerModel> list,
            ManifestListListener manifestListListener, ManifestListOnItemListener itemOnClickListener) {
        this.list = list;
        this.manifestListListener = manifestListListener;
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public ManifestListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewItem = layoutInflater.inflate(R.layout.recycler_manifest_list, parent, false);
//        RelativeLayout imageLayout = (RelativeLayout) viewItem.findViewById(R.id.imageLayout);
        imageIndicator = viewItem.findViewById(R.id.imageIndicator);
        // glide failed here
//        Glide.with(viewItem).load(R.drawable.ic_arrow_forward).into(imageIndicator);

        return new ManifestListRecyclerViewHolder(viewItem);
    }

    private void updateImageView(ViewGroup parent, ImageView imageView, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setImageDrawable(parent.getContext().getResources().getDrawable(id, parent.getContext().getApplicationContext().getTheme()));
        } else {
            imageView.setImageDrawable(parent.getContext().getResources().getDrawable(id));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ManifestListRecyclerViewHolder holder, int position) {
        ManifestListRecyclerModel manifestListRecyclerModel = list.get(position);
        holder.tvManifestId.setText(manifestListRecyclerModel.getId());
        holder.tvService.setText(manifestListRecyclerModel.getService());
        holder.tvWorkType.setText(manifestListRecyclerModel.getWorkType());
        holder.tvFrom.setText(manifestListRecyclerModel.getFrom());
        holder.tvTo.setText(manifestListRecyclerModel.getTo());
//        Glide.with(holder.itemView.getContext())
//                .load(R.drawable.logo)
//                .placeholder(R.drawable.ic_arrow_forward)
//                .into(imageIndicator);
        // updateImageView(holder.txtViewId.getContext(), imageIndicator, R.drawable.ic_arrow_forward);
        View.OnClickListener clickListener = getClickListener(manifestListRecyclerModel);
        holder.imageView.setOnClickListener(clickListener);
        holder.bind(manifestListRecyclerModel, itemOnClickListener);
    }

    private View.OnClickListener getClickListener(ManifestListRecyclerModel model) {
        return view -> manifestListListener.onClick(model);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

}
