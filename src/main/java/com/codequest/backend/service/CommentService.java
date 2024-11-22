package com.codequest.backend.service;

import com.codequest.backend.entity.Board;
import com.codequest.backend.entity.Comment;
import com.codequest.backend.repository.BoardRepository;
import com.codequest.backend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository; // BoardRepository 추가

    public Comment addComment(Long boardId, String nickname, String content) {
        // 게시글 존재 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));

        Comment comment = new Comment();
        comment.setBoard(board); // Board 객체 설정
        comment.setNickname(nickname);
        comment.setContent(content);
        return commentRepository.save(comment);
    }
    public Comment addReply(Long parentCommentId, String nickname, String content) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다: " + parentCommentId));

        Comment reply = new Comment();
        reply.setParentComment(parentComment);
        reply.setBoard(parentComment.getBoard());
        reply.setNickname(nickname);
        reply.setContent(content);
        return commentRepository.save(reply);
    }

    // 댓글 조회 (페이징)
    // public Page<Comment> getCommentsByBoardId(Long boardId, int page, int size) {
    //     Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    //     return commentRepository.findByBoardIdAndParentCommentIsNull(boardId, pageable);
    // }
    public Page<Comment> getCommentsByBoardId(Long boardId, int page, int size) {
        // Board 객체를 가져오기
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));
        // 페이징 처리
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        // 수정된 repository 메서드 사용
        return commentRepository.findByBoardAndParentCommentIsNull(board, pageable);
    }
    
}


// @Service
// public class CommentService {

//     @Autowired
//     private CommentRepository commentRepository;

//     // 대댓글 저장
//     public Comment addReply(Long parentCommentId, String nickname, String content) {
//         Comment parentComment = commentRepository.findById(parentCommentId)
//                 .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다: " + parentCommentId));

//         Comment reply = new Comment();
//         reply.setParentComment(parentComment);
//         reply.setNickname(nickname);
//         reply.setContent(content);
//         reply.setBoard(parentComment.getBoard()); // 부모 댓글의 게시글 연결

//         return commentRepository.save(reply);
//     }
// }

