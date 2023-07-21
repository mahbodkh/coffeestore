package app.bestseller.coffeestore.controller;

import app.bestseller.coffeestore.controller.validator.PaginationValidator;
import app.bestseller.coffeestore.exception.BadRequestException;
import app.bestseller.coffeestore.service.ProductService;
import app.bestseller.coffeestore.service.dto.ProductResponseDTO;
import app.bestseller.coffeestore.service.dto.ProductDTO;
import app.bestseller.coffeestore.service.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Abe with ❤️.
 */


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/products", produces = "application/json")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    // ==============================================
    //                     CLIENT
    // ==============================================
    @Operation(summary = "Get a product by id.")
    @GetMapping("/{id}/")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable("id") Long product) {
        var reply = productService.getProductById(product);
        return ResponseEntity.ok(productMapper.toDto(reply));
    }

    @Operation(summary = "Get all products.")
    @GetMapping("/all/")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) throws BadRequestException {
        var products = productService.getProducts(PaginationValidator.validatePaginationOrThrow(page, size));
        return ResponseEntity.ok(productMapper.toPage(products));
    }

    // ==============================================
    //                     ADMIN
    // ==============================================
    @Operation(summary = "Create a product (Drink/Topping).")
    @PostMapping("/admin/create/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@Valid @RequestBody ProductDTO requestDto) throws BadRequestException {
        productService.createProduct(requestDto);
    }

    @Operation(summary = "Update a product information with the given ID.")
    @PutMapping("/admin/{id}/edit/")
    @ResponseStatus(HttpStatus.OK)
    public void editProduct(@Valid @PathVariable("id") Long productId, @Valid @RequestBody ProductDTO requestDto) {
        productService.updateProduct(productId, requestDto);
    }

    @Operation(summary = "Delete a product by productId.")
    @DeleteMapping("/admin/{id}/delete/")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@Valid @PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
    }

}
