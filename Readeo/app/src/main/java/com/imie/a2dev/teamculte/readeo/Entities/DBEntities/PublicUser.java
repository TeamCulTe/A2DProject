package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

/**
 * Final class representing a public user of the application (without password for security reasons, book lists and
 * reviews).
 */
public class PublicUser extends DBEntity {
    /**
     * Stores the user's pseudo.
     */
    private String pseudo;

    /**
     * Stores the user's email.
     */
    private String email;

    /**
     * Stores the user's profile.
     */
    private Profile profile;

    /**
     * Stores the user's country of living.
     */
    private String country;

    /**
     * Stores the city in which the user lives.
     */
    private String city;

    /**
     * User's default constructor.
     */
    public PublicUser() {
        super();
    }

    /**
     * User's nearly full filled constructor, providing all attributes values, except for database related ones.
     * @param pseudo The pseudo to set.
     * @param email The email to set.
     * @param profile The profile to set.
     * @param country The country name to set.
     * @param city the city name to set.
     */
    public PublicUser(String pseudo, String email, Profile profile, String country, String city) {
        super();

        this.pseudo = pseudo;
        this.email = email;
        this.profile = profile;
        this.country = country;
        this.city = city;
    }

    /**
     * User's full filled constructor, providing all attributes values.
     * @param id The id to set.
     * @param pseudo The pseudo to set.
     * @param email The email to set.
     * @param profile The profile to set.
     * @param country The country name to set.
     * @param city the city name to set.
     */
    public PublicUser(int id, String pseudo, String email, Profile profile, String country, String city) {
        super(id);

        this.pseudo = pseudo;
        this.email = email;
        this.profile = profile;
        this.country = country;
        this.city = city;
    }

    /**
     * Gets the pseudo attribute.
     * @return The String value of pseudo attribute.
     */
    public String getPseudo() {
        return this.pseudo;
    }

    /**
     * Sets the pseudo attribute.
     * @param newPseudo The new String value to set.
     */
    public void setPseudo(String newPseudo) {
        this.pseudo = newPseudo;
    }

    /**
     * Gets the emai attribute.
     * @return The String value of emai attribute.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the email attribute.
     * @param newEmail The new String value to set.
     */
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    /**
     * Gets the profile attribute.
     * @return The Profile value of profile attribute.
     */
    public Profile getProfile() {
        return this.profile;
    }

    /**
     * Sets the profile attribute.
     * @param newProfile The new Profile value to set.
     */
    public void setProfile(Profile newProfile) {
        this.profile = newProfile;
    }

    /**
     * Gets the country attribute.
     * @return The String value of country attribute.
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * Sets the country attribute.
     * @param newCountry The new String value to set.
     */
    public void setCountry(String newCountry) {
        this.country = newCountry;
    }

    /**
     * Gets the city attribute.
     * @return The String value of city attribute.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Sets the city attribute.
     * @param newCity The new String value to set.
     */
    public void setCity(String newCity) {
        this.city = newCity;
    }
}
