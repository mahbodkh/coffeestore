package app.bestseller.coffeestore.repository;

import app.bestseller.coffeestore.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p " +
            "where p.type = 'TOPPING' " +
            "and p.id in ( select product.id " +
            "from Order o " +
            "join o.products product " +
            "group by product.id " +
            "order by count(product.id) desc )")
    Page<Product> findMostUsedTopping(Pageable pageable);
}
