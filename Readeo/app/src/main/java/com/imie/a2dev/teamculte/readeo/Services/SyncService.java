package com.imie.a2dev.teamculte.readeo.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;
import com.imie.a2dev.teamculte.readeo.Utils.ManagerHolderUtils;
import com.imie.a2dev.teamculte.readeo.Utils.UpdaterUtils;

import java.util.ArrayList;
import java.util.List;

public final class SyncService extends Service implements HTTPRequestQueueSingleton.HTTPRequestQueueListener {
    /**
     * Stores the list of managers used to update the databases.
     */
    private final List<DBManager> managers = new ArrayList<>();

    /**
     * Stores the current manager position.
     */
    private int currentManagerPos = 0;

    /**
     * SyncService's default constructor.
     */
    public SyncService() {
        super();
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        this.initManagers();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ServiceStarted", "The sync service started.");

        UpdaterUtils.getUpdateFromMySQL(this.managers.get(this.currentManagerPos));
        Log.i("Progress", "[DONE] -> " + HTTPRequestQueueSingleton.getInstance(this).getLastRequestUrl());

        return START_STICKY;
    }

    /**
     * Initializes the manager list.
     */
    private void initManagers() {
        this.managers.add(ManagerHolderUtils.getInstance().getCityDBManager());
        this.managers.add(ManagerHolderUtils.getInstance().getUserDBManager());
        this.managers.add(ManagerHolderUtils.getInstance().getProfileDBManager());
        this.managers.add(ManagerHolderUtils.getInstance().getBookListTypeDBManager());
        this.managers.add(ManagerHolderUtils.getInstance().getBookListDBManager());
        this.managers.add(ManagerHolderUtils.getInstance().getQuoteDBManager());
        this.managers.add(ManagerHolderUtils.getInstance().getReviewDBManager());

        HTTPRequestQueueSingleton.getInstance(this).setListener(this);
    }

    @Nullable @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public void onRequestsFinished() {
        if (this.currentManagerPos < this.managers.size()) {

            UpdaterUtils.getUpdateFromMySQL(this.managers.get(this.currentManagerPos));
            Log.i("Progress", "[DONE] -> " + HTTPRequestQueueSingleton.getInstance(this).getLastRequestUrl());

            ++this.currentManagerPos;
        } else {
            this.currentManagerPos = 0;

            Log.i("ServiceEnded", "The sync service ended.");
        }
    }

    @Override public void onRequestFinished() {
        // Nothing to do.
    }

    @Override public void onRequestError() {
        // Nothing to do.
    }
}
