package app.bestseller.coffeestore.exception;

/**
 * Created by Abe with ❤️.
 */

public class UserNotFoundException extends BadRequestException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
