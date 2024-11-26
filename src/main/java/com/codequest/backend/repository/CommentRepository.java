package com.codequest.backend.repository;

import com.codequest.backend.entity.Board;
import com.codequest.backend.entity.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 댓글 조회
    @EntityGraph(attributePaths = {"childComments"})
    List<Comment> findByBoard(Board board);
    List<Comment> findByParentComment(Comment parentComment); // 대댓글 조회
    // Page<Comment> findByBoardIdAndParentCommentIsNull(Long boardId, Pageable pageable);
    Page<Comment> findByBoardAndParentCommentIsNull(Board board, Pageable pageable);

}
