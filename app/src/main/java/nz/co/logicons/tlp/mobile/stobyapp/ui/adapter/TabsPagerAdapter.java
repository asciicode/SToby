package nz.co.logicons.tlp.mobile.stobyapp.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import nz.co.logicons.tlp.mobile.stobyapp.ui.model.FragmentModel;


@SuppressWarnings("deprecation")
public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    private List<FragmentModel> lstFragments = new ArrayList<>();
    private int lockedIndex = -1;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public int getCount() {
        if (lockedIndex >= 0)
            return 1;
        return lstFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.getModel(position).getTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (lockedIndex >= 0)
            return lstFragments.get(lockedIndex).getFragment();
        return lstFragments.stream().filter(a->a.getPosition()==position).map(FragmentModel::getFragment).findFirst().orElse(new Fragment());
    }

    /*
     * User defined functions
     */

    public void addFragment(Fragment fragment, String title) {
        lstFragments.add(new FragmentModel(fragment, title, lstFragments.size()));
    }

    public FragmentModel getModel(int position) {
        if (lockedIndex >= 0)
            return lstFragments.get(lockedIndex);
        return lstFragments.stream().filter(a->a.getPosition()==position).findFirst().orElse(new FragmentModel());
    }

    public void setLockedIndex(int lockedIndex) {
        this.lockedIndex = lockedIndex;

        /*
         * Refreshes and calls its functions: getCount(), getPageTitle(), getItem()
         */
        notifyDataSetChanged();
    }

    public void clearFragments() {
        for (FragmentModel model : lstFragments) {
            if (model.getPosition() > 0)
                lstFragments.remove(model.getPosition());
        }
    }

}
