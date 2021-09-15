package nz.co.logicons.tlp.mobile.stobyapp.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {

    public static void hideKeyboard(View view) {
        Object systemService = view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        InputMethodManager imm = (InputMethodManager) systemService;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
