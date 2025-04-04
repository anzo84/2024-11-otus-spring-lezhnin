package ru.otus.hw11.pug;

import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.reactive.result.view.View;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PugView implements View {

    private static final List<MediaType> SUPPORTED_MEDIA_TYPES =
        Arrays.asList(MediaType.TEXT_HTML, MediaType.APPLICATION_XHTML_XML, // HTML
            MediaType.APPLICATION_XML, MediaType.TEXT_XML,        // XML
            MediaType.APPLICATION_RSS_XML,                        // RSS
            MediaType.APPLICATION_ATOM_XML,                       // ATOM
            new MediaType("application", "javascript"),           // JAVASCRIPT
            new MediaType("application", "ecmascript"),           //
            new MediaType("text", "javascript"),                  //
            new MediaType("text", "ecmascript"),                  //
            MediaType.APPLICATION_JSON,                           // JSON
            new MediaType("text", "css"),                         // CSS
            MediaType.TEXT_PLAIN,                                 // TEXT
            MediaType.TEXT_EVENT_STREAM);                        // SERVER-SENT EVENTS (SSE)

    private final String templateName;

    private final ReactivePugRenderer renderer;

    public PugView(String templateName, ReactivePugRenderer renderer) {
        this.templateName = templateName;
        this.renderer = renderer;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return SUPPORTED_MEDIA_TYPES;
    }

    @Override
    public Mono<Void> render(@Nullable Map<String, ?> model, @Nullable MediaType contentType,
                             ServerWebExchange exchange) {

        return renderer.render(templateName, (Map<String, Object>) model,
                exchange.getResponse().bufferFactory())
            .flatMap(buffer -> exchange.getResponse().writeWith(Mono.just(buffer)));
    }
}