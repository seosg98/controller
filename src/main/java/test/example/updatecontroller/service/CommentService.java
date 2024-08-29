package org.example.demoeight.service;

import lombok.RequiredArgsConstructor;
import org.example.demoeight.dto.comment.request.CommentSaveRequestDto;
import org.example.demoeight.dto.comment.response.CommentResponseDto;
import org.example.demoeight.dto.comment.response.CommentSaveResponseDto;
import org.example.demoeight.entity.Board;
import org.example.demoeight.entity.Comment;
import org.example.demoeight.repository.BoardRepository;
import org.example.demoeight.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentSaveResponseDto saveComment(Long boardId, CommentSaveRequestDto commentSaveRequestDto) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("아 보드 없다고"));

        Comment newComment = new Comment(commentSaveRequestDto.getContents(), board);
        Comment savedComment = commentRepository.save(newComment);

        return new CommentSaveResponseDto(savedComment.getId(), savedComment.getContents());
    }

    public List<CommentResponseDto> getComments() {
        List<Comment> commentList = commentRepository.findAll();

        List<CommentResponseDto> dtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            dtoList.add(new CommentResponseDto(comment.getId(), comment.getContents()));
        }
        return dtoList;
    }
}
