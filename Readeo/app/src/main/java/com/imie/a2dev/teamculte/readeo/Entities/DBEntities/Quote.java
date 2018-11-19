package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

/**
 * Final class representing a quote from a user to a specific book.
 */
public final class Quote extends DBEntity {
    /**
     * Stores the text of the quote.
     */
    private String quote;

    /**
     * Stores the user who wrote the quote.
     */
    private User user;

    /**
     * Stores the book on which the review has been written.
     */
    private Book book;

    /**
     * Quote's default constructor.
     */
    public Quote() {

    }

    /**
     * Quote's nearly full filled constructor, providing all attributes values except for the database's related ones.
     *
     * @param quote The text of the quote to set.
     * @param user The user related to the quote.
     * @param book The book concerned by the review.
     */
    public Quote(String quote, User user, Book book) {
        this.quote = quote;
        this.user = user;
        this.book = book;
    }

    /**
     * Quote's full filled constructor, providing all attributes values.
     *
     * @param id The id to set.
     * @param quote The text of the quote to set.
     * @param user The user related to the quote.
     * @param book The book concerned by the review.
     * @param deleted The boolean value of the deleted attribute to set.
     */
    public Quote(int id, String quote, User user, Book book, boolean deleted) {
        super(id, deleted);

        this.quote = quote;
        this.user = user;
        this.book = book;
    }

    /**
     * Gets the quote attribute.
     *
     * @return The String value of quote attribute.
     */
    public String getQuote() {
        return this.quote;
    }

    /**
     * Sets the quote attribute.
     *
     * @param newQuote The new String value to set.
     */
    public void setQuote(String newQuote) {
        this.quote = newQuote;
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

    /**
     * Gets the book attribute.
     *
     * @return The Book value of book attribute.
     */
    public Book getBook() {
        return this.book;
    }

    /**
     * Sets the book attribute.
     *
     * @param newBook The new Book value to set.
     */
    public void setBook(Book newBook) {
        this.book = newBook;
    }
}
