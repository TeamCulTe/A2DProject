package com.imie.a2dev.teamculte.readeo.Utils.Enums;

/**
 * Enum defining a form input error.
 */
public enum InputError {
    NO_ERROR(0),
    PSEUDO_FORMAT(1),
    PSEUDO_TAKEN(2),
    EMAIL_FORMAT(3),
    EMAIL_TAKEN(4),
    CITY_FORMAT(5),
    PASSWORD_FORMAT(6),
    PASSWORD_MATCH(7);

    /**
     * Defines the default formatting error message.
     */
    private static final String FORMAT_MSG = "The format of the %s is not valid.";

    /**
     * Defines the default already taken error message.
     */
    private static final String TAKEN_MSG = "The %s is already taken.";

    /**
     * Defines the default not matching error message.
     */
    private static final String MATCH_MSG = "The %s don't match.";

    /**
     * Stores the enum's value.
     */
    private int value;

    /**
     * Stores the associated error message.
     */
    private String message;

    /**
     * InputError's constructor.
     * @param value The associated value to set.
     */
    InputError(int value) {
        this.value = value;
        
        this.initMessage();
    }

    /**
     * Gets the value attribute.
     * @return The int value of value attribute.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Sets the value attribute.
     * @param newValue The new int value to set.
     */
    public void setValue(int newValue) {
        this.value = newValue;
    }

    /**
     * Gets the message attribute.
     * @return The String value of message attribute.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets the message attribute.
     * @param newMessage The new String value to set.
     */
    public void setMessage(String newMessage) {
        this.message = newMessage;
    }

    /**
     * Depending on the enum value, defines the associated error message.
     */
    private void initMessage() {
        String baseMsg;
        String elt;
        
        switch (this.value) {
            case 1:
                baseMsg = FORMAT_MSG;
                elt = "pseudo";
                
                break;
            case 2:
                baseMsg = TAKEN_MSG;
                elt = "pseudo";
                
                break;
            case 3:
                baseMsg = FORMAT_MSG;
                elt = "email";
                
                break;
            case 4:
                baseMsg = TAKEN_MSG;
                elt = "email";
                
                break;
            case 5:
                baseMsg = FORMAT_MSG;
                elt = "city";
                
                break;
            case 6:
                baseMsg = FORMAT_MSG;
                elt = "password";

                break;
            case 7:
                baseMsg = MATCH_MSG;
                elt = "passwords";

                break;
            default:
                
                return;
        }
        
        this.message = String.format(baseMsg, elt);
    }
}
