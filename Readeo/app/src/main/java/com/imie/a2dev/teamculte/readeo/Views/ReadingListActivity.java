package com.imie.a2dev.teamculte.readeo.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookListType;
import com.imie.a2dev.teamculte.readeo.R;

/**
 * Activity managing the user's reading lists (displaying the list of books, that can be clicked to display further 
 * info).
 */
public final class ReadingListActivity extends AppCompatActivity {
    /**
     * Stores the type of the current book list.
     */
    private BookListType type;

    /**
     * Gets the type attribute.
     * @return The BookListType value of type attribute.
     */
    public BookListType getType() {
        return this.type;
    }

    /**
     * Sets the type attribute.
     * @param newType The new BookListType value to set.
     */
    public void setType(BookListType newType) {
        this.type = newType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.type = (BookListType) this.getIntent().getSerializableExtra(BookListTypeDBSchema.TABLE);
        
        this.setContentView(R.layout.activity_reading_list);
    }
}
