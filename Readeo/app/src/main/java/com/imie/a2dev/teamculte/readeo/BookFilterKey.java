package com.imie.a2dev.teamculte.readeo;

import com.imie.a2dev.teamculte.readeo.DBManagers.AuthorDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookDBManager;
import com.imie.a2dev.teamculte.readeo.DBManagers.CategoryDBManager;

/**
 * Enum used to represent the possible book filter keys from library.
 */
public enum BookFilterKey {
    CATEGORY (CategoryDBManager.NAME),
    AUTHOR (AuthorDBManager.NAME),
    TITLE (BookDBManager.TITLE);

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
     * Gets the name attribute value.
     * @return The String value of the attribute.
     */
    public String getName() {
        return this.name;
    }
}
