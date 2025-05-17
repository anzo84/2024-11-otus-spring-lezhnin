package ru.otus.hw.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ReleaseStrategy;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import ru.otus.hw.model.Receipt;
import ru.otus.hw.model.ReceiptPosition;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализует агрегатор по формированию чека из позиций на основе аннотаций
 */
@MessageEndpoint
@Slf4j
public class ReceiptEndPoints {

    @Aggregator(inputChannel = "receiptPositionsOperations", outputChannel = "completedReceiptsChannel")
    public Message<Receipt> aggregateReceipt(List<ReceiptPosition> positions) {
        Receipt receipt = new Receipt();
        receipt.setId(positions.get(0).getReceiptId());
        receipt.setReceiptPositions(positions);
        receipt.setDateTime(LocalDateTime.now());
        return MessageBuilder.withPayload(receipt)
            .setHeader("report-date", receipt.getDateTime().toLocalDate())
            .build();
    }

    @CorrelationStrategy
    public String correlateByReceiptId(ReceiptPosition position) {
        return position.getReceiptId();
    }

    @ReleaseStrategy
    public boolean releaseCheck(List<Message<ReceiptPosition>> messages) {
        return messages.stream().anyMatch(msg -> msg.getPayload().getIsLast());
    }

    @ServiceActivator(inputChannel = "completedReceiptsChannel", outputChannel = "reportChannel")
    public Message<Receipt> sendToLog(Message<Receipt> receipt) {
        log.info("Сформирован чек \n {}", receipt.getPayload());
        return receipt;
    }
}