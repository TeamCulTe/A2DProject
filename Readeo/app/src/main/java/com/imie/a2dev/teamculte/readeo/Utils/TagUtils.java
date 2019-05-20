package com.imie.a2dev.teamculte.readeo.Utils;

/**
 * Class used to define all different tags from the app (logging/errors...).
 */
public abstract class TagUtils {
    /**
     * Defines the common error tag.
     */
    public final static String ERROR_TAG = "CommonError";

    /**
     * Defines the SQLite log tag.
     */
    public final static String SQLITE_TAG = "SQLiteError";

    /**
     * Defines the JSON log tag.
     */
    public final static String JSON_TAG = "JSONError";

    /**
     * Defines the server log tag.
     */
    public final static String SERVER_TAG = "ServerError";
}
