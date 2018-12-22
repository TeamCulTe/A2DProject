package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

/**
 * Abstract class defining the base attributes of the database entities classes.
 */
public abstract class DBEntity {
    /**
     * Stores the database identifier.
     */
    private int id;

    /**
     * DBEntity's default constructor.
     */
    protected DBEntity() {
        this.id = 0;
    }

    /**
     * DBEntity's full filled constructor initializing all the database related attributes.
     * @param id The id to set.
     */
    protected DBEntity(int id) {
        this.id = id;
    }

    /**
     * Gets the id attribute.
     * @return The int value of id attribute.
     */
    public final int getId() {
        return this.id;
    }

    /**
     * Sets the id attribute.
     * @param newId The new int value to set.
     */
    public final void setId(int newId) {
        this.id = newId;
    }
}
