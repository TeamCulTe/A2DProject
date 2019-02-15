package com.imie.a2dev.teamculte.readeo;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import lombok.Getter;
import lombok.Setter;

/**
 * Class used to add HTTP request (to use the API). Using singleton pattern in order to have only one instance of
 * RequestQueue.
 */
@Getter
@Setter
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
     * Stores the number of requests.
     */
    private int requestNumber;

    /**
     * Stores the number of finished requests.
     */
    private int requestFinished;

    /**
     * Stores the listener in order to notify it when the request from the queue have been passed.
     */
    private HTTPRequestQueueListener listener;

    /**
     * Private constructor to apply the pattern.
     * @param context The associated context to set.
     */
    private HTTPRequestQueueSingleton(Context context) {
        HTTPRequestQueueSingleton.context = context;
        this.requestQueue = this.getRequestQueue();
    }

    /**
     * Called when a request is finished, simply increment the finished request attribute and check if all the
     * requests have been passed (notify the listener and reinitializes the counters).
     */
    public void finishRequest() {
        this.requestFinished++;

        if (this.requestFinished == this.requestNumber) {
            this.listener.onRequestsFinished();

            this.requestNumber = 0;
            this.requestFinished = 0;
        }
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
        this.requestNumber++;
    }

    /**
     * Interface used to implement pattern observer on the HTTPRequestQueueSingleton (notify when all the requests
     * have been passed).
     */
    public interface HTTPRequestQueueListener {
        /**
         * Called when the requests from the queue are finished.
         */
        void onRequestsFinished();
    }
}
