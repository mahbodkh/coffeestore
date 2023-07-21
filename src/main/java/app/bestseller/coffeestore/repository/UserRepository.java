package app.bestseller.coffeestore.repository;


import app.bestseller.coffeestore.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Created by Abe with ❤️.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
