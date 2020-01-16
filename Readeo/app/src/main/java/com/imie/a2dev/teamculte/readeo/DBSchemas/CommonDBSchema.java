package com.imie.a2dev.teamculte.readeo.DBSchemas;

/**
 * Class used to define the common database schema (such as last_update field).
 * Useful to separate logic code from managers and structures.
 */
public abstract class CommonDBSchema {

    /**
     * Stores the default last update column.
     */
    public static String UPDATE = "last_update";

    /**
     * Stores the default lat update column value.
     */
    public static String UPDATE_DEFAULT = "CURRENT_TIMESTAMP";

    /**
     * Stores the default date time format.
     */
    public static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

}
