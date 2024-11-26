package com.codequest.backend.repository;

import com.codequest.backend.entity.Board;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 필요한 경우 추가 쿼리 메서드를 정의할 수 있습니다.
    // 검색어가 제목 또는 내용에 포함된 게시글 조회
    
    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    Page<Board> findByCreatedAtAfter(LocalDateTime startDate, Pageable pageable);

    
    @Query("SELECT b FROM Board b LEFT JOIN b.likes l GROUP BY b ORDER BY COUNT(l) DESC")
    Page<Board> findAllByLikesCount(Pageable pageable);

}

