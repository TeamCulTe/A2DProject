package com.imie.a2dev.teamculte.readeo.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookList;
import com.imie.a2dev.teamculte.readeo.R;

/**
 * Activity managing the user's reading lists (displaying the list of books, that can be clicked to display further
 * info).
 */
public final class ReadingListActivity extends AppCompatActivity {
    /**
     * Stores the current book list.
     */
    private BookList bookList;

    /**
     * Gets the bookList attribute.
     * @return The BookList value of bookList attribute.
     */
    public BookList getBookList() {
        return this.bookList;
    }

    /**
     * Sets the bookList attribute.
     * @param newBookList The new BookList value to set.
     */
    public void setBookList(BookList newBookList) {
        this.bookList = newBookList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.bookList = (BookList) this.getIntent().getSerializableExtra(BookListDBSchema.TABLE);

        this.setContentView(R.layout.activity_reading_list);
    }
}
