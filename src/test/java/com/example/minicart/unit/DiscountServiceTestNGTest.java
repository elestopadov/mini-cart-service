package com.example.minicart.unit;

import com.example.minicart.model.Cart;
import com.example.minicart.model.Category;
import com.example.minicart.service.DiscountService;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscountServiceTestNGTest {
    private final DiscountService service = new DiscountService();

    @DataProvider(name = "categories")
    public Object[][] categories() {
        return new Object[][]{
                {Category.ELECTRONICS, "1.00"},
                {Category.CLOTHING, "2.00"},
                {Category.FOOD, "0.00"},
                {Category.BOOKS, "3.00"}
        };
    }

    @Test(dataProvider = "categories")
    public void data_driven_discount_by_category(Category category, String expected) {
        Cart cart = new Cart("c-category");
        cart.addItem(TestData.product("p1", "20", category), 1);
        assertThat(service.calculateDiscount(cart)).isEqualByComparingTo(expected);
    }

    @DataProvider(name = "thresholds")
    public Object[][] thresholds() {
        return new Object[][]{
                {"99", "0.00"},
                {"100", "3.00"},
                {"499", "14.97"},
                {"500", "35.00"}
        };
    }

    @Test(dataProvider = "thresholds")
    public void data_driven_threshold_boundary(String amount, String expected) {
        Cart cart = new Cart("c-threshold");
        cart.addItem(TestData.product("p1", amount, Category.FOOD), 1);
        assertThat(service.calculateDiscount(cart)).isEqualByComparingTo(expected);
    }
}
