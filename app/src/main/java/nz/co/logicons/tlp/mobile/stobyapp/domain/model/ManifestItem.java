package nz.co.logicons.tlp.mobile.stobyapp.domain.model;

/*
 * @author by Allen
 */
public class ManifestItem {

    private String manifestId;

    private String movementId;

    private String jobId;

    private String itemId;

    private String barCode;

    private String productId;

    private String customerId;

    private boolean loaded;

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

    public ManifestItem(){}
    public ManifestItem(String manifestId, String movementId, String jobId, String itemId, String barCode,
            String productId, String customerId, boolean loaded)
    {
        super();
        this.manifestId = manifestId;
        this.movementId = movementId;
        this.jobId = jobId;
        this.itemId = itemId;
        this.barCode = barCode;
        this.productId = productId;
        this.customerId = customerId;
        this.loaded = loaded;
    }

    public void setManifestId(String manifestId)
    {
        this.manifestId = manifestId;
    }

    public void setMovementId(String movementId)
    {
        this.movementId = movementId;
    }

    public void setJobId(String jobId)
    {
        this.jobId = jobId;
    }

    public void setItemId(String itemId)
    {
        this.itemId = itemId;
    }

    public void setBarCode(String barCode)
    {
        this.barCode = barCode;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public void setLoaded(boolean loaded)
    {
        this.loaded = loaded;
    }

    @Override
    public String toString()
    {
        return "ManifestItem [manifestId=" + manifestId + ", movementId=" + movementId + ", jobId=" + jobId + ", itemId="
                + itemId + ", barCode=" + barCode + ", productId=" + productId + ", customerId=" + customerId + ", loaded="
                + loaded + "]";
    }


}
