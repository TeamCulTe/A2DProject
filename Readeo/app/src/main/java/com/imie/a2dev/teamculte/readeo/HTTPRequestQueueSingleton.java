package com.imie.a2dev.teamculte.readeo;

import android.content.Context;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

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
     * Stores the number of requests pending for each manager.
     */
    private Map<String, Integer> requestsPending = new LinkedHashMap<>();

    /**
     * Stores the url of the last request (used for debugging).
     */
    private String lastRequestUrl;

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
     * Called when a request is finished, simply decrement the value of pending requests to the associated manager.
     * Calls the listener method if set.
     * @param sender The associated manager.
     */
    public void finishRequest(String sender) {
        if (this.requestsPending.containsKey(sender)) {
            int requestNumber = (this.requestsPending.get(sender) - 1 > 0) ? this.requestsPending.get(sender) - 1 : 0 ;

            this.requestsPending.put(sender, requestNumber);

            if (requestNumber == 0 && this.listener != null) {
                this.listener.onRequestsFinished();
            }
        }
    }

    /**
     * Returns true if there are still request on the queue.
     * @param sender The associated manager.
     * @return True if still requests pending else false.
     */
    public boolean hasRequestPending(String sender) {
        return (this.requestsPending.containsKey(sender) && this.requestsPending.get(sender) != 0);
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

    /**
     * Gets the request queue if exists else initializes it before.
     * @return The request queue.
     */
    public RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(HTTPRequestQueueSingleton.context.getApplicationContext());
        }

        return this.requestQueue;
    }

    /**
     * Adds a request to the request queue.
     * @param sender The manager who sends the request.
     * @param request The request to add.
     * @param <T> Used for type safety control.
     */
    public <T> void addToRequestQueue(String sender, Request<T> request) {
        if (!this.requestsPending.containsKey(sender)) {
            this.requestsPending.put(sender, 1);
        } else {
            this.requestsPending.put(sender, this.requestsPending.get(sender) + 1);
        }

        this.lastRequestUrl = request.getUrl();

        request.setRetryPolicy(new DefaultRetryPolicy(5000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.getRequestQueue().add(request);
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
