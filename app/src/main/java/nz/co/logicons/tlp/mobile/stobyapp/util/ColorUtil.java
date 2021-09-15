package nz.co.logicons.tlp.mobile.stobyapp.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class ColorUtil {

    public static int getColorIntFromStyle(Context context, int style) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(style, typedValue, true);
        return typedValue.data;
    }
}
