package ru.otus.hw.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import ru.otus.hw.model.SaleReport;

/**
 * Выводит в лог сформированный отчет
 */
@MessageEndpoint
@Slf4j
public class SaleReportEndPoints {

    @ServiceActivator(inputChannel = "completedReportChannel")
    public Message<SaleReport> sendReportToLog(Message<SaleReport> report) {
        log.info("Сформирован отчет \n {}", report.getPayload());
        return report;
    }
}
