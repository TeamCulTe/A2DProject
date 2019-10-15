package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import android.database.Cursor;
import android.util.Log;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Abstract class defining the base attributes of the database entities classes.
 */
@Getter
@Setter
@EqualsAndHashCode
public abstract class DBEntity implements Serializable {
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

    /**
     * Defines if the entity is empty (used to check on loading).
     * @return true if the id is equal to 0 else false.
     */
    public boolean isEmpty() {
        return this.id == 0;
    }

    /**
     * Log a formatted error (class name and method name).
     * @param methodName The name of the current method.
     * @param error The error raised.
     */
    protected void logError(String methodName, Exception error) {
        Log.e(String.format("[%s:%s] : ", this.getClass().getName(), methodName), error.getMessage());
    }

}
