package nz.co.logicons.tlp.mobile.stobyapp.network.model;

/*
 * @author by Allen
 */
public class ManifestDto {
    private String id;
    private boolean allocated;
    private String service;
    private String workType;
    private String driver;
    private String vehicle;
    private String trailer1;
    private String trailer2;
    private String trailer3;
    private String from;
    private String to;
    private boolean storeLoad;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }

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

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getTrailer1() {
        return trailer1;
    }

    public void setTrailer1(String trailer1) {
        this.trailer1 = trailer1;
    }

    public String getTrailer2() {
        return trailer2;
    }

    public void setTrailer2(String trailer2) {
        this.trailer2 = trailer2;
    }

    public String getTrailer3() {
        return trailer3;
    }

    public void setTrailer3(String trailer3) {
        this.trailer3 = trailer3;
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

    public boolean isStoreLoad() {
        return storeLoad;
    }

    public void setStoreLoad(boolean storeLoad) {
        this.storeLoad = storeLoad;
    }
}
