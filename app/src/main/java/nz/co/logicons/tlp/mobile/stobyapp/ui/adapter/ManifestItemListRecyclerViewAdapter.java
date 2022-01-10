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
import nz.co.logicons.tlp.mobile.stobyapp.ui.holder.ManifestItemListRecyclerViewHolder;
import nz.co.logicons.tlp.mobile.stobyapp.ui.model.ManifestItemListRecyclerModel;

/*
 * @author by Allen
 */
public class ManifestItemListRecyclerViewAdapter extends RecyclerView.Adapter<ManifestItemListRecyclerViewHolder> {
    private List<ManifestItemListRecyclerModel> list;
    private ImageView imageIndicator;
    //    private ManifestItemListListener manifestItemListListener;

    public ManifestItemListRecyclerViewAdapter(List<ManifestItemListRecyclerModel> list) {
        this.list = list;
//        this.manifestItemListListener = manifestItemListListener;
    }

    @NonNull
    @Override
    public ManifestItemListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewItem = layoutInflater.inflate(R.layout.recycler_manifest_item_list, parent, false);
        return new ManifestItemListRecyclerViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ManifestItemListRecyclerViewHolder holder, int position) {
        ManifestItemListRecyclerModel manifestItemListRecyclerModel = list.get(position);
        String tmp = String.format("Job - %s, Customer - %s, Product - %s, Quantity - %s, Loaded - %s"
                , manifestItemListRecyclerModel.getJobId(), manifestItemListRecyclerModel.getCustomerId()
                , manifestItemListRecyclerModel.getProductId(), manifestItemListRecyclerModel.getQuantity()
                , manifestItemListRecyclerModel.isLoaded());
        holder.tvJobId.setText(tmp);
//        holder.tvProductId.setText(manifestItemListRecyclerModel.getProductId());
//        holder.tvCustomerId.setText(manifestItemListRecyclerModel.getCustomerId());
//        holder.tvLoaded.setText("" + manifestItemListRecyclerModel.isLoaded());
//        holder.cbLoaded.setChecked(manifestItemListRecyclerModel.isLoaded());
    }

    private void updateImageView(ViewGroup parent, ImageView imageView, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setImageDrawable(parent.getContext().getResources().getDrawable(id, parent.getContext().getApplicationContext().getTheme()));
        } else {
            imageView.setImageDrawable(parent.getContext().getResources().getDrawable(id));
        }
    }
    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


}
