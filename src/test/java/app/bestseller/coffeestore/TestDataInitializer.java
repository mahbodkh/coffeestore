package app.bestseller.coffeestore;

import app.bestseller.coffeestore.domain.Product;
import app.bestseller.coffeestore.domain.User;
import lombok.Getter;
import org.junit.jupiter.api.TestInstance;

/*
  Created by Abe with ❤️.
 */

@Getter
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TestDataInitializer {

    private User customer;
    private User admin;
    private Product blackCoffee;
    private Product latte;
    private Product mocha;
    private Product tea;
    private Product milk;
    private Product hazelnutSyrup;
    private Product chocolateSauce;
    private Product lemon;


    // ==============================================
    //                   DRINKS
    // ==============================================
    Product buildDrinkBlackCoffee() {
        var product = new Product();
        product.setType(Product.Type.DRINK);
        product.setPrice(4.0);
        product.setName("Black Coffee");
        return product;
    }

    Product buildDrinkLatte() {
        var product = new Product();
        product.setType(Product.Type.DRINK);
        product.setPrice(5.0);
        product.setName("Latte");
        return product;
    }

    Product buildDrinkMocha() {
        var product = new Product();
        product.setType(Product.Type.DRINK);
        product.setPrice(6.0);
        product.setName("Mocha");
        return product;
    }

    Product buildDrinkTea() {
        var product = new Product();
        product.setType(Product.Type.DRINK);
        product.setPrice(3.0);
        product.setName("Tea");
        return product;
    }

    // ==============================================
    //                  TOPPING
    // ==============================================
    Product buildToppingMilk() {
        var product = new Product();
        product.setType(Product.Type.TOPPING);
        product.setPrice(2.0);
        product.setName("Milk");
        return product;
    }

    Product buildToppingHazelnutSyrup() {
        var product = new Product();
        product.setType(Product.Type.TOPPING);
        product.setPrice(3.0);
        product.setName("Hazelnut Syrup");
        return product;
    }

    Product buildToppingChocolateSauce() {
        var product = new Product();
        product.setType(Product.Type.TOPPING);
        product.setPrice(5.0);
        product.setName("Chocolate Sauce");
        return product;
    }

    Product buildToppingLemon() {
        var product = new Product();
        product.setType(Product.Type.TOPPING);
        product.setPrice(2.0);
        product.setName("Lemon");
        return product;
    }


    // ==============================================
    //                   ADMIN
    // ==============================================
    User buildAdmin() {
        var user = new User();
        user.setUsername("bestseller");
        return user;
    }


    // ==============================================
    //                     USER
    // ==============================================
    User buildCustomer() {
        var user = new User();
        user.setUsername("ebrahim");
        return user;
    }


    protected void setUp() throws Exception {
        this.customer = buildCustomer();
        this.admin = buildAdmin();
        this.blackCoffee = buildDrinkBlackCoffee();
        this.latte = buildDrinkLatte();
        this.mocha = buildDrinkMocha();
        this.tea = buildDrinkTea();
        this.milk = buildToppingMilk();
        this.hazelnutSyrup = buildToppingHazelnutSyrup();
        this.chocolateSauce = buildToppingChocolateSauce();
        this.lemon = buildToppingLemon();
    }
}
