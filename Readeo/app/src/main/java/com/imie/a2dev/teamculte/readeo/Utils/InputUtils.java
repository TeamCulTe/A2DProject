package com.imie.a2dev.teamculte.readeo.Utils;

import com.imie.a2dev.teamculte.readeo.DBManagers.UserDBManager;
import com.imie.a2dev.teamculte.readeo.Utils.Enums.InputError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.EMAIL;
import static com.imie.a2dev.teamculte.readeo.DBSchemas.UserDBSchema.PSEUDO;

/**
 * Abstract class used to validate input values from forms.
 */
public abstract class InputUtils {
    /**
     * Defines the valid pseudo regex pattern.
     */
    private static final Pattern PSEUDO_PATTERN = Pattern.compile("^[a-zA-Z0-9]{4,}$");

    /**
     * Defines the valid email address regex pattern.
     */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Defines the valid city regex pattern.
     */
    private static final Pattern CITY_PATTERN =
            Pattern.compile(
                    "^[a-zA-Z\\u0080-\\u024F]+(?:([ \\-']|(\\. ))[a-zA-Z\\u0080-\\u024F]+)*$");

    /**
     * Defines the valid password regex pattern.
     */
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("[a-zA-Z0-9]{8,20}");

    /**
     * Checks the validity of the pseudo (match to pattern and is available).
     * @param pseudo The pseudo to check.
     * @param listener The listener to call.
     * @return The associated input error.
     */
    public static InputError validatePseudo(String pseudo,
                                            HTTPRequestQueueSingleton.HTTPRequestQueueListener listener) {
        InputError error = InputError.NO_ERROR;
        UserDBManager manager = ManagerHolderUtils.getInstance().getUserDBManager();

        if (!InputUtils.validateFormat(pseudo, PSEUDO_PATTERN)) {
            error = InputError.PSEUDO_FORMAT;
        } else if (!manager.isAvailableMySQL(PSEUDO, pseudo, listener)) {
            error = InputError.PSEUDO_TAKEN;
        }

        return error;
    }

    /**
     * Checks the validity of the email (match to pattern and is available).
     * @param email The email to check.
     * @param listener The listener to call.
     * @return The associated input error.
     */
    public static InputError validateEmail(String email,
                                           HTTPRequestQueueSingleton.HTTPRequestQueueListener listener) {
        InputError error = InputError.NO_ERROR;
        UserDBManager manager = ManagerHolderUtils.getInstance().getUserDBManager();

        if (!InputUtils.validateFormat(email, EMAIL_PATTERN)) {
            error = InputError.EMAIL_FORMAT;
        } else if (!manager.isAvailableMySQL(EMAIL, email, listener)) {
            error = InputError.EMAIL_TAKEN;
        }

        return error;
    }

    /**
     * Checks the validity of the password (match to pattern and both identical).
     * @param password The password to check.
     * @param confirmation The password confirmation.
     * @return The associated input error.
     */
    public static InputError validatePassword(String password, String confirmation) {
        InputError error = InputError.NO_ERROR;

        if (!InputUtils.validateFormat(password, PASSWORD_PATTERN)) {
            error = InputError.PASSWORD_FORMAT;
        } else if (!password.equals(confirmation)) {
            error = InputError.PASSWORD_MATCH;
        }

        return error;
    }

    /**
     * Checks the validity of the city (match to pattern).
     * @param city The password to check.
     * @return The associated input error.
     */
    public static InputError validateCity(String city) {
        if (!InputUtils.validateFormat(city, CITY_PATTERN)) {
            return InputError.CITY_FORMAT;
        } else {
            return InputError.NO_ERROR;
        }
    }

    /**
     * Checks if the input given in parameter is valid or not depending on the Pattern provided.
     * @param input The input to check.
     * @param pattern The pattern to apply.
     * @return True if valid else false.
     */
    private static boolean validateFormat(String input, Pattern pattern) {
        Matcher matcher = pattern.matcher(input);

        return matcher.find();
    }
}
