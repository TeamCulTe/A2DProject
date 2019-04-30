package com.imie.a2dev.teamculte.readeo.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.imie.a2dev.teamculte.readeo.R;

/**
 * Activity managing the user's reading lists (displaying the list of books, that can be clicked to display further 
 * info).
 */
public class ReadingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_list);
    }
}
