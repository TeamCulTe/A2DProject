package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import com.imie.a2dev.teamculte.readeo.Entities.BookList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Final class representing a private user of the application (the one connected with all its attribute).
 */
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
     * Stores the user's country name.
     */
    private String country;

    /**
     * Stores the user's city name.
     */
    private String city;

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
     * @param country The country name to set.
     * @param city the city name to set.
     * @param bookLists The associated bookLists to set.
     * @param reviews The list of reviews to set.
     */
    public PrivateUser(String pseudo,
                       String password,
                       String email,
                       Profile profile,
                       String country,
                       String city,
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
     * @param country The country name to set.
     * @param city the city name to set.
     * @param bookLists The associated bookLists to set.
     * @param reviews The list of reviews to set.
     */
    public PrivateUser(int id,
                       String pseudo,
                       String password,
                       String email,
                       Profile profile,
                       String country,
                       String city,
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
     * Gets the password attribute.
     * @return The String value of password attribute.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Gets the email attribute.
     * @return The String value of email attribute.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets the country attribute.
     * @return The String value of country attribute.
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * Gets the city attribute.
     * @return The String value of city attribute.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Gets the bookLists attribute.
     * @return The Map<String, BookList> value of bookLists attribute.
     */
    public Map<String, BookList> getBookLists() {
        return this.bookLists;
    }

    /**
     * Gets the reviews attribute.
     * @return The List<Review> value of reviews attribute.
     */
    public List<Review> getReviews() {
        return this.reviews;
    }

    /**
     * Sets the password attribute.
     * @param newPassword The new String value to set.
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Sets the email attribute.
     * @param newEmail The new String value to set.
     */
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    /**
     * Sets the country attribute.
     * @param newCountry The new String value to set.
     */
    public void setCountry(String newCountry) {
        this.country = newCountry;
    }

    /**
     * Sets the city attribute.
     * @param newCity The new String value to set.
     */
    public void setCity(String newCity) {
        this.city = newCity;
    }

    /**
     * Sets the bookLists attribute.
     * @param newBookLists The new Map<String, BookList> value to set.
     */
    public void setBookLists(Map<String, BookList> newBookLists) {
        this.bookLists = newBookLists;
    }

    /**
     * Sets the reviews attribute.
     * @param newReviews The new List<Review> value to set.
     */
    public void setReviews(List<Review> newReviews) {
        this.reviews = newReviews;
    }
}
