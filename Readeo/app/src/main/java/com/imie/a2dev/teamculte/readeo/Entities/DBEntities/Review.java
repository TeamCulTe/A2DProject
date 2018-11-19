package com.imie.a2dev.teamculte.readeo.Entities.DBEntities;

/**
 * Final class representing a review written by an user for a specific book.
 */
public final class Review extends DBEntity {
    /**
     * Stores the review's text.
     */
    private String review;

    /**
     * Stores the book concerned by the review.
     */
    private Book book;

    /**
     * Stores the user who wrote the review.
     */
    private User user;

    /**
     * Not yet implemented.
     * Defines if the review can be read by the other users or not.
     */
    private boolean shared = false;

    /**
     * Review's default constructor.
     */
    public Review() {

    }

    /**
     * Review's nearly full filled constructor, providing all attributes values, except for database related ones.
     *
     * @param review The review to set.
     * @param book The book to set.
     * @param user The user to set.
     * @param shared The value defining if the review is shared or not.
     */
    public Review(String review, Book book, User user, boolean shared) {
        this.review = review;
        this.book = book;
        this.user = user;
        this.shared = shared;
    }

    /**
     * Review's full filled constructor, providing all attributes values.
     *
     * @param review The review to set.
     * @param book The book to set.
     * @param user The user to set.
     * @param shared The value defining if the review is shared or not.
     */
    public Review(int id, String review, Book book, User user, boolean shared, boolean deleted) {
        super(id, deleted);

        this.review = review;
        this.book = book;
        this.user = user;
        this.shared = shared;
    }

    /**
     * Gets the review attribute.
     *
     * @return The String value of review attribute.
     */
    public String getReview() {
        return this.review;
    }

    /**
     * Sets the review attribute.
     *
     * @param newReview The new String value to set.
     */
    public void setReview(String newReview) {
        this.review = newReview;
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
     * Gets the shared attribute.
     *
     * @return The boolean value of the shared attribute.
     */
    public boolean isShared() {
        return this.shared;
    }

    /**
     * Sets the shared attribute.
     *
     * @param newShared The new boolean value to set.
     */
    public void setPublic(boolean newShared) {
        this.shared = newShared;
    }
}
