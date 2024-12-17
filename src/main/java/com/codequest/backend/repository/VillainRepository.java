package com.codequest.backend.repository;

import com.codequest.backend.entity.Villain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VillainRepository extends JpaRepository<Villain, Long> {
    // boardId의 존재 여부를 확인하는 메서드:
    boolean existsByBoardId(Long boardId);

    List<Villain> findByCategory(String category);
}
