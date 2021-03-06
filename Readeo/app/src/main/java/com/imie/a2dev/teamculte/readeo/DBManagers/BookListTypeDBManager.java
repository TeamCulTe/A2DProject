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
import com.android.volley.toolbox.StringRequest;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookListType;
import com.imie.a2dev.teamculte.readeo.Utils.HTTPRequestQueueSingleton;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema.IMAGE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema.NAME;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.BookListTypeDBSchema.TABLE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.DEFAULT_FORMAT;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;

/**
 * Manager class used to manage the book list type entities from databases.
 */
public final class BookListTypeDBManager extends SimpleDBManager {
    /**
     * BookListTypeDBManager's constructor.
     * @param context The associated context.
     */
    public BookListTypeDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
        this.baseUrl = APIManager.API_URL + APIManager.BOOK_LIST_TYPES;
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull BookListType entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(NAME, entity.getName());
            data.put(IMAGE, entity.getImage());

            this.database.insertOrThrow(this.table, null, data);

            return true;
        } catch (SQLiteException e) {
            this.logError("createSQLite", e);

            return false;
        }
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean updateSQLite(@NonNull BookListType entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(NAME, entity.getName());
            data.put(IMAGE, entity.getImage());
            data.put(UPDATE, new DateTime().toString(DEFAULT_FORMAT));

            return this.database.update(this.table, data, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            this.logError("updateSQLite", e);

            return false;
        }
    }

    /**
     * From an id, returns the associated java entity.
     * @param id The id of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public BookListType loadSQLite(int id) {
        Cursor result = this.loadCursorSQLite(id);

        if (result == null || result.getCount() == 0) {
            return null;
        }

        return new BookListType(result);
    }

    /**
     * Queries all the book list type from the database.
     * @return The list of book list type.
     */
    public List<BookListType> queryAllSQLite() {
        List<BookListType> bookListTypes = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(this.QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    bookListTypes.add(new BookListType(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("queryAllSQLite", e);
        }

        return bookListTypes;
    }

    /**
     * From the API, query the list of all categories from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(baseUrl + APIManager.READ);
    }

    /**
     * Creates a book list type entity in MySQL database.
     * @param type The book list type to create.
     */
    public void createMySQL(final BookListType type) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        if (type.getId() != 0) {
            param.put(ID, String.valueOf(type.getId()));
        }

        param.put(NAME, type.getName());
        param.put(IMAGE, type.getImage());

        StringRequest request = new StringRequest(Request.Method.POST, url, null, new OnRequestError()) {
            @Override
            protected Map<String, String> getParams() {
                return param;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(BookListTypeDBManager.this.getContext())
                                         .finishRequest(BookListTypeDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String resp = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Pattern pattern = Pattern.compile("^\\d.$");

                    if (pattern.matcher(resp).find()) {
                        type.setId(Integer.valueOf(resp));
                    }
                } catch (IOException e) {
                    BookListTypeDBManager.this.logError("createMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(BookListTypeDBManager.this.getContext())
                                             .finishRequest(BookListTypeDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();
    }

    /**
     * Loads a book list type from MySQL database.
     * @param idType The id of the book list type.
     * @return The loaded book list type.
     */
    public BookListType loadMySQL(int idType) {
        final BookListType bookListType = new BookListType();
        String url = this.baseUrl + APIManager.READ + ID + "=" + idType;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, null, new OnRequestError()) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(response.data,
                                                                   HttpHeaderParser.parseCharset(response.headers)));
                    JSONObject object = jsonArray.getJSONObject(0);

                    bookListType.init(object);
                } catch (Exception e) {
                    BookListTypeDBManager.this.logError("loadMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(BookListTypeDBManager.this.getContext())
                                             .finishRequest(BookListTypeDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(BookListTypeDBManager.this.getContext())
                                         .finishRequest(BookListTypeDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();

        return (bookListType.isEmpty()) ? null : bookListType;
    }

    @Override
    public boolean createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(NAME, entity.getString(NAME));
            data.put(IMAGE, entity.getString(IMAGE));

            this.database.insertOrThrow(this.table, null, data);

            return true;
        } catch (Exception e) {
            this.logError("createSQLite", e);

            return false;
        }
    }

    @Override
    public boolean updateSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{entity.getString(ID)};

            data.put(NAME, entity.getString(NAME));
            data.put(IMAGE, entity.getString(IMAGE));
            data.put(UPDATE, new DateTime().toString(DEFAULT_FORMAT));

            return this.database.update(this.table, data, whereClause, whereArgs) != 0;
        } catch (Exception e) {
            this.logError("updateSQLite", e);

            return false;
        }
    }
}
