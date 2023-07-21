package app.bestseller.coffeestore.exception;

/**
 * Created by Abe with ❤️.
 */
public class ProductNotFoundException extends BadRequestException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}