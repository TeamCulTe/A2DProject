package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookDBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListTypeDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.imie.a2dev.teamculte.readeo.Utils.TagUtils.JSON_TAG;

/**
 * Final class representing a book list from a specific user.
 */
@Getter
@Setter
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
     * BookList's full filled constructor providing all its attributes values from a json object.
     * @param result The json array.
     */
    public BookList(JSONArray result) {
        this.init(result);
    }

    @Override
    protected void init(@NonNull Cursor result, boolean close) {
        // Not used as the BookList is a MySQL entity, not an SQLite one.
    }

    /**
     * Initializes the user from a JSON response array.
     * @param array The JSON response from the API.
     */
    public void init(@NonNull JSONArray array) {
        try {
            Book book;
            BookDBManager bookDBManager = new BookDBManager(App.getAppContext());
            JSONObject first = array.getJSONObject(0);

            for (int i = 0; i < array.length(); i++) {
                book = bookDBManager.loadSQLite(array.getJSONObject(i).getInt(BookListDBSchema.BOOK));

                if (book != null) {
                    this.books.add(book);
                }
            }

            this.id = first.getInt(BookListDBSchema.USER);
            this.type = new BookListTypeDBManager(App.getAppContext()).loadSQLite(first.getInt(BookListDBSchema.TYPE));
        } catch (JSONException e) {
            Log.e(JSON_TAG, e.getMessage());
        }
    }
}
