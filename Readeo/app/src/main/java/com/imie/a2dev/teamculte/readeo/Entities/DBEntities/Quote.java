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
     * Stores the pseudo of the user who wrote the quote.
     */
    private String author;

    /**
     * Quote's default constructor.
     */
    public Quote() {
        super();
    }

    /**
     * Quote's nearly full filled constructor, providing all attributes values except for the database's related ones.
     * @param quote The text of the quote to set.
     * @param author The user's pseudo related to the quote.
     */
    public Quote(String quote, String author) {
        super();

        this.quote = quote;
        this.author = author;
    }

    /**
     * Quote's full filled constructor, providing all attributes values.
     * @param id The id to set.
     * @param quote The text of the quote to set.
     * @param author The user's pseudo related to the quote.
     */
    public Quote(int id, String quote, String author) {
        super(id);

        this.quote = quote;
        this.author = author;
    }

    /**
     * Gets the quote attribute.
     * @return The String value of quote attribute.
     */
    public String getQuote() {
        return this.quote;
    }

    /**
     * Sets the quote attribute.
     * @param newQuote The new String value to set.
     */
    public void setQuote(String newQuote) {
        this.quote = newQuote;
    }

    /**
     * Gets the author attribute.
     * @return The String value of author attribute.
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Sets the author attribute.
     * @param newAuthor The new String value to set.
     */
    public void setAuthor(String newAuthor) {
        this.author = newAuthor;
    }
}
