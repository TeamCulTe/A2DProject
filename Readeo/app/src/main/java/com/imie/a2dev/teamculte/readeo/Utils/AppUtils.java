package com.imie.a2dev.teamculte.readeo.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.imie.a2dev.teamculte.readeo.App;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Abstract utils class used to do some checks on the app/device.
 */
public abstract class AppUtils {
    /**
     * Checks if the mobile has access to the internet.
     * @return True if online else false.
     */
    public static boolean isOnline(Activity activity) {
        ConnectivityManager cM =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cM.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Initializes the ImageLoader configuration.
     */
    public static void initImageLoaderConfig() {
        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.init(ImageLoaderConfiguration.createDefault(App.getAppContext()));
    }
}
