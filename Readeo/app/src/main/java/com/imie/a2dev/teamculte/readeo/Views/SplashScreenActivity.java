package com.imie.a2dev.teamculte.readeo.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.imie.a2dev.teamculte.readeo.R;

/**
 * Displays the app splash screen and synchronise the databases.
 */
public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }
}
