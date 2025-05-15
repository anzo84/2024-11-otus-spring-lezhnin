package ru.otus.hw.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import ru.otus.hw.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@MessagingGateway
public interface PointOfSaleGateway {
    @Gateway(requestChannel = "receiptChannel")
    void generateReceipt(List<Product> products,
                         @Header("quantities") Map<Product, BigDecimal> quantities,
                         @Header("receiptDate") LocalDateTime date);
}