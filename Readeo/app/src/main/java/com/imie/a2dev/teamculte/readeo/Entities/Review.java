package com.imie.a2dev.teamculte.readeo.Entities;

/**
 * Final class representing a review written by an user for a specific book.
 */
public final class Review extends DBEntity {
    /**
     * Stores the review's text.
     */
    private String review;

    /**
     * Stores the author's pseudo.
     */
    private String author;

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
     * @param author The name of the author to set.
     * @param shared The value defining if the review is shared or not.
     */
    public Review(String review, String author, boolean shared) {
        this.review = review;
        this.author = author;
        this.shared = shared;
    }

    /**
     * Review's full filled constructor, providing all attributes values.
     *
     * @param review The review to set.
     * @param author The user to set.
     * @param shared The value defining if the review is shared or not.
     */
    public Review(int id, String review, String author, boolean shared, boolean deleted) {
        super(id, deleted);

        this.review = review;
        this.author = author;
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
     * Gets the author attribute.
     *
     * @return The String value of author attribute.
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Sets the author attribute.
     *
     * @param newAuthor The new User value to set.
     */
    public void setAuthor(String newAuthor) {
        this.author = newAuthor;
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
