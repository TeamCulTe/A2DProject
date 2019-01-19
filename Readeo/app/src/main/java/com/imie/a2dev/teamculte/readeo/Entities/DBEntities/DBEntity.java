package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.database.Cursor;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class defining the base attributes of the database entities classes.
 */
@Getter
@Setter
public abstract class DBEntity {
    /**
     * Stores the database identifier.
     */
    protected int id;

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
     * Initializes all the entity attributes values from the result of a database query
     * closes the cursor if close is true.
     * @param result The result of the query.
     * @param close Defines if the cursor should be closed or not.
     */
    protected abstract void init(Cursor result, boolean close);
}
