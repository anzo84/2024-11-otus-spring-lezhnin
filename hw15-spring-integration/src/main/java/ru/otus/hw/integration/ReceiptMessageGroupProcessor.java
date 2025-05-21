package ru.otus.hw.integration;

import org.springframework.integration.aggregator.MessageGroupProcessor;
import org.springframework.integration.store.MessageGroup;
import org.springframework.messaging.Message;
import ru.otus.hw.model.Receipt;
import ru.otus.hw.model.ReceiptPosition;
import ru.otus.hw.model.SaleReport;
import ru.otus.hw.model.SaleReportPosition;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Формирует отчет из чеков продажи
 */
public class ReceiptMessageGroupProcessor implements MessageGroupProcessor {

    @Override
    public Object processMessageGroup(MessageGroup group) {
        if (group.getMessages().isEmpty()) {
            return null;
        }
        Map<String, SaleReportPosition> reportPositionMap = group.getMessages()
            .stream()
            .map(Message::getPayload)
            .filter(Receipt.class::isInstance)
            .map(Receipt.class::cast)
            .flatMap(positions -> positions.getReceiptPositions().stream())
            .collect(Collectors.toMap(ReceiptPosition::getProductName,
                position -> new SaleReportPosition(position.getProductName(),
                    position.getUom(),
                    position.getQuantity(),
                    position.getTotal()), (a, b) -> {
                    a.setQuantity(a.getQuantity().add(b.getQuantity()));
                    a.setTotal(a.getTotal().add(b.getTotal()));
                    return a;
                }
            ));
        return new SaleReport((LocalDate) group.getOne().getHeaders().get("report-date"),
            List.copyOf(reportPositionMap.values()));
    }
}
