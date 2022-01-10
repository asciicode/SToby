package nz.co.logicons.tlp.mobile.stobyapp.domain.model;

/*
 * @author by Allen
 */
public class ManifestItem {

    private String itemMovementId;

    private String manifestId;

    private String movementId;

    private String jobId;

    private String itemId;

    private String barCode;

    private String productId;

    private String customerId;

    private boolean loaded;

    private String itemIndex;

    private int quantity;

    public String getManifestId() {
        return manifestId;
    }

    public String getMovementId() {
        return movementId;
    }

    public String getJobId() {
        return jobId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getProductId() {
        return productId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public ManifestItem() {
    }

    public ManifestItem(String itemMovementId, String manifestId, String movementId, String jobId, String itemId,
            String barCode, String productId, String customerId, boolean loaded, String itemIndex,
            int quantity) {
        super();
        this.itemMovementId = itemMovementId;
        this.manifestId = manifestId;
        this.movementId = movementId;
        this.jobId = jobId;
        this.itemId = itemId;
        this.barCode = barCode;
        this.productId = productId;
        this.customerId = customerId;
        this.loaded = loaded;
        this.itemIndex = itemIndex;
        this.quantity  = quantity;
    }
    public String getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(String itemIndex) {
        this.itemIndex = itemIndex;
    }
    public void setManifestId(String manifestId) {
        this.manifestId = manifestId;
    }

    public void setMovementId(String movementId) {
        this.movementId = movementId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public String getItemMovementId() {
        return itemMovementId;
    }

    public void setItemMovementId(String itemMovementId) {
        this.itemMovementId = itemMovementId;
    }

    @Override
    public String toString() {
        return "ManifestItem [itemMovementId=" + itemMovementId + ", manifestId=" + manifestId + ", movementId="
                + movementId + ", jobId=" + jobId + ", itemId=" + itemId + ", barCode=" + barCode + ", productId=" + productId
                + ", customerId=" + customerId + ", loaded=" + loaded + "]";
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
