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
    public TextView txtViewId;
    private TextView txtViewInwardsDocket;
    private TextView txtViewInwardCustomer;
    private TextView txtViewInwardWarehouse;
    private TextView txtViewInwardLocation;
    private TextView txtViewInwardProduct;
    private TextView txtViewInwardQuantity;
    private LinearLayout layoutRecyclerInwards;
    public ImageView imageView;

    public ManifestListRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.txtViewId = itemView.findViewById(R.id.tvManifestId);
//        this.txtViewInwardsDocket = itemView.findViewById(R.id.txtview_inwarddocket_value);
//        this.txtViewInwardCustomer = itemView.findViewById(R.id.txtview_inwardcustomer_value);
//        this.txtViewInwardWarehouse = itemView.findViewById(R.id.txtview_inwardwarehouse_value);
//        this.txtViewInwardLocation = itemView.findViewById(R.id.txtview_inwardlocation_value);
//        this.txtViewInwardProduct = itemView.findViewById(R.id.txtview_inwardproduct_value);
//        this.txtViewInwardQuantity = itemView.findViewById(R.id.txtview_inwardquantity_value);
//        this.layoutRecyclerInwards = itemView.findViewById(R.id.layout_recycler_inwards_border);
        this.imageView = itemView.findViewById(R.id.imageIndicator);
    }

    public TextView getTxtViewId() {
        return txtViewId;
    }

    public TextView getTxtViewInwardsDocket() {
        return txtViewInwardsDocket;
    }

    public TextView getTxtViewInwardCustomer() {
        return txtViewInwardCustomer;
    }

    public TextView getTxtViewInwardWarehouse() {
        return txtViewInwardWarehouse;
    }

    public LinearLayout getLayoutRecyclerInwards() {
        return layoutRecyclerInwards;
    }

    public TextView getTxtViewInwardProduct() {
        return txtViewInwardProduct;
    }

    public TextView getTxtViewInwardLocation() {
        return txtViewInwardLocation;
    }

    public TextView getTxtViewInwardQuantity() {
        return txtViewInwardQuantity;
    }

    public void bind(ManifestListRecyclerModel model, ManifestListOnItemListener manifestListOnItemListener){
        itemView.setOnClickListener(view ->  {
                manifestListOnItemListener.onItemClick(model);
        });
    }
}
