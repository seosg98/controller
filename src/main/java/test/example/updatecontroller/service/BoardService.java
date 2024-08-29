package org.example.demoeight.service;

import lombok.RequiredArgsConstructor;
import org.example.demoeight.dto.board.request.BoardSaveRequestDto;
import org.example.demoeight.dto.board.request.BoardUpdateRequestDto;
import org.example.demoeight.dto.board.response.BoardSaveResponseDto;
import org.example.demoeight.dto.board.response.BoardSimpleResponseDto;
import org.example.demoeight.dto.board.response.BoardUpdateResponseDto;
import org.example.demoeight.entity.Board;
import org.example.demoeight.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardSaveResponseDto saveBoard(BoardSaveRequestDto boardSaveRequestDto) {
        Board newBoard = new Board(
                boardSaveRequestDto.getTitle(),
                boardSaveRequestDto.getContents()
        );
        Board savedBoard = boardRepository.save(newBoard);

        return new BoardSaveResponseDto(savedBoard.getId(), savedBoard.getTitle(), savedBoard.getContents());
    }

    public Page<BoardSimpleResponseDto> getBoards(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Board> boards = boardRepository.findAllByOrderByModifiedAtDesc(pageable);

        return boards.map(board -> new BoardSimpleResponseDto(
                board.getId(),
                board.getTitle(),
                board.getComments()
        ));
//        List<Board> boardList = boardRepository.findAll();
//
//        List<BoardSimpleResponseDto> dtoList = new ArrayList<>();
//        for (Board board : boardList) {
//            List<Comment> commentList = board.getComments();
//            List<CommentResponseDto> commentDtoList = new ArrayList<>();
//
//            for (Comment comment : commentList) {
//                commentDtoList.add(new CommentResponseDto(comment.getId(), comment.getContents()));
//            }
//
//            BoardSimpleResponseDto dto = new BoardSimpleResponseDto(
//                    board.getId(),
//                    board.getTitle(),
//                    commentDtoList
//            );
//            dtoList.add(dto);
//        }
//        return dtoList;
    }

    @Transactional
    public BoardUpdateResponseDto updateBoard(Long boardId, BoardUpdateRequestDto boardUpdateRequestDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("아 보드 없다고"));
        board.update(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContents());

        return new BoardUpdateResponseDto(board.getId(), board.getTitle(), board.getContents());
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        if (!boardRepository.existsById(boardId)) {
            throw new NullPointerException("아 보드 없다고");
        }

        boardRepository.deleteById(boardId);
    }
}
