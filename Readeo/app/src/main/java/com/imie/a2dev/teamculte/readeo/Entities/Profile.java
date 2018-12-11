package com.imie.a2dev.teamculte.readeo.Entities;

/**
 * Final class representing the profile of an user with its related information.
 */
public final class Profile extends DBEntity {
    /**
     * Stores the description written by the user for his profile.
     */
    private String description;

    /**
     * Stores the path of the user's avatar displayed on his profile.
     */
    private String avatar;

    /**
     * Stores the owner of the profile.
     */
    private User user;

    /**
     * Profile's default constructor.
     */
    public Profile() {

    }

    /**
     * Profile's nearly full filled constructor, providing all attributes values, except for database related ones.
     *
     * @param description The description to set.
     * @param avatar The path of the avatar to set.
     * @param user The user to set.
     */
    public Profile(String description, String avatar, User user) {
        this.description = description;
        this.avatar = avatar;
        this.user = user;
    }

    /**
     * Profile's full filled constructor, providing all attributes values.
     *
     * @param id The id to set.
     * @param description The description to set.
     * @param avatar The path of the avatar to set.
     * @param user The user to set.
     * @param deleted The boolean value of the deleted attribute to set.
     */
    public Profile(int id, String description, String avatar, User user, boolean deleted) {
        super(id, deleted);

        this.description = description;
        this.avatar = avatar;
        this.user = user;
    }

    /**
     * Gets the description attribute.
     *
     * @return The String value of description attribute.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description attribute.
     *
     * @param newDescription The new String value to set.
     */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * Gets the avatar attribute.
     *
     * @return The String value of avatar attribute.
     */
    public String getAvatar() {
        return this.avatar;
    }

    /**
     * Sets the avatar attribute.
     *
     * @param newAvatar The new String value to set.
     */
    public void setAvatar(String newAvatar) {
        this.avatar = newAvatar;
    }

    /**
     * Gets the user attribute.
     *
     * @return The User value of user attribute.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Sets the user attribute.
     *
     * @param newUser The new User value to set.
     */
    public void setUser(User newUser) {
        this.user = newUser;
    }
}
