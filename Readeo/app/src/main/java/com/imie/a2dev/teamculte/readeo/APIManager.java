package com.imie.a2dev.teamculte.readeo;

/**
 * Abstract class used to define the different API access, roles and actions.
 */
public abstract class APIManager {
    /**
     * Stores the url used to access to the API.
     */
    public static final String API_URL = "http://10.0.2.2:8888/";

    // Entities mapping.

    /**
     * Stores the folder / mapping to access to authors functionality.
     */
    public static final String AUTHORS = "authors/";

    /**
     * Stores the folder / mapping to access to book list types functionality.
     */
    public static final String BOOK_LIST_TYPES = "book_list_types/";

    /**
     * Stores the folder / mapping to access to book lists functionality.
     */
    public static final String BOOK_LISTS = "book_lists/";

    /**
     * Stores the folder / mapping to access to books functionality.
     */
    public static final String BOOKS = "books/";

    /**
     * Stores the folder / mapping to access to categories functionality.
     */
    public static final String CATEGORIES = "categories/";

    /**
     * Stores the folder / mapping to access to cities functionality.
     */
    public static final String CITIES = "cities/";

    /**
     * Stores the folder / mapping to access to countries functionality.
     */
    public static final String COUNTRIES = "countries/";

    /**
     * Stores the folder / mapping to access to profiles functionality.
     */
    public static final String PROFILES = "profiles/";

    /**
     * Stores the folder / mapping to access to quotes functionality.
     */
    public static final String QUOTES = "quotes/";

    /**
     * Stores the folder / mapping to access to reviewa functionality.
     */
    public static final String REVIEWS = "reviews/";

    /**
     * Stores the folder / mapping to access to users functionality.
     */
    public static final String USERS = "users/";

    /**
     * Stores the folder / mapping to access to writers functionality.
     */
    public static final String WRITERS = "writers/";

    // Actions mapping.

    /**
     * Stores the create script file name / mapping.
     */
    public static final String CREATE = "create.php";

    /**
     * Stores the read script file name / mapping.
     */
    public static final String READ = "read.php?";

    /**
     * Stores the read script file name / mapping for getting the id and last update fields (providing the parameter).
     */
    public static final String READ_UPDATE = "read.php?update_query";

    /**
     * Stores the update script file name / mapping.
     */
    public static final String UPDATE = "update.php";

    /**
     * Stores the delete script file name / mapping.
     */
    public static final String DELETE = "delete.php";

    /**
     * Stores the restore script file name / mapping.
     */
    public static final String RESTORE = "restore.php";

    // Common parameters.

    /**
     * Defines the start parameter name for the API querying.
     */
    public static final String START = "start";

    /**
     * Defines the end parameter name for the API querying.
     */
    public static final String END = "end";

    /**
     * Defines the test parameter name for the API querying.
     */
    public static final String TEST = "test";

}
