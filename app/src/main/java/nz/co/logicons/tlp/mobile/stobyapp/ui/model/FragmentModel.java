package nz.co.logicons.tlp.mobile.stobyapp.ui.model;

import androidx.fragment.app.Fragment;

public class FragmentModel {
    private String title;
    private Fragment fragment;
    private int position;

    public FragmentModel() {}
    public FragmentModel(Fragment fragment, String title, int position){
        this.title = title;
        this.fragment = fragment;
        this.position = position;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }
    public void setFragment(Fragment pFragment) {
        this.fragment = fragment;
    }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }


}
