package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Request;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.City;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager class used to manage the city entities from databases.
 */
public final class CityDBManager extends DBManager {
    /**
     * Defines the city's table name.
     */
    public static final String TABLE =  "City";

    /**
     * Defines the city's id field.
     */
    public static final String ID =  "id_city";

    /**
     * Defines the city's name field.
     */
    public static final String NAME =  "name_city";

    /**
     * Stores the base of the categories API url.
     */
    private final String baseUrl = APIManager.API_URL + APIManager.CITIES;

    /**
     * CategoryDBManager's constructor.
     * @param context The associated context.
     */
    public CityDBManager(Context context) {
        super(context);
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull City entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(NAME, entity.getName());
            DBManager.database.insertOrThrow(TABLE, null, data);

            return true;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Queries the value of a specific field from a specific id.
     * @param field The field to access.
     * @param id The id of the db entity to access.
     * @return The value of the field.
     */
    public String getFieldSQLite(String field, int id) {
        return this.getFieldSQLite(field, TABLE, ID, id);
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean updateSQLite(@NonNull City entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(NAME, entity.getName());

            return DBManager.database.update(TABLE, data, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From an id, returns the associated java entity.
     * @param id The id of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public City loadSQLite(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(SIMPLE_QUERY_ALL, TABLE, ID);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

            return new City(result);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * From an id given in parameter, deletes the associated entity in the database.
     * @param id The id of the entity to delete.
     * @return true if success else false.
     */
    public boolean deleteSQLite(int id) {
        try {
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(id)};

            return DBManager.database.delete(TABLE, whereClause, whereArgs) != 0;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * Queries all the city from the database.
     * @return The list of city.
     */
    public List<City> queryAllSQLite() {
        List<City> cities = new ArrayList<>();

        try {
            Cursor result = DBManager.database.rawQuery(String.format(QUERY_ALL, TABLE), null);

            if (result.getCount() > 0) {
                do {
                    cities.add(new City(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }

        return cities;
    }

    /**
     * From the API, query the list of all categories from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importAllFromMySQL() {
        super.importAllFromMySQL(baseUrl + APIManager.READ);
    }

    /**
     * Creates a city entity in MySQL database.
     * @param name The name of the city to create.
     */
    public void createMySQL(String name) {
        String url = String.format(baseUrl + APIManager.CREATE + NAME + "=%s", name);

        super.requestString(Request.Method.POST, url, null);
    }

    @Override
    protected void createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(NAME, entity.getString(NAME));
            DBManager.database.insertOrThrow(TABLE, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }
}
