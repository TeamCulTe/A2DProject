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

import java.util.ArrayList;
import java.util.List;

/**
 * Manager class used to manage the book list entities from databases.
 */
public final class BookListDBManager extends DBManager {
    /**
     * Defines the book list's table name.
     */
    public static final String TABLE = "BookList";

    /**
     * Defines the book list's id field.
     */
    public static final String USER = UserDBManager.ID;

    /**
     * Defines the book list's avatar field.
     */
    public static final String BOOK = BookDBManager.ID;

    /**
     * Defines the book list's description field.
     */
    public static final String TYPE = "id_book_list_type";

    /**
     * Stores the base of the book lists API url.
     */
    private final String baseUrl = APIManager.API_URL + APIManager.BOOK_LISTS;

    /**
     * BookListDBManager's constructor.
     * @param context The associated context.
     */
    public BookListDBManager(Context context) {
        super(context);
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
    public List<BookList> loadUserMySQL(int idUser) {
        final ArrayList<BookList> bookLists = new ArrayList<>();
        BookListTypeDBManager bookListTypeDBManager = new BookListTypeDBManager(this.getContext());

        for (BookListType type : bookListTypeDBManager.queryAllSQLite()) {
            bookLists.add(this.loadMySQL(idUser, type.getId()));
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
    protected void createSQLite(@NonNull JSONObject entity) {
        // Nothing to do as the entity is not a SQLite one.
    }
}
