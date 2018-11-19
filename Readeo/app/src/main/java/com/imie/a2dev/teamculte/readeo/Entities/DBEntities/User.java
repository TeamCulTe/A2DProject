package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Final class representing an user of the application.
 */
public final class User extends DBEntity {
    /**
     * Stores the user's pseudo.
     */
    private String pseudo;

    /**
     * Stores the user's hashed password.
     */
    private String password;

    /**
     * Stores the user's email.
     */
    private String email;

    /**
     * Stores the user's country of living.
     */
    private String country;

    /**
     * Stores the city in which the user lives.
     */
    private String city;

    /**
     * Contains the user's associated profile.
     */
    private Profile profile;

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
    public User() {
        this.bookLists = new HashMap<>();
        this.reviews = new ArrayList<>();
    }

    /**
     * User's nearly full filled constructor, providing all attributes values, except for database related ones.
     *
     * @param pseudo The pseudo to set.
     * @param password The password to set.
     * @param email The email to set.
     * @param country The country name to set.
     * @param city the city name to set.
     * @param profile The associated profile to set.
     * @param bookLists The associated bookLists to set.
     * @param reviews The list of reviews to set.
     */
    public User(String pseudo, String password, String email, String country, String city, Profile profile,
                Map<String, BookList> bookLists, List<Review> reviews) {
        this.pseudo = pseudo;
        this.password = password;
        this.email = email;
        this.country = country;
        this.city = city;
        this.profile = profile;
        this.bookLists = bookLists;
        this.reviews = reviews;
    }

    /**
     * User's full filled constructor, providing all attributes values.
     *
     * @param id The id to set.
     * @param pseudo The pseudo to set.
     * @param password The password to set.
     * @param email The email to set.
     * @param country The country name to set.
     * @param city the city name to set.
     * @param profile The associated profile to set.
     * @param bookLists The associated bookLists to set.
     * @param reviews The list of reviews to set.
     * @param deleted The boolean value to set.
     */
    public User(int id, String pseudo, String password, String email, String country, String city, Profile profile,
                Map<String, BookList> bookLists, List<Review> reviews, boolean deleted) {
        super(id, deleted);

        this.pseudo = pseudo;
        this.password = password;
        this.email = email;
        this.country = country;
        this.city = city;
        this.profile = profile;
        this.bookLists = bookLists;
        this.reviews = reviews;
    }

    /**
     * Gets the pseudo attribute.
     *
     * @return The String value of pseudo attribute.
     */
    public String getPseudo() {
        return this.pseudo;
    }

    /**
     * Sets the pseudo attribute.
     *
     * @param newPseudo The new String value to set.
     */
    public void setPseudo(String newPseudo) {
        this.pseudo = newPseudo;
    }

    /**
     * Gets the password attribute.
     *
     * @return The String value of password attribute.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the password attribute.
     *
     * @param newPassword The new String value to set.
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Gets the emai attribute.
     *
     * @return The String value of emai attribute.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the email attribute.
     *
     * @param newEmail The new String value to set.
     */
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    /**
     * Gets the country attribute.
     *
     * @return The String value of country attribute.
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * Sets the country attribute.
     *
     * @param newCountry The new String value to set.
     */
    public void setCountry(String newCountry) {
        this.country = newCountry;
    }

    /**
     * Gets the city attribute.
     *
     * @return The String value of city attribute.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Sets the city attribute.
     *
     * @param newCity The new String value to set.
     */
    public void setCity(String newCity) {
        this.city = newCity;
    }

    /**
     * Gets the profile attribute.
     *
     * @return The Profile value of profile attribute.
     */
    public Profile getProfile() {
        return this.profile;
    }

    /**
     * Sets the profile attribute.
     *
     * @param newProfile The new Profile value to set.
     */
    public void setProfile(Profile newProfile) {
        this.profile = newProfile;
    }

    /**
     * Gets the bookLists attribute.
     *
     * @return The Map<String, BookList> value of bookLists attribute.
     */
    public Map<String, BookList> getBookLists() {
        return this.bookLists;
    }

    /**
     * Sets the bookLists attribute.
     *
     * @param newBookLists The new Map<String, BookList> value to set.
     */
    public void setBookLists(Map<String, BookList> newBookLists) {
        this.bookLists = newBookLists;
    }

    /**
     * Gets the reviews attribute.
     *
     * @return The List<Review> value of reviews attribute.
     */
    public List<Review> getReviews() {
        return this.reviews;
    }

    /**
     * Sets the reviews attribute.
     *
     * @param newReviews The new List<Review> value to set.
     */
    public void setReviews(List<Review> newReviews) {
        this.reviews = newReviews;
    }
}
