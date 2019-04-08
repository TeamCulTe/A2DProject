package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookList;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookListType;
import com.imie.a2dev.teamculte.readeo.HTTPRequestQueueSingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        this.ids = new String[]{USER, BOOK, TYPE};
        this.baseUrl = APIManager.API_URL + APIManager.BOOK_LISTS;
    }

    /**
     * Creates a book lists entity in MySQL database.
     * @param bookList The book list to create.
     */
    public void createMySQL(BookList bookList) {

        for (Book book : bookList.getBooks()) {
            String url = this.baseUrl + APIManager.CREATE;
            Map<String, String> param = new HashMap<>();

            param.put(USER, String.valueOf(bookList.getId()));
            param.put(BOOK, String.valueOf(book.getId()));
            param.put(TYPE, String.valueOf(bookList.getType().getId()));

            super.requestString(Request.Method.POST, url, null, param);
        }
    }

    /**
     * Creates a book lists entity in MySQL database.
     * @param idUser The id of the user who owns the book list.
     * @param idBook The id of the book to add in the book list.
     * @param idType The id of the book list type.
     */
    public void createMySQL(int idUser, int idBook, int idType) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        param.put(USER, String.valueOf(idUser));
        param.put(BOOK, String.valueOf(idBook));
        param.put(TYPE, String.valueOf(idType));

        super.requestString(Request.Method.POST, url, null, param);
    }

    /**
     * Loads a book list from MySQL database.
     * @param idUser The id of the user from which load the book list.
     * @param idType The id of the book list type.
     * @return The loaded book list.
     */
    public BookList loadMySQL(int idUser, int idType) {
        // TODO : See refactor here.
        final BookList bookList = new BookList();
        final List<Integer> bookIds = new ArrayList<>();
        final int typeId[] = new int[1];
        String url = this.baseUrl + APIManager.READ + USER + "=" + idUser + "&" + TYPE + "=" + idType;

        super.requestJsonArray(Request.Method.GET, url, response -> {
            bookList.init(response);

            // TODO : See how to deal with differences between distant and local db.
            if (bookList.getBooks().size() == 0) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        bookIds.add(response.getJSONObject(i).getInt(BookListDBSchema.BOOK));
                    } catch (JSONException e) {
                        Log.e(JSON_TAG, e.getMessage());
                    }
                }
            }

            if (bookList.getType() == null) {
                try {
                    typeId[0] = response.getJSONObject(0).getInt(BookListDBSchema.TYPE);
                } catch (JSONException e) {
                    Log.e(JSON_TAG, e.getMessage());
                }
            }

            HTTPRequestQueueSingleton.getInstance(BookListDBManager.this.getContext()).finishRequest(this.getClass().getName());
        });

        this.waitForResponse();

        if (bookIds.size() > 0) {
            BookDBManager bookDBManager = new BookDBManager(this.getContext());

            for (int id : bookIds) {
                bookList.getBooks().add(bookDBManager.loadMySQL(id));
            }
        }

        if (bookList.getType() == null) {
            bookList.setType(new BookListTypeDBManager(this.getContext()).loadMySQL(typeId[0]));
        }

        return (bookList.isEmpty()) ? null : bookList;
    }

    /**
     * Loads the book lists from MySQL database from a specific user.
     * @param idUser The id of the user who owns the book lists to load.
     * @return The loaded book lists.
     */
    public Map<String, BookList> loadUserMySQL(int idUser) {
        // TODO : See refactor here.
        BookList bookList;
        final Map<String, BookList> bookLists = new HashMap<>();
        BookListTypeDBManager bookListTypeDBManager = new BookListTypeDBManager(this.getContext());

        for (BookListType type : bookListTypeDBManager.queryAllSQLite()) {
            bookList = this.loadMySQL(idUser, type.getId());

            if (bookList != null) {
                bookLists.put(bookList.getType().getName(), bookList);
            }
        }

        if (bookLists.size() == 0) {
            final List<Integer> typeIds = new ArrayList<>();
            String url = this.baseUrl + APIManager.READ + USER + "=" + idUser;

            super.requestJsonArray(Request.Method.GET, url, response -> {
                int idType;
                int previous = 0;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        idType = response.getJSONObject(i).getInt(BookListDBSchema.TYPE);
                        if (idType != previous) {
                            typeIds.add(idType);

                            previous = idType;
                        }
                    } catch (JSONException e) {
                        Log.e(JSON_TAG, e.getMessage());
                    }
                }

                HTTPRequestQueueSingleton.getInstance(BookListDBManager.this.getContext()).finishRequest(this.getClass().getName());
            });

            this.waitForResponse();

            BookListType type;

            for (int id : typeIds) {
                type = bookListTypeDBManager.loadMySQL(id);
                bookList = BookListDBManager.this.loadMySQL(idUser, id);

                if (type != null && bookList != null) {
                    bookLists.put(type.getName(), bookList);
                }
            }
        }

        return (bookLists.size() > 0) ? bookLists : null;
    }

    /**
     * Deletes all book lists entities associated to a user in MySQL database.
     * @param idUser The id of the user.
     */
    public void deleteUserMySQL(int idUser) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(USER, String.valueOf(idUser));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Deletes a book list entry in MySQL database.
     * @param idUser The id of the user who owns the book list.
     * @param idBook The id of the book to delete from the book list.
     * @param idType The id of the book list type.
     */
    public void deleteMySQL(int idUser, int idBook, int idType) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(USER, String.valueOf(idUser));
        param.put(BOOK, String.valueOf(idBook));
        param.put(TYPE, String.valueOf(idType));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Restores all book lists entities associated to a user in MySQL database.
     * @param idUser The id of the user.
     */
    public void restoreUserMySQL(int idUser) {
        String url = this.baseUrl + APIManager.RESTORE;
        Map<String, String> param = new HashMap<>();

        param.put(USER, String.valueOf(idUser));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Restores a book list entry in MySQL database.
     * @param idUser The id of the user who owns the book list.
     * @param idBook The id of the book to restore from the book list.
     * @param idType The id of the book list type.
     */
    public void restoreMySQL(int idUser, int idBook, int idType) {
        String url = this.baseUrl + APIManager.RESTORE;
        Map<String, String> param = new HashMap<>();

        param.put(USER, String.valueOf(idUser));
        param.put(BOOK, String.valueOf(idBook));
        param.put(TYPE, String.valueOf(idType));

        super.requestString(Request.Method.PUT, url, null, param);
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
