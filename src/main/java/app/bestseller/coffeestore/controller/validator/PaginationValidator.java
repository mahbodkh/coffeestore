package app.bestseller.coffeestore.controller.validator;

import app.bestseller.coffeestore.exception.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static app.bestseller.coffeestore.config.Constants.MAX_PAGE_SIZE;

/**
 * Created by Abe with ❤️.
 */

public class PaginationValidator {

    public static Pageable validatePaginationOrThrow(int page, int size) throws BadRequestException {
        if (page < 0) page = 0;
        if (size <= 0 || size > MAX_PAGE_SIZE) {
            throw new BadRequestException("page size (" + size + ") must be between 1 and " + MAX_PAGE_SIZE);
        }
        return PageRequest.of(page - 1, size);
    }

    private PaginationValidator() {
    }
}
