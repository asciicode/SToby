package nz.co.logicons.tlp.mobile.stobyapp.ui.listener;

import androidx.fragment.app.FragmentActivity;

/*
 * @author by Allen
 */
public class IgnoreBackPressedListener implements OnBackPressedListener {
    private final FragmentActivity activity;

    public IgnoreBackPressedListener(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void doBack() {
//        activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}