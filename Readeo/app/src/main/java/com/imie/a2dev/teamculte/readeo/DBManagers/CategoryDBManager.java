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
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Category;
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

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema.NAME;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema.TABLE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.DEFAULT_FORMAT;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;

/**
 * Manager class used to manage the category entities from databases.
 */
public final class CategoryDBManager extends SimpleDBManager {
    /**
     * CategoryDBManager's constructor.
     * @param context The associated context.
     */
    public CategoryDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
        this.baseUrl = APIManager.API_URL + APIManager.CATEGORIES;
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull Category entity) {
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
    public boolean updateSQLite(@NonNull Category entity) {
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
    public Category loadSQLite(int id) {
        Cursor result = this.loadCursorSQLite(id);

        if (result == null || result.getCount() == 0) {
            return null;
        }

        return new Category(result);
    }

    /**
     * Queries all the categories from the database.
     * @return The list of categories.
     */
    public List<Category> queryAllSQLite() {
        List<Category> categories = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(this.QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    categories.add(new Category(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("queryAllSQLite", e);
        }

        return categories;
    }

    /**
     * From the API, query the list of all categories from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(baseUrl + APIManager.READ);
    }

    /**
     * Creates a category entity in MySQL database.
     * @param category The category to create.
     */
    public void createMySQL(final Category category) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        if (category.getId() != 0) {
            param.put(ID, String.valueOf(category.getId()));
        }

        param.put(NAME, category.getName());

        StringRequest request = new StringRequest(Request.Method.POST, url, null, new OnRequestError()) {
            @Override
            protected Map<String, String> getParams() {
                return param;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(CategoryDBManager.this.getContext())
                                         .finishRequest(CategoryDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String resp = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Pattern pattern = Pattern.compile("^\\d.$");

                    if (pattern.matcher(resp).find()) {
                        category.setId(Integer.valueOf(resp));
                    }
                } catch (IOException e) {
                    CategoryDBManager.this.logError("createMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(CategoryDBManager.this.getContext())
                                             .finishRequest(CategoryDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();
    }

    /**
     * Loads a category from MySQL database.
     * @param idCategory The id of the category.
     * @return The loaded category.
     */
    public Category loadMySQL(int idCategory) {
        final Category category = new Category();
        String url = this.baseUrl + APIManager.READ + ID + "=" + idCategory;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, null, new OnRequestError()) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(response.data,
                                                                   HttpHeaderParser.parseCharset(response.headers)));
                    JSONObject object = jsonArray.getJSONObject(0);

                    category.init(object);
                } catch (Exception e) {
                    CategoryDBManager.this.logError("loadMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(CategoryDBManager.this.getContext())
                                             .finishRequest(CategoryDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(CategoryDBManager.this.getContext())
                                         .finishRequest(CategoryDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();

        return (category.isEmpty()) ? null : category;
    }

    @Override
    public boolean createSQLite(@NonNull JSONObject entity) {
        this.database.beginTransaction();
        
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(NAME, entity.getString(NAME));
            data.put(UPDATE, entity.getString(UPDATE));
            
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
            
            this.database.setTransactionSuccessful();

            return success;
        } catch (Exception e) {
            this.logError("updateSQLite", e);

            return false;
        } finally {
            this.database.endTransaction();
        }
    }
}
