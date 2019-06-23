package com.imie.a2dev.teamculte.readeo.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.imie.a2dev.teamculte.readeo.DBManagers.AuthorDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListTypeDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CategoryDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CityDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CountryDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.ProfileDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.QuoteDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.ReviewDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.WriterDBManager;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;
import com.imie.a2dev.teamculte.readeo.R;
import com.imie.a2dev.teamculte.readeo.Utils.ManagerHolderUtils;
import com.imie.a2dev.teamculte.readeo.Utils.UpdaterUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays the app splash screen and synchronise the databases.
 */
public final class SplashScreenActivity extends AppCompatActivity implements HTTPRequestQueueSingleton.HTTPRequestQueueListener {
    /**
     * Stores the list of DBManagers used to update the app db.
     */
    private List<DBManager> managers = new ArrayList<>();

    /**
     * Defines the current manager update.
     */
    private int currentManagerPos = 0;

    /**
     * Stores the update progress text.
     */
    private TextView updateProgress;
    
    /**
     * SplashScreenActivity's default constructor.
     */
    public SplashScreenActivity() {
    }

    @Override
    public void onRequestsFinished() {
        if (this.currentManagerPos + 1 < this.managers.size()) {
            this.currentManagerPos += 1;

            this.updateProgress.setText(String.format(this.getResources().getString(R.string.update_progress),
                    this.currentManagerPos, this.managers.size()));
            UpdaterUtils.getUpdateFromMySQL(this.managers.get(this.currentManagerPos));
        } else {
            Intent intent = new Intent(this, IndexActivity.class);

            this.startActivity(intent);
        }

        Log.i("Progress", "[DONE] -> " + HTTPRequestQueueSingleton.getInstance(this).getLastRequestUrl());
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.activity_splash_screen);

        this.initUpdate();
        this.initView();
    }
    
    private void initView() {
        this.updateProgress = this.findViewById(R.id.txt_current_progress);
        
        this.updateProgress.setText(String.format(this.getResources().getString(R.string.update_progress),
                this.currentManagerPos, this.managers.size()));
    }

    /**
     * Initializes the sync between local and distant db.
     */
    private void initUpdate() {
        managers.add(new CountryDBManager(this));
        managers.add(new CityDBManager(this));
        //managers.add(new AuthorDBManager(this));
        //managers.add(new CategoryDBManager(this));
        //managers.add(new BookListTypeDBManager(this));
        //managers.add(new BookDBManager(this));
        //managers.add(new WriterDBManager(this));
        managers.add(new ProfileDBManager(this));
        managers.add(new UserDBManager(this));
        managers.add(new QuoteDBManager(this));
        managers.add(new ReviewDBManager(this));

        HTTPRequestQueueSingleton.getInstance(this).setListener(this);
        UpdaterUtils.getUpdateFromMySQL(this.managers.get(0));
    }
}
