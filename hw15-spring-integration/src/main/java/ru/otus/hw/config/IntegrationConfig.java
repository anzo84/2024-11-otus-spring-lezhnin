package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.hw.service.ShopService;

import java.time.LocalDateTime;

@Configuration
@EnableIntegration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> receiptChannel() {
        return MessageChannels.queue(5);
    }

    @Bean
    public MessageChannelSpec<?, ?> reportChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> aggregatedReceiptsChannel() {
        return MessageChannels.queue();
    }

    @Bean
    public IntegrationFlow receiptProcessingFlow(ShopService shopService) {
        return IntegrationFlow.from(receiptChannel())
            .handle(shopService, "makeReceipt")
            .channel(aggregatedReceiptsChannel())
            .get();
    }

    @Bean
    public IntegrationFlow reportGenerationFlow(ShopService shopService) {
        return IntegrationFlow.from(aggregatedReceiptsChannel())
            .aggregate(agg -> agg
                .correlationStrategy(m -> {
                    return m.getHeaders().get("receiptDate", LocalDateTime.class).toLocalDate();
                })
                .releaseStrategy(g -> g.size() >= 100)
                .expireGroupsUponCompletion(true)
                .groupTimeout(30000))
            .handle(shopService, "makeSaleReport")
            .channel(reportChannel())
            .get();
    }

}