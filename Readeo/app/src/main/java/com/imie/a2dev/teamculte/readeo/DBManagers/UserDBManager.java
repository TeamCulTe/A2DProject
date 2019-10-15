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
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookList;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookListType;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PublicUser;
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
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.CITY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.COUNTRY;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.EMAIL;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.ID;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PASSWORD;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PROFILE;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PSEUDO;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.TABLE;

/**
 * Manager class used to manage the user entities from databases.
 */
public final class UserDBManager extends SimpleDBManager {
    /**
     * UserDBManager's constructor.
     * @param context The associated context.
     */
    public UserDBManager(Context context) {
        super(context);

        this.table = TABLE;
        this.ids = new String[]{ID};
        this.baseUrl = APIManager.API_URL + APIManager.USERS;
    }

    /**
     * From a java entity creates the associated entity into the database.
     * @param entity The model to store into the database.
     * @return true if success else false.
     */
    public boolean createSQLite(@NonNull PublicUser entity) {
        this.database.beginTransaction();

        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getId());
            data.put(PSEUDO, entity.getPseudo());
            data.put(PROFILE, entity.getProfile().getId());

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
    public boolean updateSQLite(@NonNull PublicUser entity) {
        this.database.beginTransaction();

        try {
            ContentValues data = new ContentValues();
            String whereClause = String.format("%s = ?", ID);
            String[] whereArgs = new String[]{String.valueOf(entity.getId())};

            data.put(ID, entity.getId());
            data.put(PSEUDO, entity.getPseudo());
            data.put(PROFILE, entity.getProfile().getId());
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
     * Queries the value of a specific field from a specific pseudo.
     * @param field The field to access.
     * @param pseudo The pseudo of the db entity to access.
     * @return The value of the field.
     */
    public String getFieldFromPseudoSQLite(String field, String pseudo) {
        return this.getFieldSQLite(field, PSEUDO, pseudo);
    }

    /**
     * From a user pseudo, gets its associated id.
     * @param pseudo The pseudo of the user.
     * @return The id associated to the pseudo.
     */
    public int SQLiteGetId(String pseudo) {
        return Integer.valueOf(this.getFieldFromPseudoSQLite(ID, pseudo));
    }

    /**
     * From an id, returns the associated java entity.
     * @param id The id of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public PublicUser loadSQLite(int id) {
        Cursor result = this.loadCursorSQLite(id);

        if (result == null || result.getCount() == 0) {
            return null;
        }

        return new PublicUser(result);
    }

    /**
     * From a pseudo, returns the associated java entity.
     * @param pseudo The pseudo of entity to load from the database.
     * @return The loaded entity if exists else null.
     */
    public PublicUser loadSQLite(String pseudo) {
        try {
            String[] selectArgs = {pseudo};
            String query = String.format(this.SIMPLE_QUERY_ALL, this.table, PSEUDO);
            Cursor result = this.database.rawQuery(query, selectArgs);

            return (result.getCount() > 0) ? new PublicUser(result) : null;
        } catch (SQLiteException e) {
            this.logError("loadSQLite", e);

            return null;
        }
    }

    /**
     * From a string and a field, returns the associated java entities where the string matches in the field values.
     * @param field The field to filter on.
     * @param filter The string contained in the pseudo of the entity to load from the database.
     * @return The loaded entities if exists else null.
     */
    public List<PublicUser> loadFilteredSQLite(String field, String filter) {
        try {
            List<PublicUser> users = new ArrayList<>();
            String[] selectArgs = {filter};
            String query = String.format(this.SIMPLE_QUERY_ALL_LIKE_START, this.table, field);
            Cursor result = this.database.rawQuery(query, selectArgs);

            while (result.moveToNext()) {
                users.add(new PublicUser(result, false));
            }

            return users;
        } catch (SQLiteException e) {
            this.logError("loadFilteredSQLite", e);

            return null;
        }
    }

    /**
     * Query all the users from the database.
     * @return The list of users.
     */
    public List<PublicUser> queryAllSQLite() {
        List<PublicUser> users = new ArrayList<>();

        try {
            Cursor result = this.database.rawQuery(String.format(this.QUERY_ALL, this.table), null);

            if (result.getCount() > 0) {
                do {
                    users.add(new PublicUser(result, false));
                } while (result.moveToNext());
            }

            result.close();
        } catch (SQLiteException e) {
            this.logError("queryAllSQLite", e);
        }

        return users;
    }

    /**
     * From the API, query the list of all public users from the MySQL database in order to stores it into the SQLite
     * database.
     */
    public void importFromMySQL() {
        super.importFromMySQL(this.baseUrl + APIManager.READ + "?public=true");
    }

    /**
     * Creates a user entity in MySQL database.
     * @param user The user to create.
     */
    public void createMySQL(final PrivateUser user) {
        String url = this.baseUrl + APIManager.CREATE;
        Map<String, String> param = new HashMap<>();

        if (user.getId() != 0) {
            param.put(ID, String.valueOf(user.getId()));
        }

        param.put(PSEUDO, user.getPseudo());
        param.put(PASSWORD, user.getPassword());
        param.put(EMAIL, user.getEmail());
        param.put(PROFILE, String.valueOf(user.getProfile().getId()));
        param.put(CITY, String.valueOf(user.getCity().getId()));
        param.put(COUNTRY, String.valueOf(user.getCountry().getId()));

        StringRequest request = new StringRequest(Request.Method.POST, url, null,
                                                  new OnRequestError()) {
            @Override
            protected Map<String, String> getParams() {
                return param;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(UserDBManager.this.getContext())
                                         .finishRequest(UserDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String resp = new String(response.data,
                                             HttpHeaderParser.parseCharset(response.headers));
                    Pattern pattern = Pattern.compile("^\\d.$");

                    if (pattern.matcher(resp).find()) {
                        user.setId(Integer.valueOf(resp));
                    }
                } catch (IOException e) {
                    UserDBManager.this.logError("createMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(UserDBManager.this.getContext())
                                             .finishRequest(UserDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext()).addToRequestQueue(TABLE, request);

        this.waitForResponse();
    }

    /**
     * Updates a user entity in MySQL database.
     * @param user The user to update.
     */
    public void updateMySQL(PrivateUser user) {
        String url = this.baseUrl + APIManager.UPDATE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(user.getId()));
        param.put(PSEUDO, user.getPseudo());
        param.put(PASSWORD, user.getPassword());
        param.put(EMAIL, user.getEmail());
        param.put(PROFILE, String.valueOf(user.getProfile().getId()));
        param.put(CITY, String.valueOf(user.getCity().getId()));
        param.put(COUNTRY, String.valueOf(user.getCountry().getId()));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Updates a user fields given in parameter in MySQL database.
     * @param id The id of user to update.
     * @param field The field of the user to update.
     * @param value The the new value to set.
     */
    public void updateFieldMySQL(int id, String field, String value) {
        String url = this.baseUrl + APIManager.UPDATE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(id));
        param.put(field, value);

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Loads a user from MySQL database.
     * @param idUser The id of the user.
     * @return The loaded user.
     */
    public PrivateUser loadMySQL(int idUser) {
        final PrivateUser user = new PrivateUser();
        final int idProfile[] = new int[1];
        final int idCity[] = new int[1];
        final int idCountry[] = new int[1];
        String url = this.baseUrl + APIManager.READ + ID + "=" + idUser;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, null,
                                                        new OnRequestError()) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(response.data,
                                                                   HttpHeaderParser.parseCharset(
                                                                           response.headers)));
                    JSONObject object = jsonArray.getJSONObject(0);

                    idProfile[0] = object.getInt(PROFILE);
                    idCity[0] = object.getInt(CITY);
                    idCountry[0] = object.getInt(COUNTRY);

                    user.init(object);
                } catch (Exception e) {
                    UserDBManager.this.logError("loadMySQL", e);
                } finally {
                    HTTPRequestQueueSingleton.getInstance(UserDBManager.this.getContext())
                                             .finishRequest(UserDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                idProfile[0] = 0;
                idCity[0] = 0;
                idCountry[0] = 0;

                HTTPRequestQueueSingleton.getInstance(UserDBManager.this.getContext())
                                         .finishRequest(UserDBManager.this.table);

                return super.parseNetworkError(volleyError);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext())
                                 .addToRequestQueue(this.table, request);
        this.waitForResponse();

        user.setProfile(new ProfileDBManager(this.getContext()).loadMySQL(idProfile[0]));
        user.setCountry(new CountryDBManager(this.getContext()).loadMySQL(idCountry[0]));
        user.setCity(new CityDBManager(this.getContext()).loadMySQL(idCity[0]));
        user.setReviews(new ReviewDBManager(this.getContext()).loadUserMySQL(user.getId()));
        user.setBookLists(new BookListDBManager(this.getContext()).loadUserMySQL(user.getId()));

        return (user.isEmpty()) ? null : user;
    }

    /**
     * Loads a user from MySQL database.
     * @param email The email of the user.
     * @param password The password of the user.
     * @param listener The listener to call if defined.
     * @return The loaded user.
     */
    public PrivateUser loadMySQL(String email, String password,
                                 HTTPRequestQueueSingleton.HTTPRequestQueueListener listener) {
        final PrivateUser user = new PrivateUser();
        String url = this.baseUrl + APIManager.READ + EMAIL + "=" + email + "&" + PASSWORD + "=" +
                     password;
        final int idProfile[] = new int[1];
        final int idCity[] = new int[1];
        final int idCountry[] = new int[1];
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, null,
                                                        new OnRequestError()) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(response.data,
                                                                   HttpHeaderParser.parseCharset(
                                                                           response.headers)));
                    JSONObject object = jsonArray.getJSONObject(0);
                    idProfile[0] = object.getInt(PROFILE);
                    idCity[0] = object.getInt(CITY);
                    idCountry[0] = object.getInt(COUNTRY);

                    user.init(object);
                    
                    if (listener != null) {
                        listener.onRequestFinished();
                    }
                } catch (Exception e) {
                    UserDBManager.this.logError("loadMySQL", e);
                    
                    if (listener != null) {
                        listener.onRequestError();
                    }
                } finally {
                    HTTPRequestQueueSingleton.getInstance(UserDBManager.this.getContext())
                                             .finishRequest(UserDBManager.this.table);
                }

                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                idProfile[0] = 0;
                idCity[0] = 0;
                idCountry[0] = 0;

                HTTPRequestQueueSingleton.getInstance(UserDBManager.this.getContext())
                                         .finishRequest(UserDBManager.this.table);

                if (listener != null) {
                    listener.onRequestError();
                }

                return super.parseNetworkError(volleyError);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext())
                                 .addToRequestQueue(this.table, request);
        
        if (listener == null) {
            this.waitForResponse();
        }
        
        user.setProfile(new ProfileDBManager(this.getContext()).loadSQLite(idProfile[0]));
        user.setCountry(new CountryDBManager(this.getContext()).loadSQLite(idCountry[0]));
        user.setCity(new CityDBManager(this.getContext()).loadSQLite(idCity[0]));
        user.setReviews(new ReviewDBManager(this.getContext()).loadUserSQLite(user.getId()));
        user.setBookLists(new BookListDBManager(this.getContext()).loadUserSQLite(user.getId()));

        this.initBookLists(user);

        return (user.isEmpty()) ? null : user;
    }

