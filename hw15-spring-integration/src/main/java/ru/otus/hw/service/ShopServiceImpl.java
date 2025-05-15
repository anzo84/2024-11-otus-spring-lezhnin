package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.model.Product;
import ru.otus.hw.model.Receipt;
import ru.otus.hw.model.ReceiptPosition;
import ru.otus.hw.model.SaleReport;
import ru.otus.hw.model.SaleReportPosition;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {

    @Override
    public ReceiptPosition makePosition(Product product, BigDecimal quantity) {
        ReceiptPosition receiptPosition = new ReceiptPosition();
        receiptPosition.setProductName(product.getProductName());
        receiptPosition.setQuantity(quantity);
        receiptPosition.setPrice(product.getPrice());
        receiptPosition.setDiscount(quantity.multiply(product.getPrice())
            .setScale(2, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(0.05))
            .setScale(2, RoundingMode.HALF_UP));
        return receiptPosition;
    }

    @Override
    public Receipt makeReceipt(List<ReceiptPosition> positions, LocalDateTime dateTime) {
        Receipt receipt = new Receipt();
        receipt.setDateTime(dateTime);
        receipt.setReceiptPositions(positions);
        return receipt;
    }

    @Override
    public SaleReport makeSaleReport(List<Receipt> receipts, LocalDate date) {
        List<SaleReportPosition> reportPositions = new ArrayList<>(receipts.stream()
            .filter(receipt -> receipt.getDateTime().toLocalDate().equals(date))
            .flatMap(receipt -> receipt.getReceiptPositions().stream())
            .collect(Collectors.toMap(
                ReceiptPosition::getProductName,
                this::createReportPosition,
                this::mergeReportPositions
            ))
            .values());

        SaleReport saleReport = new SaleReport();
        saleReport.setDate(date);
        saleReport.setReportPositions(reportPositions);
        return saleReport;
    }

    private SaleReportPosition createReportPosition(ReceiptPosition position) {
        SaleReportPosition reportPosition = new SaleReportPosition();
        reportPosition.setProductName(position.getProductName());
        reportPosition.setUom(position.getUom());
        reportPosition.setQuantity(position.getQuantity());
        reportPosition.setTotal(position.getPrice()
            .multiply(position.getQuantity())
            .setScale(2, RoundingMode.HALF_UP)
            .subtract(position.getDiscount())
        );
        return reportPosition;
    }

    private SaleReportPosition mergeReportPositions(SaleReportPosition existing, SaleReportPosition newPosition) {
        existing.setQuantity(existing.getQuantity().add(newPosition.getQuantity()));
        existing.setTotal(existing.getTotal().add(newPosition.getTotal()));
        return existing;
    }

}
