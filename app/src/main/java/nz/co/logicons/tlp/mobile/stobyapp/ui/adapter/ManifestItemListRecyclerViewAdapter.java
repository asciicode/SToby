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
import nz.co.logicons.tlp.mobile.stobyapp.ui.listener.ManifestListOnItemListener;
import nz.co.logicons.tlp.mobile.stobyapp.ui.model.ManifestItemListRecyclerModel;

/*
 * @author by Allen
 */
public class ManifestItemListRecyclerViewAdapter extends RecyclerView.Adapter<ManifestListRecyclerViewHolder> {
    private List<ManifestItemListRecyclerModel> list;
    private ImageView imageIndicator;
//    private ManifestItemListListener manifestItemListListener;
    private ManifestListOnItemListener onItemClickListener;

    public ManifestItemListRecyclerViewAdapter(List<ManifestItemListRecyclerModel> list) {
        this.list = list;
//        this.manifestItemListListener = manifestItemListListener;
    }

    @NonNull
    @Override
    public ManifestListRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewItem = layoutInflater.inflate(R.layout.recycler_manifest_item_list, parent, false);
        return new ManifestListRecyclerViewHolder(viewItem);
    }
    private void updateImageView(ViewGroup parent, ImageView imageView, int id ){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setImageDrawable( parent.getContext().getResources().getDrawable(id, parent.getContext().getApplicationContext().getTheme()));
        } else {
            imageView.setImageDrawable( parent.getContext().getResources().getDrawable(id));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ManifestListRecyclerViewHolder holder, int position) {
        ManifestItemListRecyclerModel manifestItemListRecyclerModel = list.get(position);
//        View.OnClickListener clickListener = getClickListener(manifestItemListRecyclerModel);
//        holder.imageView.setOnClickListener(clickListener);

    }
//    private View.OnClickListener getClickListener(ManifestItemListRecyclerModel model) {
//        return view -> manifestItemListListener.onClick(model);
//    }
    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public void setOnItemClickListener(ManifestListOnItemListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
