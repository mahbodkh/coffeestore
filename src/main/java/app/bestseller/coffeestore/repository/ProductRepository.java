package app.bestseller.coffeestore.repository;

import app.bestseller.coffeestore.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by Abe with ❤️.
 */


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}