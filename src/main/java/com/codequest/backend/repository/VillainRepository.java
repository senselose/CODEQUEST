package com.codequest.backend.repository;

import com.codequest.backend.entity.Villain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VillainRepository extends JpaRepository<Villain, Long> {
    Villain findByName(String name); // 빌런 이름으로 조회
}