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
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Country;
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

import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.DEFAULT_FORMAT;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CountryDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CountryDBSchema.NAME;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CountryDBSchema.TABLE;

/**
 * Manager class used to manage the country entities from databases.
 */
public final class CountryDBManager extends SimpleDBManager {
    /**
     * CountryDBManager's constructor.
     * @param context The associated context.
     */
    public CountryDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
        this.baseUrl = APIManager.API_URL + APIManager.COUNTRIES;
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull Country entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(NAME, entity.getName());

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
    public boolean updateSQLite(@NonNull Country entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(NAME, entity.getName());
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
    public Country loadSQLite(int id) {
        Cursor result = this.loadCursorSQLite(id);

        if (result == null || result.getCount() == 0) {
            return null;
        }

        return new Country(result);
    }

    /**
     * Queries all the country from the database.
     * @return The list of country.
     */
    public List<Country> queryAllSQLite() {
        List<Country> countries = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(this.QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    countries.add(new Country(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("queryAllSQLite", e);
        }

        return countries;
    }

    /**
     * From the API, query the list of all categories from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(baseUrl + APIManager.READ);
    }

    /**
     * Creates a country entity in MySQL database.
     * @param country The country to create.
     */
    public void createMySQL(final Country country) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(country.getId()));
        param.put(NAME, country.getName());

        StringRequest request = new StringRequest(Request.Method.POST, url, null, new OnRequestError()) {
            @Override
            protected Map<String, String> getParams() {
                return param;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(CountryDBManager.this.getContext())
                                         .finishRequest(CountryDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String resp = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Pattern pattern = Pattern.compile("^\\d.$");

                    if (pattern.matcher(resp).find()) {
                        country.setId(Integer.valueOf(resp));
                    }
                } catch (IOException e) {
                    CountryDBManager.this.logError("createMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(CountryDBManager.this.getContext())
                                             .finishRequest(CountryDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();
    }

    /**
     * Loads a country from MySQL database.
     * @param idCountry The id of the country.
     * @return The loaded country.
     */
    public Country loadMySQL(int idCountry) {
        String url = this.baseUrl + APIManager.READ + ID + "=" + idCountry;
        return this.loadFromUrlMySQL(url);
    }

    /**
     * Loads a country from MySQL database.
     * @param countryName The name of the country.
     * @return The loaded country.
     */
    public Country loadMySQL(String countryName) {
        String url = this.baseUrl + APIManager.READ + NAME + "=" + countryName;
        return this.loadFromUrlMySQL(url);
    }

    @Override
    public boolean createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(NAME, entity.getString(NAME));

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
            data.put(UPDATE, new DateTime().toString(DEFAULT_FORMAT));

            return this.database.update(this.table, data, whereClause, whereArgs) != 0;
        } catch (Exception e) {
            this.logError("updateSQLite", e);

            return false;
        }
    }

    /**
     * Loads a country from MySQL database.
     * @param url The url used to get the entity.
     * @return The loaded country.
     */
    private Country loadFromUrlMySQL(String url) {
        final Country country = new Country();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, null, new OnRequestError()) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(response.data,
                                                                   HttpHeaderParser.parseCharset(response.headers)));
                    JSONObject object = jsonArray.getJSONObject(0);

                    country.init(object);
                } catch (Exception e) {
                    CountryDBManager.this.logError("loadFromUrlMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(CountryDBManager.this.getContext())
                                             .finishRequest(CountryDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(CountryDBManager.this.getContext())
                                         .finishRequest(CountryDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(this.table, request);
        this.waitForResponse();

        return (country.isEmpty()) ? null : country;
    }
}
