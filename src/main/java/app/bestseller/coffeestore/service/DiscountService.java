package app.bestseller.coffeestore.service;

import app.bestseller.coffeestore.domain.Product;
import app.bestseller.coffeestore.domain.ProductType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.ToDoubleFunction;


@Slf4j
@Service
public class DiscountService implements ToDoubleFunction<List<Product>> {

    private static final double RATE_PERCENTAGE = 0.25;
    private static final int AMOUNT_LIMIT = 12;
    private static final int DRINK_LIMIT = 3;
    private static final double ZERO_VALUE = 0;
    private static final double DEFAULT_VALUE = 0;


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

        if (twentyFivePromoteRate == ZERO_VALUE && amountLowestProductRate == ZERO_VALUE) {
            log.info("Discount is not available.");
            return DEFAULT_VALUE;
        }
        if (totalAmount > AMOUNT_LIMIT && drinkSize >= DRINK_LIMIT) {
            return Math.min(twentyFivePromoteRate, amountLowestProductRate);
        } else if (totalAmount > AMOUNT_LIMIT) {
            return twentyFivePromoteRate;
        } else if (drinkSize >= DRINK_LIMIT) {
            return amountLowestProductRate;
        } else
            return DEFAULT_VALUE;
    }

    /**
     * get sum of Drinks in the customer order.
     *
     * @param products are list of the customer order.
     * @return number of Drinks of the customer order.
     */
    private int getTotalDrinkSize(List<Product> products) {
        return products.stream()
                .filter(product -> ProductType.DRINK.equals(product.getType())).toList().size();
    }


    /**
     * Calculate the total cost of the customer order and then to apply 25% discount.
     *
     * @param total amount of the customer order.
     * @return the discount of the customer according to the TwentyFive percentage rule.
     */
    private double calculateDiscountByPromotionByTwentyFivePercentage(Double total) {
        double twentyFivePromoteRate = total * RATE_PERCENTAGE;
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
