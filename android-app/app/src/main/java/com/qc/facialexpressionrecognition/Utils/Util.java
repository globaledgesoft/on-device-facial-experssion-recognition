package com.qc.facialexpressionrecognition.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

public class Util {

    private static final String TAG = Util.class.getSimpleName();

    /**
     * Method to check whether camera permission is granted or not
     *
     * @return
     */
    public static boolean hasPermission(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mContext.checkSelfPermission(Constants.PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    /**
     * Method to get device display density
     *
     * @param mContext
     * @return
     */
    public static float getDisplayDensity(Context mContext) {
        float density = mContext.getResources()
                .getDisplayMetrics()
                .density;
        Logger.d(TAG, "density " + density);
        return density;
    }

    /**
     * Shows a {@link Toast} on the UI thread.
     *
     * @param text The message to show
     */
    public static void showToast(final String text, final Activity activity) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
