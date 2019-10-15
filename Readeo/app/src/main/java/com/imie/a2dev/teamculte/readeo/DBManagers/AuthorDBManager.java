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
import com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Author;
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

import static com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema.TABLE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema.NAME;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.DEFAULT_FORMAT;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;

/**
 * Manager class used to manage the author entities from databases.
 */
public final class AuthorDBManager extends SimpleDBManager {
    /**
     * AuthorDBManager's constructor.
     * @param context The associated context.
     */
    public AuthorDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
        this.baseUrl = APIManager.API_URL + APIManager.AUTHORS;
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull Author entity) {
        this.database.beginTransaction();
        
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(NAME, entity.getName());
            
            this.database.insertOrThrow(this.table, null, data);
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
    public boolean updateSQLite(@NonNull Author entity) {
        this.database.beginTransaction();
        
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(NAME, entity.getName());
            data.put(UPDATE, new DateTime().toString(DEFAULT_FORMAT));
            
            boolean success = this.database.update(this.table, data, whereClause, whereArgs) != 0;

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
     * From an id, returns the associated java entity.
     * @param id The id of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public Author loadSQLite(int id) {
        Cursor result = this.loadCursorSQLite(id);

        if (result == null || result.getCount() == 0) {
            return null;
        }

        return new Author(result);
    }

    /**
     * Queries all the authors from the database.
     * @return The list of authors.
     */
    public List<Author> queryAllSQLite() {
        List<Author> authors = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(this.QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    authors.add(new Author(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("queryAllSQLite", e);
        }

        return authors;
    }

    /**
     * From the API, query the list of all authors from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(this.baseUrl + APIManager.READ);
    }

    /**
     * Creates a author entity in MySQL database.
     * @param author The author to create.
     */
    public void createMySQL(final Author author) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        if (author.getId() != 0) {
            param.put(ID, String.valueOf(author.getId()));
        }

        param.put(NAME, author.getName());

        StringRequest request = new StringRequest(Request.Method.POST, url, null, new OnRequestError()) {
            @Override
            protected Map<String, String> getParams() {
                return param;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(AuthorDBManager.this.getContext())
                                         .finishRequest(AuthorDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String resp = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Pattern pattern = Pattern.compile("^\\d.$");

                    if (pattern.matcher(resp).find()) {
                        author.setId(Integer.valueOf(resp));
                    }
                } catch (IOException e) {
                    AuthorDBManager.this.logError("createMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(AuthorDBManager.this.getContext())
                                             .finishRequest(AuthorDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();
    }

    /**
     * Loads an author from MySQL database.
     * @param idAuthor The id of the author.
     * @return The loaded author.
     */
    public Author loadMySQL(int idAuthor) {
        final Author author = new Author();
        String url = this.baseUrl + APIManager.READ + ID + "=" + idAuthor;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, null, new OnRequestError()) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(response.data,
                                                                   HttpHeaderParser.parseCharset(response.headers)));
                    JSONObject object = jsonArray.getJSONObject(0);

                    author.init(object);
                } catch (Exception e) {
                    AuthorDBManager.this.logError("loadMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(AuthorDBManager.this.getContext())
                                             .finishRequest(AuthorDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(AuthorDBManager.this.getContext())
                                         .finishRequest(AuthorDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();

        return (author.isEmpty()) ? null : author;
    }

    @Override
    public boolean createSQLite(@NonNull JSONObject entity) {
        this.database.beginTransaction();
        
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(NAME, entity.getString(NAME));
            
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
        this.database.beginTransaction();
        
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{entity.getString(ID)};

            data.put(NAME, entity.getString(NAME));
            data.put(UPDATE, new DateTime().toString(DEFAULT_FORMAT));
            
            boolean success = this.database.update(this.table, data, whereClause, whereArgs) != 0;
            
            this.database.endTransaction();

            return success;
        } catch (Exception e) {
            this.logError("updateSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
        }
    }
}
