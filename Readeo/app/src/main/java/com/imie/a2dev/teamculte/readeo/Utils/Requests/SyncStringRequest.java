package com.imie.a2dev.teamculte.readeo.Utils.Requests;

import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.imie.a2dev.teamculte.readeo.Utils.TagUtils;

import java.io.IOException;

/**
 * Custom StringRequest class using the sync mechanisms.
 */
public final class SyncStringRequest extends StringRequest {
    /**
     * Stores the associated listener.
     */
    private SyncStringRequestListener listener;

    /**
     * SyncStringRequest's constructor matching super (without listener as mechanism don't work with sync).
     * @param method The method to use.
     * @param url The url to query.
     */
    public SyncStringRequest(int method, String url) {
        super(method, url, null, null);
    }

    /**
     * Gets the listener attribute.
     * @return The SyncStringRequestListener value of listener attribute.
     */
    public SyncStringRequestListener getListener() {
        return this.listener;
    }

    /**
     * Sets the listener attribute.
     * @param newListener The new SyncStringRequestListener value to set.
     */
    public void setListener(SyncStringRequestListener newListener) {
        this.listener = newListener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        if (this.listener != null) {
            String strResponse = "";
            
            try {
                strResponse = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } catch (IOException e) {
                Log.e(TagUtils.ERROR_TAG, "The string response parsing failed.");
            }
            
            this.listener.onStringResponse(strResponse);
        }
        
        return super.parseNetworkResponse(response);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        
        
        return super.parseNetworkError(volleyError);
    }

    /**
     * Listener interface implemented by DBManagers classes to be notified on responses. 
     */
    interface SyncStringRequestListener {
        /**
         * Called when receiving the string response.
         * @param response The response.
         */
        void onStringResponse(String response);

        /**
         * Called on error response.
         * @param error The associated error.
         */
        void onStringErrorResponse(VolleyError error);
    }
}
