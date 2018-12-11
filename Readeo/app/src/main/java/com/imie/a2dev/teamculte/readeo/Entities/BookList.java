package com.imie.a2dev.teamculte.readeo.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Final class representing a book list from a specific user.
 */
public final class BookList extends DBEntity {
    /**
     * Defines the label of the book list among "read", "reading", "to read".
     */
    private String label;

    /**
     * Stores the books associated to the book list.
     */
    private List<Book> books;

    /**
     * BookList's default constructor.
     */
    public BookList() {
        this.books = new ArrayList<>();
    }

    /**
     * BookList's nearly full filled constructor providing all attributes values except for the database related ones.
     *
     * @param label The label to set.
     * @param books The list of books to set.
     */
    public BookList(String label, List<Book> books) {
        this.label = label;
        this.books = books;
    }

    /**
     * BookList's full filled constructor, providing all the attribute's values.
     *
     * @param id The id to set.
     * @param label The label to set below (read, reading, to read).
     * @param books The list of books to set.
     * @param deleted The deleted status to set.
     */
    public BookList(int id, String label, List<Book> books, boolean deleted) {
        super(id, deleted);

        this.label = label;
        this.books = books;
    }

    /**
     * Gets the label attribute.
     *
     * @return The String value of label attribute.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Sets the label attribute.
     *
     * @param newLabel The new String value to set.
     */
    public void setLabel(String newLabel) {
        this.label = newLabel;
    }

    /**
     * Gets the books attribute.
     *
     * @return The List<Book> value of books attribute.
     */
    public List<Book> getBooks() {
        return this.books;
    }

    /**
     * Sets the books attribute.
     *
     * @param newBooks The new List<Book> value to set.
     */
    public void setBooks(List<Book> newBooks) {
        this.books = newBooks;
    }
}
