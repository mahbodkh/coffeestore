package app.bestseller.coffeestore.service;

import app.bestseller.coffeestore.TestDataInitializer;
import app.bestseller.coffeestore.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DiscountServiceTest extends TestDataInitializer {

    private Product blackCoffee;
    private Product mocha;
    private Product milk;
    private Product tea;
    private Product chocolateSauce;


    @BeforeEach
    public void setup() throws Exception {
        super.setUp();

        // prepare some products ( drink )
        blackCoffee = getBlackCoffee();
        mocha = getMocha();
        // prepare some products ( topping )
        milk = getMilk();
        tea = getTea();
        chocolateSauce = getChocolateSauce();
    }

    @InjectMocks
    DiscountService discountService = new DiscountService();

    /**
     * black coffee = 4€
     * milk = 2€
     */
    @Test
    @Order(1)
    @DisplayName("testDiscount_whenTotalAmountIsLessThanPromotionRules_thenExpectedReturnZeroDiscount")
    void testDiscount_whenTotalCostDoesNotExceedLimit() {
        // given
        List<Product> products = new ArrayList<>(List.of(blackCoffee, milk));
        // when
        double discount = discountService.applyAsDouble(products);
        // then
        assertThat(discount).isZero(); // No discount applied
    }


    /**
     * black coffee = 4€
     * mocha = 6€
     * chocolateSauce = 5€
     */
    @Test
    @Order(2)
    @DisplayName("testDiscount_whenTotalAmountAppliesTwentyFivePercentagePromotionRule_thenExpectedReturnDiscountByTwentyFivePromotion")
    void testDiscountWhenTotalCostExceedsLimitButNoDrinkLimit() {
        // given
        List<Product> products = new ArrayList<>(List.of(blackCoffee, mocha, chocolateSauce));
        // when
        double discount = discountService.applyAsDouble(products);
        // then
        assertThat(discount).isEqualTo(3.75); // 25% of (4.0 + 6.0 + 5.0) is 3.75
    }

    /**
     * black coffee = 4€
     * mocha = 6€
     * chocolateSauce = 5€
     * milk = 2€
     * total drinks >= 3 { blackCoffee, mocha, tea }
     */
    @Test
    @Order(3)
    @DisplayName("testDiscount_whenTotalAmountAppliesAllPromotionRules_thenExpectedReturnLowestToppingAmountDiscount")
    void testDiscount_whenEligibleForBothPromotions_expectedApplyLowestDiscountRate() {
        // given
        List<Product> products = new ArrayList<>(List.of(blackCoffee, mocha, tea, chocolateSauce, milk));
        // when
        double discount = discountService.applyAsDouble(products);
        // then
        assertThat(discount).isEqualTo(2.0); // Price of the lowest amount product (topping=milk) is 2.0
    }

    /**
     * black coffee = 4€
     * mocha = 6€
     * chocolateSauce = 5€
     * tea = 3€
     */
    @Test
    @Order(4)
    @DisplayName("testDiscount_whenTotalAmountIsLessThanPromotionRules_thenExpectedReturnLowestAmountDiscount")
    void testDiscountWhenDrinkLimitExceeds() {
        // given
        List<Product> products = new ArrayList<>(List.of(blackCoffee, mocha, chocolateSauce, tea));
        // when
        double discount = discountService.applyAsDouble(products);
        // then
        assertThat(discount).isEqualTo(3.0); // Price of the lowest amount drink (tea) is 3.0
    }


    /**
     * No products in the cart, so no discount should be applied
     */
    @Test
    @Order(5)
    @DisplayName("testDiscount_whenTotalAmountIsLessThanPromotionRules_thenExpectedReturnZeroDiscount")
    void testDiscountWhenCartIsEmpty() {
        // given
        List<Product> products = new ArrayList<>();
        // when
        double discount = discountService.applyAsDouble(products);
        // then
        assertThat(discount).isZero();
    }


    /**
     * black coffee = 4€
     * mocha = 6€
     * milk = 2€
     * <p>
     * total amount 4€ + 6€ + 2€ = 12€
     * Milk is the cheapest drink, but is not more than 12 euros.
     */
    @Test
    @Order(4)
    @DisplayName("testDiscount_whenTotalAmountIsLessThanPromotionRules_thenExpectedReturnZeroDiscount")
    void testDiscount_whenTotalCostIsEqualTwelve_expectedReturnZero() {
        // given
        List<Product> products = new ArrayList<>(List.of(blackCoffee, mocha, milk));
        // when
        double discount = discountService.applyAsDouble(products);
        // then
        assertThat(discount).isZero();
    }
}
