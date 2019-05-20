package com.imie.a2dev.teamculte.readeo.Views;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.imie.a2dev.teamculte.readeo.R;

/**
 * Activity managing the user log in or sign in.
 */
public class IndexActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.activity_index);
        
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.content_fragment, new LogInFragment());
        transaction.commit();
    }
}
