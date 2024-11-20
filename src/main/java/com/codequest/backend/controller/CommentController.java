package com.codequest.backend.controller;

import com.codequest.backend.entity.Board;
import com.codequest.backend.entity.Comment;
import com.codequest.backend.repository.BoardRepository;
import com.codequest.backend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.7:3000"})
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    // 특정 게시글의 댓글 조회
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<Comment>> getCommentsByBoardId(@PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));
        List<Comment> comments = commentRepository.findByBoard(board);
        return ResponseEntity.ok(comments);
    }

    // 댓글 작성
    @PostMapping("/board/{boardId}")
    public ResponseEntity<Comment> addCommentToBoard(@PathVariable Long boardId, @RequestBody Comment comment) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));
        comment.setBoard(board);
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok(savedComment);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody Comment updatedCommentDetails) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
        existingComment.setContent(updatedCommentDetails.getContent());
        existingComment.setLikes(updatedCommentDetails.getLikes());
        Comment updatedComment = commentRepository.save(existingComment);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
        commentRepository.delete(comment);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
