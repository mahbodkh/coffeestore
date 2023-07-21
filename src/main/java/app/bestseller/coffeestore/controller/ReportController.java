package app.bestseller.coffeestore.controller;

import app.bestseller.coffeestore.controller.validator.PaginationValidator;
import app.bestseller.coffeestore.exception.BadRequestException;
import app.bestseller.coffeestore.service.ProductService;
import app.bestseller.coffeestore.service.dto.ProductResponseDTO;
import app.bestseller.coffeestore.service.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Abe with ❤️.
 */


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/reports", produces = "application/json")
public class ReportController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Operation(summary = "Get list of the most used toppings.")
    @GetMapping("/admin/topping/most-used/")
    public ResponseEntity<Page<ProductResponseDTO>> getMostUsedToppings(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) throws BadRequestException {
        var mostUsedToppings = productService.getMostUsedTopping(PaginationValidator.validatePaginationOrThrow(page, size));
        return ResponseEntity.ok(productMapper.toPage(mostUsedToppings));
    }
}
