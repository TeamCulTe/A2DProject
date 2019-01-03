package com.imie.a2dev.teamculte.readeo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

//TODO : See if working.

/**
 * Class representing the application, used to get a context from anywhere in a static way.
 */
public class App extends Application {
    /**
     * Stores the context.
     */
    private static Context context;

    /**
     * Stores the log tag.
     */
    private final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        App.context = this.getApplicationContext();
    }

    /**
     * Gets the context.
     * @return The class context.
     */
    public static Context getAppContext() {
        return App.context;
    }

    /**
     * Gets the current application, used to get a context.
     * @return The current application.
     */
    public static Application getAppWithReflection() {
        try {
            return (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication")
                    .invoke(null, (Object[]) null);
        } catch (Exception e) {
            Log.e("App", e.getMessage());
            return null;
        }
    }
}
