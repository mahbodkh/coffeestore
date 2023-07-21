package app.bestseller.coffeestore.controller.validator;

import static app.bestseller.coffeestore.config.Constants.MAX_PAGE_SIZE;
import static org.junit.jupiter.api.Assertions.*;

import app.bestseller.coffeestore.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

/**
 * Created by Abe with ❤️.
 */

class PaginationValidatorTest {

    @Test
    void testValidatePaginationOrThrow_ValidInput_ReturnsPageable() throws BadRequestException {
        // given
        int page = 1;
        int size = 20;
        // when
        Pageable pageable = PaginationValidator.validatePaginationOrThrow(page, size);
        // then
        assertEquals(page - 1, pageable.getPageNumber());
        assertEquals(size, pageable.getPageSize());
    }

    @Test
    void testValidatePaginationOrThrow_ZeroSize_ThrowsBadRequestException() {
        // given
        int page = 1;
        int size = 0;
        // then
        assertThrows(BadRequestException.class,
                () -> PaginationValidator.validatePaginationOrThrow(page, size));
    }

    @Test
    void testValidatePaginationOrThrow_SizeExceedsMaxPageSize_ThrowsBadRequestException() {
        // given
        int page = 1;
        int size = MAX_PAGE_SIZE + 1;
        // then
        assertThrows(BadRequestException.class,
                () -> PaginationValidator.validatePaginationOrThrow(page, size));
    }
}