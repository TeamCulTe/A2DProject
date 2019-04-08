package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookListDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CityDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CountryDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.DBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.ProfileDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.ReviewDBManager;
import com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Final class representing a private user of the application (the one connected with all its attribute).
 */
@Getter
@Setter
public final class PrivateUser extends PublicUser {
    /**
     * Stores the user's hashed password.
     */
    private String password;

    /**
     * Stores the user's email address.
     */
    private String email;

    /**
     * Stores the user's country.
     */
    private Country country;

    /**
     * Stores the user's city.
     */
    private City city;

    /**
     * Stores the user's book lists.
     */
    private Map<String, BookList> bookLists;

    /**
     * Contains all the user's reviews.
     */
    private List<Review> reviews;

    /**
     * User's default constructor.
     */
    public PrivateUser() {
        super();

        this.bookLists = new HashMap<>();
        this.reviews = new ArrayList<>();
    }

    /**
     * User's nearly full filled constructor, providing all attributes values, except for database related ones.
     * @param pseudo The pseudo to set.
     * @param password The password to set.
     * @param email The email to set.
     * @param profile The profile to set.
     * @param country The country to set.
     * @param city the city to set.
     * @param bookLists The associated bookLists to set.
     * @param reviews The list of reviews to set.
     */
    public PrivateUser(String pseudo,
                       String password,
                       String email,
                       Profile profile,
                       Country country,
                       City city,
                       Map<String, BookList> bookLists,
                       List<Review> reviews) {
        super(pseudo, profile);

        this.password = password;
        this.email = email;
        this.country = country;
        this.city = city;
        this.bookLists = bookLists;
        this.reviews = reviews;
    }

    /**
     * User's full filled constructor, providing all attributes values.
     * @param id The id to set.
     * @param pseudo The pseudo to set.
     * @param password The password to set.
     * @param email The email to set.
     * @param profile The profile to set.
     * @param country The country to set.
     * @param city the city to set.
     * @param bookLists The associated bookLists to set.
     * @param reviews The list of reviews to set.
     */
    public PrivateUser(int id,
                       String pseudo,
                       String password,
                       String email,
                       Profile profile,
                       Country country,
                       City city,
                       Map<String, BookList> bookLists,
                       List<Review> reviews) {
        super(id, pseudo, profile);

        this.password = password;
        this.email = email;
        this.country = country;
        this.city = city;
        this.bookLists = bookLists;
        this.reviews = reviews;
    }

    /**
     * Private user's full filled constructor providing all its attributes values from a json object.
     * @param result The json object.
     */
    public PrivateUser(JSONObject result) {
        this.init(result);
    }

    /**
     * Initializes the user from a JSON response object (except for the relation objects ones).
     * @param object The JSON response from the API.
     */
    public void init(@NonNull JSONObject object) {
        try {
            this.id = object.getInt(UserDBSchema.ID);
            this.pseudo = object.getString(UserDBSchema.PSEUDO);
            this.password = object.getString(UserDBSchema.PASSWORD);
            this.email = object.getString(UserDBSchema.EMAIL);
        } catch (JSONException e) {
            Log.e(DBManager.JSON_TAG, e.getMessage());
        }
    }
}
