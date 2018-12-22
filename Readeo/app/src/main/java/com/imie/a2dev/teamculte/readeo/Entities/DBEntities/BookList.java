package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

import java.util.ArrayList;
import java.util.List;

/**
 * Final class representing a book list from a specific user.
 */
public final class BookList extends DBEntity {
    /**
     * Defines the type of the book list among "read", "reading", "to read".
     */
    private String type;

    /**
     * Stores the books associated to the book list.
     */
    private List<Book> books;

    /**
     * BookList's default constructor.
     */
    public BookList() {
        super();

        this.books = new ArrayList<>();
    }

    /**
     * BookList's nearly full filled constructor providing all attributes values except for the database related ones.
     * @param type The type to set.
     * @param books The list of books to set.
     */
    public BookList(String type, List<Book> books) {
        super();

        this.type = type;
        this.books = books;
    }

    /**
     * BookList's full filled constructor, providing all the attribute's values.
     * @param id The id to set.
     * @param type The type to set below (read, reading, to read).
     * @param books The list of books to set.
     */
    public BookList(int id, String type, List<Book> books) {
        super(id);

        this.type = type;
        this.books = books;
    }

    /**
     * Gets the type attribute.
     * @return The String value of type attribute.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Sets the type attribute.
     * @param newType The new String value to set.
     */
    public void setType(String newType) {
        this.type = newType;
    }

    /**
     * Gets the books attribute.
     * @return The List<Book> value of books attribute.
     */
    public List<Book> getBooks() {
        return this.books;
    }

    /**
     * Sets the books attribute.
     * @param newBooks The new List<Book> value to set.
     */
    public void setBooks(List<Book> newBooks) {
        this.books = newBooks;
    }
}
