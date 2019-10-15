package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookListDBSchema;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookList;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookListType;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;
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
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;

/**
 * Manager class used to manage the book list entities from databases.
 */
public final class BookListDBManager extends RelationDBManager {
    /**
     * BookListDBManager's constructor.
     * @param context The associated context.
     */
    public BookListDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{USER, TYPE, BOOK};
        this.baseUrl = APIManager.API_URL + APIManager.BOOK_LISTS;
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull BookList entity) {
        this.database.beginTransaction();
        
        try {
            ContentValues data;

            for (Book book : entity.getBooks()) {
                data = new ContentValues();

                data.put(USER, entity.getId());
                data.put(TYPE, entity.getType().getId());
                data.put(BOOK, book.getId());
                
                this.database.insertOrThrow(this.table, null, data);
            }

            this.database.setTransactionSuccessful();

            return true;
        } catch (SQLiteException e) {
            this.logError("createSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
        }
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean updateSQLite(@NonNull BookList entity) {
        this.database.beginTransaction();
        
        try {
            this.deleteSQLite(entity.getId(), entity.getType().getId());
            
            boolean success = this.createSQLite(entity);

            this.database.setTransactionSuccessful();

            return success;
        } catch (SQLiteException e) {
            this.logError("updateSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
        }
    }

    /**
     * From a user id, returns the associated list of java entities.
     * @param idUser The id of the user.
     * @return The loaded list of entities if exists else null.
     */
    public Map<String, BookList> loadUserSQLite(int idUser) {
        String query = String.format(SIMPLE_QUERY_ALL, this.table, USER);
        Cursor cursor = this.database.rawQuery(query, new String[]{String.valueOf(idUser)});
        BookList bookList;
        Map<String, BookList> userBookLists = new HashMap<>();

        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        while (cursor.moveToNext()) {
            bookList = new BookList(cursor, false);

            userBookLists.put(bookList.getType().getName(), bookList);
        }

        cursor.close();

        return userBookLists;
    }

    /**
     * From a couple of ids, returns the associated java entity.
     * @param idUser The id of the user.
     * @param idType The id of the book list type.
     * @return The loaded list of entities if exists else null.
     */
    public BookList loadSQLite(int idUser, int idType) {
        String query = String.format(DOUBLE_QUERY_ALL, this.table, USER, TYPE);
        Cursor cursor = this.database.rawQuery(query, new String[]{String.valueOf(idUser), String.valueOf(idType)});

        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        return new BookList(cursor);
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
            param.put(TYPE, String.valueOf(bookList.getType().getId()));
            param.put(BOOK, String.valueOf(book.getId()));

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
        param.put(TYPE, String.valueOf(idType));
        param.put(BOOK, String.valueOf(idBook));

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
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, null, new OnRequestError()) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(response.data,
                                                                   HttpHeaderParser.parseCharset(response.headers)));

                    bookList.init(jsonArray);

                    // TODO : See how to deal with differences between distant and local db.
                    if (bookList.getBooks().size() == 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                bookIds.add(jsonArray.getJSONObject(i).getInt(BookListDBSchema.BOOK));
                            } catch (JSONException e) {
                                BookListDBManager.this.logError("loadMySQL", e);
                            }
                        }
                    }

                    if (bookList.getType() == null) {
                        try {
                            typeId[0] = jsonArray.getJSONObject(0).getInt(BookListDBSchema.TYPE);
                        } catch (JSONException e) {
                            BookListDBManager.this.logError("loadMySQL", e);
                        }
                    }
                } catch (Exception e) {
                    BookListDBManager.this.logError("loadMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(BookListDBManager.this.getContext()).finishRequest(
                            BookListDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(BookListDBManager.this.getContext()).finishRequest(
                        BookListDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
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
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, null,
                                                            new OnRequestError()) {
                @Override
                protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                    try {
                        int idType;

                        JSONArray jsonArray = new JSONArray(new String(response.data,
                                                                       HttpHeaderParser
                                                                               .parseCharset(response.headers)));
                        int previous = 0;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            idType = jsonArray.getJSONObject(i).getInt(BookListDBSchema.TYPE);

                            if (idType != previous) {
                                typeIds.add(idType);

                                previous = idType;
                            }
                        }
                    } catch (Exception e) {
                        BookListDBManager.this.logError("loadUserMySQL", e);
                    } finally {
                        HTTPRequestQueueSingleton.getInstance(BookListDBManager.this.getContext()).finishRequest(
                                BookListDBManager.this.table);
                    }

                    return super.parseNetworkResponse(response);
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    HTTPRequestQueueSingleton.getInstance(BookListDBManager.this.getContext()).finishRequest(
                            BookListDBManager.this.table);

                    return super.parseNetworkError(volleyError);
                }
            };

            HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
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
    public boolean createSQLite(@NonNull JSONObject entity) {
        this.database.beginTransaction();
        
        try {
            ContentValues data = new ContentValues();

            data.put(USER, entity.getInt(USER));
            data.put(BOOK, entity.getInt(BOOK));
            data.put(TYPE, entity.getInt(TYPE));

            this.database.insertOrThrow(this.table, null, data);
            this.database.setTransactionSuccessful();
            
            return true;
        } catch (Exception e) {
            this.logError("createSQLite", e);
            
            return false;
        } finally {
            this.database.endTransaction();
        }
    }

    @Override
    public boolean updateSQLite(@NonNull JSONObject entity) {
        // Nothing to do as the entity is not supposed to be updated..
        return false;
    }
}
