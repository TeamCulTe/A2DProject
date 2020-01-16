package com.imie.a2dev.teamculte.readeo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Class representing the application, used to get a context from anywhere in a static way.
 */
public class App extends Application {
    /**
     * Stores the log tag.
     */
    private static final String APP_TAG = "App";

    /**
     * Stores the context.
     */
    private static Context context;

    /**
     * Gets the context.
     * @return The class context.
     */
    public static Context getAppContext() {
        return App.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        App.context = this.getApplicationContext();
    }
}
