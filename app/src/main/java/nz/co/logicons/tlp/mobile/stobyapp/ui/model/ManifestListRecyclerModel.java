package nz.co.logicons.tlp.mobile.stobyapp.ui.model;

/*
 * @author by Allen
 */
public class ManifestListRecyclerModel {
    private String id;
    private String service;
    private String workType;
    private String driver;
    private String from;
    private String to;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String depot) {
        this.driver = depot;
    }


}
