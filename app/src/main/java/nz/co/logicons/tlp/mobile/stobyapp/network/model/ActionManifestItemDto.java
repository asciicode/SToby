package nz.co.logicons.tlp.mobile.stobyapp.network.model;

/*
 * @author by Allen
 */
public class ActionManifestItemDto {
    private String action;
    private String manifestId;
    private String barcode;
    private String jobId;
    private String jobItemId;
    private String itemIndex;

    public ActionManifestItemDto()
    {
    }

    public ActionManifestItemDto(String action, String manifestId, String barcode, String jobId
            , String jobItemId, String itemIndex) {
        this.action = action;
        this.manifestId = manifestId;
        this.barcode = barcode;
        this.jobId = jobId;
        this.jobItemId = jobItemId;
        this.itemIndex = itemIndex;
    }
    public String getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(String itemIndex) {
        this.itemIndex = itemIndex;
    }
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getManifestId() {
        return manifestId;
    }

    public void setManifestId(String manifestId) {
        this.manifestId = manifestId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getJobId()
    {
        return jobId;
    }

    public void setJobId(String jobId)
    {
        this.jobId = jobId;
    }

    public String getJobItemId()
    {
        return jobItemId;
    }

    public void setJobItemId(String jobItemId)
    {
        this.jobItemId = jobItemId;
    }
}
