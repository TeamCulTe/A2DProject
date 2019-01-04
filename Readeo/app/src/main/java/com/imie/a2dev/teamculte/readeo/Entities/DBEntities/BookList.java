package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListTypeDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Final class representing a book list from a specific user.
 */
public final class BookList extends DBEntity {
    /**
     * Defines the type of the book list among "read", "reading", "to read".
     */
    private BookListType type;

    /**
     * Stores the books associated to the book list.
     */
    private List<Book> books;

    /**
     * BookList's default constructor.
     */
    public BookList() {
        super();

        this.books = new ArrayList<>();
    }

    /**
     * BookList's nearly full filled constructor providing all attributes values except for the database related ones.
     * @param type The BookListType to set.
     * @param books The list of books to set.
     */
    public BookList(BookListType type, List<Book> books) {
        super();

        this.type = type;
        this.books = books;
    }

    /**
     * BookList's full filled constructor, providing all the attribute's values.
     * @param id The id to set.
     * @param type The BookListType to set below (read, reading, to read).
     * @param books The list of books to set.
     */
    public BookList(int id, BookListType type, List<Book> books) {
        super(id);

        this.type = type;
        this.books = books;
    }

    /**
     * Gets the type attribute.
     * @return The BookListType value of type attribute.
     */
    public BookListType getType() {
        return this.type;
    }

    /**
     * Gets the books attribute.
     * @return The List<Book> value of books attribute.
     */
    public List<Book> getBooks() {
        return this.books;
    }

    /**
     * Sets the type attribute.
     * @param newType The new BookListType value to set.
     */
    public void setType(BookListType newType) {
        this.type = newType;
    }

    /**
     * Sets the books attribute.
     * @param newBooks The new List<Book> value to set.
     */
    public void setBooks(List<Book> newBooks) {
        this.books = newBooks;
    }

    @Override
    protected void init(Cursor result, boolean close) {
        // Not used as the BookList is a MySQL entity, not an SQLite one.
    }

    /**
     * Initializes the user from a JSON response array.
     * @param array The JSON response from the API.
     */
    public void init(JSONArray array) {
        try {
            Context context = App.getAppContext();
            BookDBManager bookDBManager = new BookDBManager(App.getAppContext());
            JSONObject first = array.getJSONObject(0);

            for (int i = 0; i < array.length(); i++) {
                this.books.add(bookDBManager.loadSQLite(array.getJSONObject(i).getInt(BookListDBManager.BOOK)));
            }

            this.id = first.getInt(BookListDBManager.USER);
            this.type = new BookListTypeDBManager(App.getAppContext()).loadSQLite(first.getInt(BookListDBManager.TYPE));
        } catch (JSONException e) {
            Log.e(DBManager.JSON_TAG, e.getMessage());
        }
    }
}
