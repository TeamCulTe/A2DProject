package com.imie.a2dev.teamculte.readeo.Entities;

import com.imie.a2dev.teamculte.readeo.App;
import com.imie.a2dev.teamculte.readeo.DBManagers.BookDBManager;
import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Final class representing a library displaying a list of books to the user depending on the filter applied
 * (category, author, title...).
 */
@Getter
@Setter
public final class Library {
    /**
     * Stores the type of filter applied.
     */
    private String filterKey;

    /**
     * Stores the value to filter on.
     */
    private String filterValue;

    /**
     * Stores the list of book matching to the filter.
     */
    private List<Book> books;

    /**
     * Library's default constructor.
     */
    public Library() {
        this.books = new ArrayList<>();
    }

    /**
     * Library's constructor initialized by the filter.
     * @param filterKey The filter key.
     * @param filterValue The filter value.
     */
    public Library(String filterKey, String filterValue) {
        this.filterKey = filterKey;
        this.filterValue = filterValue;
        this.books = new BookDBManager(App.getAppContext()).loadFilteredSQLite(this.filterKey, this.filterValue);
    }
}
