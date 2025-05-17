package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import ru.otus.hw.integration.ReceiptMessageGroupProcessor;
import ru.otus.hw.model.CloseDay;
import ru.otus.hw.model.ReceiptPosition;

@Configuration
public class ReceiptSaleReportIntegration {

    /**
     * @return Входной канал операций точки продаж
     */
    @Bean
    public MessageChannel inputPointOfSalesOperations() {
        return new DirectChannel();
    }

    /**
     * @return Канал обработки чеков позиций чеков
     */
    @Bean
    public MessageChannel receiptPositionsOperations() {
        return new DirectChannel();
    }

    /**
     * @return Канал собранных чеков продажи
     */
    @Bean
    public MessageChannel completedReceiptsChannel() {
        return new DirectChannel();
    }

    /**
     * @return Канал для данных формирования отчета
     */
    @Bean
    public MessageChannel reportChannel() {
        return new DirectChannel();
    }

    /**
     * @return Канал сформированных отчетов
     */
    @Bean
    public MessageChannel completedReportChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public IntegrationFlow inputFlow() {
        return IntegrationFlow.from(inputPointOfSalesOperations())
            .<Object, Class<?>>route(Object::getClass, mapping -> mapping
                .subFlowMapping(ReceiptPosition.class, sf -> sf.channel(receiptPositionsOperations()))
                .subFlowMapping(CloseDay.class,
                    sf -> sf
                        .enrichHeaders(h -> h
                            .headerExpression("report-date", "payload.date")
                            .headerExpression("close-day", "true"))
                        .channel(reportChannel()))
                .defaultOutputChannel("defaultChannel")
            )
            .get();
    }

    @Bean
    public IntegrationFlow reportFlow() {
        return IntegrationFlow.from(reportChannel())
            .aggregate(
                agg -> agg
                    .correlationExpression("headers['report-date']")
                    .releaseExpression("messages.?[headers['close-day'] == true].size() > 0")
                    .outputProcessor(new ReceiptMessageGroupProcessor())
                    .expireGroupsUponCompletion(true)
            )
            .channel(completedReportChannel())
            .get();
    }
}
