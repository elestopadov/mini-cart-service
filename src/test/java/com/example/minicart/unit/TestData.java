package com.example.minicart.unit;
import com.example.minicart.model.Category;
import com.example.minicart.model.Product;
import java.math.BigDecimal;
public final class TestData {
    private TestData() { }
    public static Product product(String id, String price, Category category) { return new Product(id, id + " name", new BigDecimal(price), category); }
}
