package com.imie.a2dev.teamculte.readeo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.BookList;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.PrivateUser;

/**
 * Utils class used to manage SharedPreferences.
 */
public abstract class PreferencesUtils {
    /**
     * Stores the app preferences label.
     */
    public static final String PREFS_LABEL = "Readeo_preferences";

    /**
     * Stores the default email preference tag.
     */
    public static final String USER_PREF = "Readeo_user";

    /**
     * Gets an instance of SharedPreferences.
     * @return The shared preferences
     */
    public static SharedPreferences getSharedPrefs() {
        return App.getAppContext().getSharedPreferences(PREFS_LABEL, Context.MODE_PRIVATE);
    }

    /**
     * Gets an instance of SharedPreferences editor.
     * @return The editor.
     */
    public static SharedPreferences.Editor getSharedPrefsEditor() {
        return PreferencesUtils.getSharedPrefs().edit();
    }

    /**
     * Save a user into the shared preferences.
     * @param user The user to save.
     */
    public static void saveUser(PrivateUser user) {
        SharedPreferences.Editor editor = getSharedPrefsEditor();

        editor.putString(USER_PREF, new Gson().toJson(user));
        editor.commit();
    }

    /**
     * Gets the user from the preferences.
     * @return The associated user.
     */
    public static PrivateUser loadUser() {
        SharedPreferences preferences = getSharedPrefs();
        String jsonUser = preferences.getString(USER_PREF, null);
        PrivateUser user = null;

        if (jsonUser != null) {
            user = new Gson().fromJson(jsonUser, PrivateUser.class);
        }

        return user;
    }

    /**
     * Updates a book list from the user saved in the preferences.
     * @param bookList The book list to set to the user to update.
     */
    public static void updateUserBookList(BookList bookList) {
        PrivateUser user = loadUser();

        for (String type : user.getBookLists().keySet()) {
            if (bookList.getType().getName().equals(type)) {
                user.getBookLists().put(type, bookList);

                break;
            }
        }

        saveUser(user);
    }
}
