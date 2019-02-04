package com.imie.a2dev.teamculte.readeo.DBManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.imie.a2dev.teamculte.readeo.APIManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Author;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema.TABLE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema.NAME;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.CommonDBSchema.UPDATE;

/**
 * Manager class used to manage the author entities from databases.
 */
public final class AuthorDBManager extends DBManager {
    /**
     * Stores the base of the authors API url.
     */
    private final String baseUrl = APIManager.API_URL + APIManager.AUTHORS;

    /**
     * AuthorDBManager's constructor.
     * @param context The associated context.
     */
    public AuthorDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull Author entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(NAME, entity.getName());
            DBManager.database.insertOrThrow(this.table, null, data);

            return true;
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return false;
        }
    }

    /**
     * From a java entity updates the associated entity into the database.
     * @param entity The model to update into the database.
     * @return true if success else false.
     */
    public boolean updateSQLite(@NonNull Author entity) {
        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(NAME, entity.getName());
            data.put(UPDATE, new Date().toString());

            return DBManager.database.update(this.table, data, whereClause, whereArgs) != 0;
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
    public Author loadSQLite(int id) {
        try {
            String[] selectArgs = {String.valueOf(id)};
            String query = String.format(SIMPLE_QUERY_ALL, this.table, ID);
            Cursor result = DBManager.database.rawQuery(query, selectArgs);

            return new Author(result);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());

            return null;
        }
    }

    /**
     * Queries all the authors from the database.
     * @return The list of authors.
     */
    public List<Author> queryAllSQLite() {
        List<Author> authors = new ArrayList<>();

        try {
            Cursor result = DBManager.database.rawQuery(String.format(QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    authors.add(new Author(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }

        return authors;
    }

    /**
     * Queries all the authors paginated from the database.
     * @param start The start index (LIMIT).
     * @param end The end index (OFFSET).
     * @return The list of authors.
     */
    public List<Author> queryAllPaginatedSQLite(int start, int end) {
        List<Author> authors = new ArrayList<>();

        try {
            Cursor result = DBManager.database.rawQuery(String.format(QUERY_ALL_PAGINATED, this.table, start, end), null);

            if (result.getCount() > 0) {
                do {
                    authors.add(new Author(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }

        return authors;
    }

    /**
     * From the API, query the list of all authors from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(baseUrl + APIManager.READ);
    }

    /**
     * Gets the list of MySQL ids and last update fields in order to check which entities needs to be updated.
     */
    public void getUpdateFromMySQL() {
        final String[][] mysqlUpdateFIelds = this.getUpdateFieldsSQLite();
        this.requestJsonArray(Request.Method.POST, baseUrl + APIManager.READ_UPDATE, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //TODO : Create a double array and compare both id/last_update fields.
            }
        });
    }

    @Override
    protected void createSQLite(@NonNull JSONObject entity) {
        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(NAME, entity.getString(NAME));
            DBManager.database.insertOrThrow(this.table, null, data);
        } catch (SQLiteException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(SQLITE_TAG, e.getMessage());
        }
    }
}
