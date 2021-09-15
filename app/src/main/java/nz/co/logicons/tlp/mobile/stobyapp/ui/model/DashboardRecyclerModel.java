package nz.co.logicons.tlp.mobile.stobyapp.ui.model;

import android.app.Activity;

public class DashboardRecyclerModel {

    private int imgId;
    private String imgText;
    private String imgTextColor;
    private String imgDescription;
    private int screenId;
//    private ScreenEnum seGroup;
    private Activity activity;

    public DashboardRecyclerModel(int imgId, String imgText, String imgTextColor, int screenId, /*ScreenEnum seGroup, */Activity activity) {
        this.imgId = imgId;
        this.imgText = imgText;
        this.imgTextColor = imgTextColor;
        this.screenId = screenId;
        this.activity = activity;
//        this.seGroup = seGroup;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgButtonId) {
        this.imgId = imgButtonId;
    }

    public String getImgText() {
        return imgText;
    }

    public void setImgText(String imgText) {
        this.imgText = imgText;
    }

    public String getImgDescription() {
        return imgDescription;
    }

    public void setImgDescription(String imgDescription) {
        this.imgDescription = imgDescription;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

//    public ScreenEnum getSeGroup() {
//        return seGroup;
//    }

    public String getImgTextColor() {
        return imgTextColor;
    }

    public void setImgTextColor(String imgTextColor) {
        this.imgTextColor = imgTextColor;
    }

    @Override
    public String toString() {
        return "DashboardRecyclerModel{" +
                "imgId=" + imgId +
                ", imgText='" + imgText + '\'' +
                ", imgTextColor='" + imgTextColor + '\'' +
                ", imgDescription='" + imgDescription + '\'' +
                ", screenId=" + screenId +
                ", activity=" + activity +
                '}';
    }
}
