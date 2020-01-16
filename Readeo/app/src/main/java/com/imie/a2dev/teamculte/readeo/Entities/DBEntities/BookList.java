package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookDBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListTypeDBManager;

import lombok.Getter;
import lombok.Setter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema.BOOK;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema.TYPE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema.USER;

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
    private List<Book> books = new ArrayList<>();

    /**
     * BookList's default constructor.
     */
    public BookList() {
        super();
    }

    /**
     * BookList's nearly full filled constructor providing all attributes values except for the database related ones.
     * @param type The BookListType to set.
     */
    public BookList(BookListType type) {
        super();

        this.type = type;
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

    /**
     * BookList's full filled constructor providing all its attributes values from a cursor object.
     * @param result The cursor.
     */
    public BookList(Cursor result) {
        this.init(result, true);
    }

    /**
     * BookList's full filled constructor providing all its attributes values from a cursor object.
     * @param result The cursor.
     * @param closed Defines if the cursor should be closed or not.
     */
    public BookList(Cursor result, boolean closed) {
        this.books = new ArrayList<>();

        this.init(result, closed);
    }

    @Override
    protected void init(@NonNull Cursor result, boolean close) {
        try {
            if (result.getPosition() == -1) {
                result.moveToNext();
            }

            Context context = App.getAppContext();
            BookDBManager bookDBManager = new BookDBManager(context);

            this.setId(result.getInt(result.getColumnIndex(USER)));
            this.setType(new BookListTypeDBManager(context).loadSQLite(result.getInt(result.getColumnIndex(TYPE))));

            do {
                this.getBooks().add(bookDBManager.loadSQLite(result.getInt(result.getColumnIndex(BOOK))));
            } while (result.moveToNext() && result.getInt(result.getColumnIndex(TYPE)) == this.getType().getId());
        } catch (SQLiteException e) {
            this.logError("init", e);
        } finally {
            if (close) {
                result.close();
            }
        }
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

            this.id = first.getInt(USER);
            this.type = new BookListTypeDBManager(App.getAppContext()).loadSQLite(first.getInt(BookListDBSchema.TYPE));
        } catch (JSONException e) {
            this.logError("init", e);
        }
    }

    /**
     * Checks if the book list contains the given book.
     * @param book The book to check.
     * @return True if the book is in the book list else false.
     */
    public boolean contains(Book book) {
        for (Book elt : this.getBooks()) {
            if (elt.getId() == book.getId()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Removes a book from the book list.
     * @param book The book to remove.
     */
    public void remove(Book book) {
        int pos = 0;

        for (Book elt : this.getBooks()) {
            if (elt.getId() == book.getId()) {
                break;
            }

            pos++;
        }

        this.getBooks().remove(pos);
    }
}
