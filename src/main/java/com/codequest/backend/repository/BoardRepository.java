package com.codequest.backend.repository;

import com.codequest.backend.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 필요한 경우 추가 쿼리 메서드를 정의할 수 있습니다.
}
