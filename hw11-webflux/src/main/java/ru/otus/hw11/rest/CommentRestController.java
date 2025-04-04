package ru.otus.hw11.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw11.domain.service.CommentService;
import ru.otus.hw11.rest.api.CommentsApi;
import ru.otus.hw11.rest.mapper.CommentRestMapper;
import ru.otus.hw11.rest.model.CommentDto;
import ru.otus.hw11.rest.model.ModifyCommentDto;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentRestController implements CommentsApi {

    private final CommentService service;

    private final CommentRestMapper mapper;

    @Override
    public Mono<CommentDto> createComment(Mono<CommentDto> commentDto, ServerWebExchange exchange) {
        return service.save(commentDto.map(mapper::map)).map(mapper::map);
    }

    @Override
    public Mono<Void> deleteComment(Long id, ServerWebExchange exchange) {
        return service.delete(id);
    }

    @Override
    public Flux<CommentDto> getAllComments(Long bookId, ServerWebExchange exchange) {
        return service.findByBookId(bookId).map(mapper::map);
    }

    @Override
    public Mono<CommentDto> getCommentById(Long id, ServerWebExchange exchange) {
        return service.findById(id).map(mapper::map);
    }

    @Override
    public Mono<CommentDto> updateComment(Long id, Mono<ModifyCommentDto> modifyCommentDto, ServerWebExchange exchange) {
        return service.save(modifyCommentDto.map(dto -> {
            CommentDto comment = new CommentDto();
            comment.setId(id);
            comment.setContent(dto.getContent());
            comment.setBook(dto.getBook());
            return comment;
        }).map(mapper::map)).map(mapper::map);
    }
}
