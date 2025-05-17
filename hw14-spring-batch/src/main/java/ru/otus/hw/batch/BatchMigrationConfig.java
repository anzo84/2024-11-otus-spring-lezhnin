package ru.otus.hw.batch;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.jpa.model.Receipt;

import java.util.stream.Collectors;

@Configuration
public class BatchMigrationConfig {

    private static final int CHUNK_SIZE = 3;

    private static final String JOB_NAME = "jpaToMongoMigrationJob";

    private static final String STEP_NAME = "migrateReceiptsStep";

    @Bean
    public JpaPagingItemReader<Receipt> jpaReceiptReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<Receipt>()
            .name("receiptReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("SELECT r FROM Receipt r LEFT JOIN FETCH r.positions")
            .pageSize(CHUNK_SIZE)
            .build();
    }

    @Bean
    public ItemProcessor<Receipt, ru.otus.hw.mongo.model.Receipt> receiptProcessor() {
        return receipt -> {
            ru.otus.hw.mongo.model.Receipt mongoReceipt = new ru.otus.hw.mongo.model.Receipt();
            mongoReceipt.setDateTime(receipt.getDateTime());

            if (receipt.getPositions() != null) {
                mongoReceipt.setPositions(
                    receipt.getPositions().stream()
                        .map(pos -> new ru.otus.hw.mongo.model.Position(
                            String.valueOf(pos.getId()),
                            pos.getProductName(),
                            pos.getUom(),
                            pos.getQuantity(),
                            pos.getPrice()
                        ))
                        .collect(Collectors.toList())
                );
            }
            return mongoReceipt;
        };
    }

    @Bean
    public ItemWriter<ru.otus.hw.mongo.model.Receipt> mongoReceiptWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<ru.otus.hw.mongo.model.Receipt>()
            .template(mongoTemplate)
            .collection("receipts")
            .build();
    }

    @Bean
    public Step migrateReceiptsStep(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager,
        ItemReader<Receipt> reader,
        ItemProcessor<Receipt, ru.otus.hw.mongo.model.Receipt> processor,
        ItemWriter<ru.otus.hw.mongo.model.Receipt> writer
    ) {
        return new StepBuilder(STEP_NAME, jobRepository)
            .<Receipt, ru.otus.hw.mongo.model.Receipt>chunk(CHUNK_SIZE, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }

    @Bean
    public Job jpaToMongoMigrationJob(JobRepository jobRepository, Step migrateReceiptsStep) {
        return new JobBuilder(JOB_NAME, jobRepository)
            .start(migrateReceiptsStep)
            .build();
    }
}