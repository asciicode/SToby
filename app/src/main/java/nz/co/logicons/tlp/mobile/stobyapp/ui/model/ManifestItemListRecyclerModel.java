package nz.co.logicons.tlp.mobile.stobyapp.ui.model;

/*
 * @author by Allen
 */
public class ManifestItemListRecyclerModel {
    private String id;
    private String manifestId;

    private String movementId;

    private String jobId;

    private String itemId;

    private String barCode;

    private String productId;

    private String customerId;

    private boolean loaded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManifestId() {
        return manifestId;
    }

    public void setManifestId(String manifestId) {
        this.manifestId = manifestId;
    }

    public String getMovementId() {
        return movementId;
    }

    public void setMovementId(String movementId) {
        this.movementId = movementId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
