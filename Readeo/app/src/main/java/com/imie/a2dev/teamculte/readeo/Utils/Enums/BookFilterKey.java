package com.imie.a2dev.teamculte.readeo.Utils.Enums;

import com.imie.a2dev.teamculte.readeo.DBSchemas.AuthorDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.BookDBSchema;
import com.imie.a2dev.teamculte.readeo.DBSchemas.CategoryDBSchema;

/**
 * Enum used to represent the possible book filter keys from library.
 */
public enum BookFilterKey {
    NONE("None"),
    CATEGORY("Category"),
    AUTHOR("Author"),
    TITLE("Title");

    /**
     * Stores the filter key's associated database field.
     */
    private String name;

    /**
     * BookFilterKey's constructor.
     * @param name The name of th filter.
     */
    BookFilterKey(String name) {
        this.name = name;
    }

    /**
     * Gets a book filter key from a string value.
     * @param name The value used to get the associated book filter key.
     * @return The associated book filter key.
     */
    public static BookFilterKey fromName(String name) {
        BookFilterKey bookFilterKey = null;

        switch (name) {
            case "Title":
                bookFilterKey = BookFilterKey.TITLE;

                break;
            case "Author":
                bookFilterKey = BookFilterKey.AUTHOR;

                break;
            case "Category":
                bookFilterKey = BookFilterKey.CATEGORY;

                break;
            default:
                break;
        }

        return bookFilterKey;
    }

    /**
     * Gets the name attribute value.
     * @return The String value of the attribute.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the associated filter column name.
     * @return The filter column name associated to the enum.
     */
    public String getFilterCol() {
        String filterCol = null;

        switch (this) {
            case CATEGORY:
                filterCol = CategoryDBSchema.NAME;

                break;
            case AUTHOR:
                filterCol = AuthorDBSchema.NAME;

                break;
            case TITLE:
                filterCol = BookDBSchema.TITLE;

                break;
            default:
                break;
        }

        return filterCol;
    }
}
