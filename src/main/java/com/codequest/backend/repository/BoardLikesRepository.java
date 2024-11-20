package com.codequest.backend.repository;

import com.codequest.backend.entity.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {

    // 수정된 메서드: Board 엔티티의 기본 키 이름 사용
    Optional<BoardLikes> findByUserIdAndBoard_BoardId(String userId, Long boardId);

    // 특정 게시글의 좋아요 수
    Long countByBoard_BoardId(Long boardId);
}