    /**
     * Checks if a value is available for a specific field (not already taken).
     * @param field The associated field.
     * @param value The value to check.
     * @param listener The listener to call.
     * @return True if the value is available (not taken) else false.
     */
    public boolean isAvailableMySQL(String field, String value,
                                    HTTPRequestQueueSingleton.HTTPRequestQueueListener listener) {
        final boolean[] available = new boolean[1];
        String url = this.baseUrl + APIManager.READ + field + "=" + value + "&public=1";
        available[0] = true;
        StringRequest request = new StringRequest(Request.Method.GET, url, null,
                                                  new OnRequestError()) {
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                HTTPRequestQueueSingleton.getInstance(UserDBManager.this.getContext())
                                         .finishRequest(UserDBManager.this.table);
                
                if (listener != null) {
                    listener.onRequestError();
                }

                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.data.length > 2) {
                    available[0] = false;
                }

                HTTPRequestQueueSingleton.getInstance(UserDBManager.this.getContext())
                                         .finishRequest(UserDBManager.this.table);
                
                if (listener != null) {
                    listener.onRequestFinished();
                }

                return super.parseNetworkResponse(response);
            }
        };

        HTTPRequestQueueSingleton.getInstance(this.getContext())
                                 .addToRequestQueue(this.table, request);
        
        if (listener == null) {
            this.waitForResponse();
        }

        return available[0];
    }

    /**
     * Deletes a user entity in MySQL database.
     * @param email The email of the user to delete.
     * @param password The password of the user to delete.
     */
    public void deleteMySQL(String email, String password) {
        String url = this.baseUrl + APIManager.DELETE;
        Map<String, String> param = new HashMap<>();

        param.put(PASSWORD, password);
        param.put(EMAIL, email);

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Restores a user entity in MySQL database.
     * @param email The email of the user to restore.
     * @param password The password of the user to restore.
     */
    public void restoreMySQL(String email, String password) {
        String url = this.baseUrl + APIManager.RESTORE;
        Map<String, String> param = new HashMap<>();

        param.put(PASSWORD, password);
        param.put(EMAIL, email);

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Restores a user from MySQL database.
     * @param idUser The id of the user to restore.
     */
    public void restoreMySQL(int idUser) {
        String url = this.baseUrl + APIManager.RESTORE;
        Map<String, String> param = new HashMap<>();

        param.put(ID, String.valueOf(idUser));

        super.requestString(Request.Method.PUT, url, null, param);
    }

    /**
     * Deletes a user entity in MySQL database.
     * @param user The user to delete.
     */
    public void deleteMySQL(PrivateUser user) {
        this.deleteMySQL(user.getEmail(), user.getPassword());
    }

    @Override
    public boolean createSQLite(@NonNull JSONObject entity) {
        this.database.beginTransaction();

        try {
            ContentValues data = new ContentValues();

            data.put(ID, entity.getInt(ID));
            data.put(PSEUDO, entity.getString(PSEUDO));
            data.put(PROFILE, entity.getInt(PROFILE));

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

            data.put(PSEUDO, entity.getString(PSEUDO));
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

    /**
     * Initializes the user book lists if empty.
     */
    private void initBookLists(PrivateUser user) {
        List<BookListType> types = new BookListTypeDBManager(this.getContext()).queryAllSQLite();
        Map<String, BookList> bookLists = new HashMap<>();
        BookList bookList;

        if (user.getBookLists() == null) {
            user.setBookLists(bookLists);
        }

        for (BookListType type : types) {
            if (!user.getBookLists().containsKey(type.getName())) {
                bookList = new BookList(type);

                bookList.setId(user.getId());
                user.getBookLists().put(type.getName(), bookList);
            }
        }
    }
}
