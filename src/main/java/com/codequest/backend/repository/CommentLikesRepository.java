package com.codequest.backend.repository;

import com.codequest.backend.entity.CommentLikes;
import com.codequest.backend.entity.Board;
import com.codequest.backend.entity.Comment;
import com.codequest.backend.entity.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, Long> {
    Optional<CommentLikes> findByCommentAndUser(Comment comment, User user); // 특정 댓글과 사용자 조회
    int countByComment(Comment comment); // 댓글의 좋아요 수 계산
    void deleteByCommentAndUser(Comment comment, User user); // 특정 댓글과 사용자 좋아요 삭제

    
}
