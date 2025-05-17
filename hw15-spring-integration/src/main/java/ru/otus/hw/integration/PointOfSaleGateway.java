package ru.otus.hw.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.model.CloseDay;
import ru.otus.hw.model.ReceiptPosition;

@MessagingGateway
public interface PointOfSaleGateway {

    @Gateway(requestChannel = "inputPointOfSalesOperations")
    void registerOperation(ReceiptPosition receiptPosition);

    @Gateway(requestChannel = "inputPointOfSalesOperations")
    void registerOperation(CloseDay closeDay);
}