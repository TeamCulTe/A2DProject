package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

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
     * Profile's default constructor.
     */
    public Profile() {
        super();
    }

    /**
     * Profile's nearly full filled constructor, providing all attributes values, except for database related ones.
     * @param description The description to set.
     * @param avatar The path of the avatar to set.
     */
    public Profile(String description, String avatar) {
        super();

        this.description = description;
        this.avatar = avatar;
    }

    /**
     * Profile's full filled constructor, providing all attributes values.
     * @param id The id to set.
     * @param description The description to set.
     * @param avatar The path of the avatar to set.
     */
    public Profile(int id, String description, String avatar) {
        super(id);

        this.description = description;
        this.avatar = avatar;
    }

    /**
     * Gets the description attribute.
     * @return The String value of description attribute.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description attribute.
     * @param newDescription The new String value to set.
     */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * Gets the avatar attribute.
     * @return The String value of avatar attribute.
     */
    public String getAvatar() {
        return this.avatar;
    }

    /**
     * Sets the avatar attribute.
     * @param newAvatar The new String value to set.
     */
    public void setAvatar(String newAvatar) {
        this.avatar = newAvatar;
    }
}
