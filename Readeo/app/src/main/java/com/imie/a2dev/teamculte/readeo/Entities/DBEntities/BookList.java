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
     * Stores the owner of the book list.
     */
    private User user;

    /**
     * BookList's default constructor.
     */
    public BookList() {
        this.books = new ArrayList<>();
    }

    /**
     * BookList's nearly full filled constructor providing all attributes values except for the database related ones.
     *
     * @param type The type to set.
     * @param books The list of books to set.
     * @param user The user related to the book list to set.
     */
    public BookList(String type, List<Book> books, User user) {
        this.type = type;
        this.books = books;
        this.user = user;
    }

    /**
     * BookList's full filled constructor, providing all the attribute's values.
     *
     * @param id The id to set.
     * @param type The type to set below (read, reading, to read).
     * @param books The list of books to set.
     * @param user The user to set.
     * @param deleted The deleted status to set.
     */
    public BookList(int id, String type, List<Book> books, User user, boolean deleted) {
        super(id, deleted);

        this.type = type;
        this.books = books;
        this.user = user;
    }

    /**
     * Gets the type attribute.
     *
     * @return The String value of type attribute.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Sets the type attribute.
     *
     * @param newType The new String value to set.
     */
    public void setType(String newType) {
        this.type = newType;
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

    /**
     * Gets the user attribute.
     *
     * @return The User value of user attribute.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Sets the user attribute.
     *
     * @param newUser The new User value to set.
     */
    public void setUser(User newUser) {
        this.user = newUser;
    }
}
