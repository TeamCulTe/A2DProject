package com.imie.a2dev.teamculte.readeo;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Class used to add HTTP request (to use the API). Using singleton pattern in order to have only one instance of
 * RequestQueue.
 */
public final class HTTPRequestQueueSingleton {
    /**
     * Stores the instance in order to apply singleton pattern.
     */
    private static HTTPRequestQueueSingleton instance;

    /**
     * Stores the associated context.
     */
    private static Context context;

    /**
     * Stores the request queue.
     */
    private RequestQueue requestQueue;

    /**
     * Private constructor to apply the pattern.
     * @param context The associated context to set.
     */
    private HTTPRequestQueueSingleton(Context context) {
        HTTPRequestQueueSingleton.context = context;
        this.requestQueue = this.getRequestQueue();
    }

    /**
     * Gets the instance attribute if exists else calls the constructor.
     * @return The instance.
     */
    public static synchronized HTTPRequestQueueSingleton getInstance(Context context) {
        if (HTTPRequestQueueSingleton.instance == null) {
            HTTPRequestQueueSingleton.instance = new HTTPRequestQueueSingleton(context);
        }

        return HTTPRequestQueueSingleton.instance;
    }

    public RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(HTTPRequestQueueSingleton.context.getApplicationContext());
        }

        return this.requestQueue;
    }

    /**
     * Adds a request to the request queue.
     * @param request The request to add.
     * @param <T> Used for type safety control.
     */
    public <T> void addToRequestQueue(Request<T> request) {
        this.getRequestQueue().add(request);
    }
}
