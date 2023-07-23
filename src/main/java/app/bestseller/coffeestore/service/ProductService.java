package app.bestseller.coffeestore.service;

import app.bestseller.coffeestore.domain.Product;
import app.bestseller.coffeestore.exception.ProductNotFoundException;
import app.bestseller.coffeestore.repository.ProductRepository;
import app.bestseller.coffeestore.repository.ReportRepository;
import app.bestseller.coffeestore.service.dto.ProductDTO;
import app.bestseller.coffeestore.service.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ReportRepository reportRepository;
    private final ProductMapper productMapper;

    public Product createProduct(ProductDTO request) {
        log.info(" ==> Request the productDto prepare: ({})", request);
        final var product = productMapper.toEntity(request);
        final var save = productRepository.save(product);
        log.info(" <== The product has been persisted: ({})", save);
        return save;
    }


    /**
     * Get product by product id.
     *
     * @param productId is the id of product and should be Long.
     * @return Product Domain Model.
     */
    public Product getProductById(Long productId) {
        log.info(" ==> Fetching product with ID: {}", productId);
        return Optional.of(productRepository.findById(productId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElseThrow(() -> {
                    log.warn("Product with ID {} not found.", productId);
                    return new ProductNotFoundException("The product (" + productId + ") not found.");
                });
    }


    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * Update a product ( Drink / Topping ).
     *
     * @param productId is a product id and it should be Long.
     * @param request   the product dto comes from the API request.
     * @return an Optional Product.
     */
    public Optional<Product> updateProduct(Long productId, ProductDTO request) {
        return Optional.of(productRepository.findById(productId)).filter(Optional::isPresent).map(Optional::get).map(product -> {
            var updatedProduct = productMapper.copyProductDtoToEntity(request, product);
            var savedProduct = productRepository.save(updatedProduct);
            log.info(" <== The product has been edited: {}", savedProduct);
            return savedProduct;
        });
    }


    /**
     * admin able to delete of the product.
     *
     * @param productId is the id of product.
     */
    public void deleteProduct(Long productId) {
        productRepository.findById(productId).ifPresent(entity -> {
            productRepository.delete(entity);
            log.info(" <== Deleted product: {}", entity);
        });
    }

    /**
     * admin able to see most used Toppings.
     *
     * @param pageable
     * @return list of Product Entity Data Model.
     */
    public Page<Product> getMostUsedTopping(Pageable pageable) {
        var mostUsedTopping = reportRepository.findMostUsedTopping(pageable);
        log.info(" <== most used toppings are : {} ", mostUsedTopping);
        return mostUsedTopping;
    }


}
