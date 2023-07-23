package app.bestseller.coffeestore.controller;

import app.bestseller.coffeestore.controller.validator.PaginationParams;
import app.bestseller.coffeestore.exception.BadRequestException;
import app.bestseller.coffeestore.service.ProductService;
import app.bestseller.coffeestore.service.dto.ProductResponseDTO;
import app.bestseller.coffeestore.service.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/reports", produces = "application/json")
public class ReportController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Operation(summary = "Get list of the most used toppings.")
    @GetMapping("/admin/topping/most-used/")
    public ResponseEntity<Page<ProductResponseDTO>> getMostUsedToppings(
            @Valid PaginationParams params
    ) throws BadRequestException {
        var mostUsedToppings = productService.getMostUsedTopping(PageRequest.of(params.getPage(), params.getSize()));
        return ResponseEntity.ok(productMapper.toPage(mostUsedToppings));
    }
}
