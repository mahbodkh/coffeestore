package app.bestseller.coffeestore.exception;


import java.util.Collections;
import java.util.Map;

/**
 * Created by Abe with ❤️.
 */

public class BadRequestException extends ValidationException {

    public BadRequestException(String message, Map<String, String> errors) {
        super(message, errors);
    }

    public BadRequestException(String message) {
        this(message, Collections.emptyMap());
    }
}
