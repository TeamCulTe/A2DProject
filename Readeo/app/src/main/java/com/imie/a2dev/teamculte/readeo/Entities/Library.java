package com.imie.a2dev.teamculte.readeo.Entities;

import com.imie.a2dev.teamculte.readeo.Entities.DBEntities.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * Final class representing a library displaying a list of books to the user depending on the filter applied
 * (category, author, title...).
 */
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
    private List<Book> books = new ArrayList<>();

    /**
     * Library's constructor initialized by the filter.
     * @param filterKey The filter key.
     * @param filterValue The filter value.
     */
    public Library(String filterKey, String filterValue) {
        this.filterKey = filterKey;
        this.filterValue = filterValue;
    }

    /**
     * Gets the filterKey attribute.
     * @return The String value of filterKey attribute.
     */
    public String getFilterKey() {
        return this.filterKey;
    }

    /**
     * Gets the filterValue attribute.
     * @return The String value of filterValue attribute.
     */
    public String getFilterValue() {
        return this.filterValue;
    }

    /**
     * Gets the books attribute.
     * @return The List<Book> value of books attribute.
     */
    public List<Book> getBooks() {
        return this.books;
    }

    /**
     * Sets the filterKey attribute.
     * @param newFilterKey The new String value to set.
     */
    public void setFilterKey(String newFilterKey) {
        this.filterKey = newFilterKey;
    }

    /**
     * Sets the filterValue attribute.
     * @param newFilterValue The new String value to set.
     */
    public void setFilterValue(String newFilterValue) {
        this.filterValue = newFilterValue;
    }

    /**
     * Sets the books attribute.
     * @param newBooks The new List<Book> value to set.
     */
    public void setBooks(List<Book> newBooks) {
        this.books = newBooks;
    }
}
