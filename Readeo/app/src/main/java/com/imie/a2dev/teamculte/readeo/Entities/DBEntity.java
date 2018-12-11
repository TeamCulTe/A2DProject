package com.imie.a2dev.teamculte.readeo.Entities;

/**
 * Abstract class defining the base attributes of the database entities classes.
 */
public abstract class DBEntity {
    /**
     * Stores the database identifier.
     */
    private int id;

    /**
     * Defines if the entity is deleted or not (soft deletion).
     */
    private boolean deleted = false;

    /**
     * DBEntity's default constructor.
     */
    protected DBEntity() {

    }

    /**
     * DBEntity's full filled constructor initializing all the database related attributes.
     *
     * @param id The id to set.
     * @param deleted The boolean value defining if the element is deleted or not.
     */
    protected DBEntity(int id, boolean deleted) {
        this.id = id;
        this.deleted = deleted;
    }

    /**
     * Gets the id attribute.
     *
     * @return The int value of id attribute.
     */
    public final int getId() {
        return this.id;
    }

    /**
     * Sets the id attribute.
     *
     * @param newId The new int value to set.
     */
    public final void setId(int newId) {
        this.id = newId;
    }

    /**
     * Gets the deleted attribute.
     *
     * @return The boolean value of deleted attribute.
     */
    public final boolean isDeleted() {
        return this.deleted;
    }

    /**
     * Sets the deleted attribute.
     *
     * @param newDeleted The new boolean value to set.
     */
    public final void setDeleted(boolean newDeleted) {
        this.deleted = newDeleted;
    }
}
