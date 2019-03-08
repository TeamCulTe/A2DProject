package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.Context;
import android.support.annotation.NonNull;
import com.android.volley.Request;
import com.android.volley.Response;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookList;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookListType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema.BOOK;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema.TABLE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema.TYPE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema.USER;

/**
 * Manager class used to manage the book list entities from databases.
 */
public final class BookListDBManager extends DBManager {
    /**
     * BookListDBManager's constructor.
     * @param context The associated context.
     */
    public BookListDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.baseUrl = APIManager.API_URL + APIManager.BOOK_LISTS;
    }

    /**
     * Creates a book lists entity in MySQL database.
     * @param bookList The book list to create.
     */
    public void createMySQL(BookList bookList) {

        for (Book book : bookList.getBooks()) {
            String url = String.format(baseUrl + APIManager.CREATE + USER + "=%s&" + BOOK + "=%s&" + TYPE + "=%s",
                    bookList.getId(),
                    book.getId(),
                    bookList.getType().getId());

            super.requestString(Request.Method.POST, url, null);
        }
    }

    /**
     * Creates a book lists entity in MySQL database.
     * @param idUser The id of the user who owns the book list.
     * @param idBook The id of the book to add in the book list.
     * @param idType The id of the book list type.
     */
    public void createMySQL(int idUser, int idBook, int idType) {
        String url = String.format(baseUrl + APIManager.CREATE + USER + "=%s&" + BOOK + "=%s&" + TYPE + "=%s",
                idUser,
                idBook,
                idType);

        super.requestString(Request.Method.POST, url, null);
    }

    /**
     * Loads a book list from MySQL database.
     * @param idUser The id of the user from which load the book list.
     * @param idType The id of the book list type.
     * @return The loaded book list.
     */
    public BookList loadMySQL(int idUser, int idType) {
        final BookList bookList = new BookList();

        String url = String.format(baseUrl + APIManager.READ + USER + "=%s&" + TYPE + "=%s", idUser, idType);

        super.requestJsonArray(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                bookList.init(response);
            }
        });

        return bookList;
    }

    /**
     * Loads the book lists from MySQL database from a specific user.
     * @param idUser The id of the user who owns the book lists to load.
     * @return The loaded book lists.
     */
    public Map<String, BookList> loadUserMySQL(int idUser) {
        BookList bookList;
        final Map<String, BookList> bookLists = new HashMap<>();
        BookListTypeDBManager bookListTypeDBManager = new BookListTypeDBManager(this.getContext());

        for (BookListType type : bookListTypeDBManager.queryAllSQLite()) {
            bookList = this.loadMySQL(idUser, type.getId());

            bookLists.put(bookList.getType().getName(), bookList);
        }

        return bookLists;
    }

    /**
     * Deletes all book lists entities associated to a user in MySQL database.
     * @param idUser The id of the user.
     */
    public void deleteUserMySQL(int idUser) {
        String url = String.format(baseUrl + APIManager.DELETE + USER + "=%s", idUser);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Deletes a book list entry in MySQL database.
     * @param idUser The id of the user who owns the book list.
     * @param idBook The id of the book to delete from the book list.
     * @param idType The id of the book list type.
     */
    public void deleteMySQL(int idUser, int idBook, int idType) {
        String url = String.format(baseUrl + APIManager.DELETE + USER + "=%s&" + BOOK + "=%s&" + TYPE + "=%s",
                idUser,
                idBook,
                idType);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Restores all book lists entities associated to a user in MySQL database.
     * @param idUser The id of the user.
     */
    public void restoreUserMySQL(int idUser) {
        String url = String.format(baseUrl + APIManager.RESTORE + USER + "=%s", idUser);

        super.requestString(Request.Method.PUT, url, null);
    }

    /**
     * Restores a book list entry in MySQL database.
     * @param idUser The id of the user who owns the book list.
     * @param idBook The id of the book to restore from the book list.
     * @param idType The id of the book list type.
     */
    public void restoreMySQL(int idUser, int idBook, int idType) {
        String url = String.format(baseUrl + APIManager.RESTORE + USER + "=%s&" + BOOK + "=%s&" + TYPE + "=%s",
                idUser,
                idBook,
                idType);

        super.requestString(Request.Method.PUT, url, null);
    }

    @Override
    public boolean deleteSQLite(int id) {
        return false;
    }

    @Override
    protected void createSQLite(@NonNull JSONObject entity) {
        // Nothing to do as the entity is not a SQLite one.
    }

    @Override
    public boolean updateSQLite(@NonNull JSONObject entity) {
        // Nothing to do as the entity is not a SQLite one.
        return false;
    }
}
