package app.bestseller.coffeestore.service;

import app.bestseller.coffeestore.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.ToDoubleFunction;

import static app.bestseller.coffeestore.config.Constants.*;

/**
 * Created by Abe with ❤️.
 */


@Slf4j
@Service
public class DiscountService implements ToDoubleFunction<List<Product>> {

    /**
     * The main discount logic.
     *
     * @param orders the customer order (drinks/toppings).
     * @return the discount is calculated.
     */
    @Override
    public double applyAsDouble(List<Product> orders) {
        final var totalAmount = calculateTotalPrice(orders);
        final var drinkSize = getTotalDrinkSize(orders);
        final var twentyFivePromoteRate = calculateDiscountByPromotionByTwentyFivePercentage(totalAmount);
        final var amountLowestProductRate = calculateDiscountByLowestAmountProduct(orders);

        if (twentyFivePromoteRate == 0 && amountLowestProductRate == 0) {
            log.info("Discount is not available.");
            return 0.0;
        }
        if (totalAmount > DISCOUNT_AMOUNT_LIMIT && drinkSize >= DISCOUNT_DRINK_LIMIT) {
            return Math.min(twentyFivePromoteRate, amountLowestProductRate);
        } else if (totalAmount > DISCOUNT_AMOUNT_LIMIT) {
            return twentyFivePromoteRate;
        } else if (drinkSize >= DISCOUNT_DRINK_LIMIT) {
            return amountLowestProductRate;
        } else
            return 0.0;
    }

    /**
     * get sum of Drinks in the customer order.
     *
     * @param products are list of the customer order.
     * @return number of Drinks of the customer order.
     */
    private int getTotalDrinkSize(List<Product> products) {
        return products.stream()
                .filter(product -> Product.Type.DRINK.equals(product.getType())).toList().size();
    }


    /**
     * Calculate the total cost of the customer order and then to apply 25% discount.
     *
     * @param total amount of the customer order.
     * @return the discount of the customer according to the TwentyFive percentage rule.
     */
    private double calculateDiscountByPromotionByTwentyFivePercentage(Double total) {
        double twentyFivePromoteRate = total * DISCOUNT_RATE_PERCENTAGE;
        log.info("Discount promotion calculated by twenty-five percentage and applied by: {} rate. ", twentyFivePromoteRate);
        return twentyFivePromoteRate;
    }


    /**
     * Calculate the lowest amount of the costumer order ( drinks / toppings ).
     *
     * @param products list of product (drinks/toppings).
     * @return the calculated discount according to the lowest amount rule.
     */
    private double calculateDiscountByLowestAmountProduct(List<Product> products) {
        double lowestPriceCalculated = products.stream().mapToDouble(Product::getPrice).min().orElse(0.0);
        log.info("Discount calculated by lowest amount of the products and applied by: {} rate ", lowestPriceCalculated);
        return lowestPriceCalculated;
    }


    /**
     * Calculate sum of drinks and toppings prices.
     *
     * @param products is list of the customer order.
     * @return a sum of total price of the customer order.
     */
    private double calculateTotalPrice(List<Product> products) {
        return products.stream()
                .mapToDouble(Product::getPrice).sum();
    }
}
