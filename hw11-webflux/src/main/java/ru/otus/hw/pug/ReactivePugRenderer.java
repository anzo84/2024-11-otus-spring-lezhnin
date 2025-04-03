package ru.otus.hw.pug;

import de.neuland.pug4j.PugConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReactivePugRenderer {

    private final PugConfiguration pugConfiguration;

    public Mono<DataBuffer> render(String templateName, Map<String, Object> model, DataBufferFactory bufferFactory) {
        return Mono.fromCallable(() -> {
                // Блокирующий вызов pug4j выполняется в отдельном потоке
                String html = pugConfiguration.renderTemplate(
                    pugConfiguration.getTemplate(templateName),
                    model
                );
                return bufferFactory.wrap(html.getBytes(StandardCharsets.UTF_8));
            })
            .subscribeOn(Schedulers.boundedElastic()); // Выполнение в отдельном потоке
    }
}