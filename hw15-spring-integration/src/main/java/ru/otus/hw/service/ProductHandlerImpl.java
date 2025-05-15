package ru.otus.hw.service;

import org.springframework.stereotype.Component;
import ru.otus.hw.model.Product;
import ru.otus.hw.model.ReceiptPosition;

import java.math.BigDecimal;

@Component
public class ProductHandlerImpl implements ProductHandler {

    private Product currentProduct;

    private final ShopService shopService;

    public ProductHandlerImpl(ShopService shopService) {
        this.shopService = shopService;
    }

    @Override
    public void handleProductSelection(Product product) {
        this.currentProduct = product;
    }

    @Override
    public ReceiptPosition handleQuantitySetting(BigDecimal quantity) {
        if (currentProduct == null) {
            throw new IllegalStateException("Product must be selected first");
        }
        ReceiptPosition position = shopService.makePosition(currentProduct, quantity);
        currentProduct = null;
        return position;
    }
}
