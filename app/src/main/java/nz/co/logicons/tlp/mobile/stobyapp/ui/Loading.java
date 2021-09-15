package nz.co.logicons.tlp.mobile.stobyapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import nz.co.logicons.tlp.mobile.stobyapp.R;

/*
 * @author by Allen
 */
public class Loading {
    private AlertDialog alertDialog;
    private Activity activity;

    public Loading(Activity activity) {
        this.activity = activity;
    }

    public void start() {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.loading, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismiss() {
        alertDialog.dismiss();
    }
}
