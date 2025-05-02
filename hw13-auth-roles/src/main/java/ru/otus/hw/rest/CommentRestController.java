package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.domain.service.CommentService;
import ru.otus.hw.rest.api.CommentsApi;
import ru.otus.hw.rest.mapper.CommentRestMapper;
import ru.otus.hw.rest.model.CommentDto;
import ru.otus.hw.rest.model.ModifyCommentDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentRestController implements CommentsApi {

    private final CommentService service;

    private final CommentRestMapper mapper;

    @Override
    public ResponseEntity<CommentDto> createComment(CommentDto commentDto) {
        return ResponseEntity.ok(mapper.toDto(service.save(mapper.map(commentDto))));
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long id) {
        service.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CommentDto>> getAllComments(Long bookId) {
        return ResponseEntity.ok(mapper.toDto(service.findByBookId(bookId)));
    }

    @Override
    public ResponseEntity<CommentDto> getCommentById(Long id) {
        Optional<ru.otus.hw.domain.model.Comment> comment = service.findById(Optional.ofNullable(id).orElse(0L));
        return comment.map(value -> ResponseEntity.ok(mapper.toDto(value)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<CommentDto> updateComment(Long id, ModifyCommentDto updateCommentRequestDto) {
        CommentDto comment = new CommentDto();
        comment.setId(id);
        comment.setContent(updateCommentRequestDto.getContent());
        comment.setBook(updateCommentRequestDto.getBook());
        return ResponseEntity.ok(mapper.toDto(service.save(mapper.map(comment))));
    }
}
