package com.imie.a2dev.teamculte.readeo.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Final class representing a book from the application.
 */
public final class Book extends DBEntity {
    /**
     * Stores the title of the book.
     */
    private String title;

    /**
     * Stores the author of the book.
     */
    private String author;

    /**
     * Stores the path of the cover image.
     */
    private String cover;

    /**
     * Stores the summary of the book.
     */
    private String summary;

    /**
     * Stores the year of the book's publication.
     */
    private int datePublished;

    /**
     * Stores the category of the book.
     */
    private String category;

    /**
     * Stores the list of quotes of the book.
     */
    private List<Quote> quotes;

    /**
     * Stores the list of reviews of the book.
     */
    private List<Review> reviews;

    /**
     * Book's default constructor.
     */
    public Book() {
        this.quotes = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    /**
     * Book's nearly full filled constructor, feeding all attributes except the one related to database (id and
     * deleted).
     *
     * @param title The title to set.
     * @param author The author to set.
     * @param cover The cover to set.
     * @param summary The summary to set.
     * @param datePublished The publication date to set.
     * @param category The category to set.
     * @param quotes The list of quotes to set.
     * @param reviews The list of reviews to set.
     */
    public Book(String title, String author, String cover, String summary, int datePublished, String category,
                List<Quote> quotes, List<Review> reviews) {
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.summary = summary;
        this.datePublished = datePublished;
        this.category = category;
        this.quotes = quotes;
        this.reviews = reviews;
    }

    /**
     * Book's full filled constructor, feeding all attributes.
     *
     * @param id The id to set.
     * @param title The title to set.
     * @param author The author to set.
     * @param cover The cover to set.
     * @param summary The summary to set.
     * @param datePublished The publication date to set.
     * @param category The category to set.
     * @param quotes The list of quotes to set.
     * @param reviews The list of reviews to set.
     * @param deleted The boolean value defining if the element is deleted or not.
     */
    public Book(int id, String title, String author, String cover, String summary, int datePublished, String category,
                List<Quote> quotes, List<Review> reviews, boolean deleted) {
        super(id, deleted);

        this.title = title;
        this.author = author;
        this.cover = cover;
        this.summary = summary;
        this.datePublished = datePublished;
        this.category = category;
        this.quotes = quotes;
        this.reviews = reviews;
    }

    /**
     * Gets the title attribute.
     *
     * @return The String value of title attribute.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title attribute.
     *
     * @param newTitle The new String value to set.
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
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
     * @param newAuthor The new String value to set.
     */
    public void setAuthor(String newAuthor) {
        this.author = newAuthor;
    }

    /**
     * Gets the cover attribute.
     *
     * @return The String value of cover attribute.
     */
    public String getCover() {
        return this.cover;
    }

    /**
     * Sets the cover attribute.
     *
     * @param newCover The new String value to set.
     */
    public void setCover(String newCover) {
        this.cover = newCover;
    }

    /**
     * Gets the summary attribute.
     *
     * @return The String value of summary attribute.
     */
    public String getSummary() {
        return this.summary;
    }

    /**
     * Sets the summary attribute.
     *
     * @param newSummary The new String value to set.
     */
    public void setSummary(String newSummary) {
        this.summary = newSummary;
    }

    /**
     * Gets the datePublished attribute.
     *
     * @return The int value of datePublished attribute.
     */
    public int getDatePublished() {
        return this.datePublished;
    }

    /**
     * Sets the datePublished attribute.
     *
     * @param newDatePublished The new int value to set.
     */
    public void setDatePublished(int newDatePublished) {
        this.datePublished = newDatePublished;
    }

    /**
     * Gets the category attribute.
     *
     * @return The String value of category attribute.
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Sets the category attribute.
     *
     * @param newCategory The new String value to set.
     */
    public void setCategory(String newCategory) {
        this.category = newCategory;
    }

    /**
     * Gets the quotes attribute.
     *
     * @return The List<Quote> value of quotes attribute.
     */
    public List<Quote> getQuotes() {
        return this.quotes;
    }

    /**
     * Sets the quotes attribute.
     *
     * @param newQuotes The new List<Quote> value to set.
     */
    public void setQuotes(List<Quote> newQuotes) {
        this.quotes = newQuotes;
    }

    /**
     * Gets the reviews attribute.
     *
     * @return The List<Review> value of reviews attribute.
     */
    public List<Review> getReviews() {
        return this.reviews;
    }

    /**
     * Sets the reviews attribute.
     *
     * @param newReviews The new List<Review> value to set.
     */
    public void setReviews(List<Review> newReviews) {
        this.reviews = newReviews;
    }
}
