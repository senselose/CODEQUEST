package com.codequest.backend.repository;

import com.codequest.backend.entity.Board;
import com.codequest.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 댓글 조회
    List<Comment> findByBoard(Board board);
}
