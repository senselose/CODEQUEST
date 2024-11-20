package com.codequest.backend.controller;

import com.codequest.backend.entity.Board;
import com.codequest.backend.entity.BoardLikes;
import com.codequest.backend.repository.BoardLikesRepository;
import com.codequest.backend.repository.BoardRepository;
import com.codequest.backend.service.BoardService;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.7:3000"})
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardLikesRepository boardLikesRepository; // 좋아요 리포지토리 추가

    // 검색 및 모든 게시글 조회 (통합)
    @GetMapping
    public Page<Board> getBoards(
            @RequestParam(value = "search", required = false) String search,
            Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return boardService.searchBoards(search, pageable);
        } else {
            return boardService.getAllBoards(pageable);
        }
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

        // 제목, 내용 등 기본 정보 업데이트
        board.setTitle(boardDetails.getTitle());
        board.setContent(boardDetails.getContent());
        board.setCategory(boardDetails.getCategory());
        board.setIsHidden(boardDetails.getIsHidden());
        board.setLocation(boardDetails.getLocation());
        board.setLikes(boardDetails.getLikes());

        // 조회수 증가
        board.setViews(board.getViews() + 1);

        return boardRepository.save(board);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다: " + id));
        boardRepository.delete(board);
    }

    // 조회수 증가 API
    @PutMapping("/{id}/views")
    public Board incrementViews(@PathVariable Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        board.setViews(board.getViews() + 1);
        return boardRepository.save(board);
    }

    // 좋아요 증가 API
    @PostMapping("/{boardId}/like")
public ResponseEntity<String> toggleLike(
        @PathVariable String boardId,
        @RequestBody Map<String, String> payload) { // Long -> String
    String userId = payload.get("userId");

    // 기존 로직 수정 없이 사용
    Board board = boardRepository.findById(Long.parseLong(boardId))
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

    Optional<BoardLikes> existingLike = boardLikesRepository.findByUserIdAndBoard_BoardId(userId, Long.parseLong(boardId));

    if (existingLike.isPresent()) {
        boardLikesRepository.delete(existingLike.get());
        return ResponseEntity.ok("좋아요 취소");
    } else {
        BoardLikes newLike = new BoardLikes();
        newLike.setUserId(userId);
        newLike.setBoard(board);
        boardLikesRepository.save(newLike);
        return ResponseEntity.ok("좋아요");
    }
}




    @GetMapping("/{boardId}/like-status")
    public ResponseEntity<Boolean> checkLikeStatus(
        @PathVariable Long boardId,
        @RequestParam String userId) {
    boolean liked = boardLikesRepository
            .findByUserIdAndBoard_BoardId(userId, boardId)
            .isPresent();
    return ResponseEntity.ok(liked);
}

}



