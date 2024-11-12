package com.codequest.backend.controller;

import com.codequest.backend.entity.Board;
import com.codequest.backend.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    // 모든 게시글 조회
    @GetMapping
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    // 게시글 생성
    @PostMapping
    public Board createBoard(@RequestBody Board board) {
        return boardRepository.save(board);
    }

    // 특정 게시글 조회
    @GetMapping("/{id}")
    public Board getBoardById(@PathVariable Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다: " + id));
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public Board updateBoard(@PathVariable Long id, @RequestBody Board boardDetails) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다: " + id));

        board.setTitle(boardDetails.getTitle());
        board.setContent(boardDetails.getContent());
        board.setCategory(boardDetails.getCategory());
        board.setIsHidden(boardDetails.getIsHidden());
        board.setLocation(boardDetails.getLocation());
        board.setLikes(boardDetails.getLikes());
        board.setViews(board.getViews() + 1); // 조회수 1 증가
        return boardRepository.save(board);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다: " + id));
        boardRepository.delete(board);
    }
}
