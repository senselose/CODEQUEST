package com.codequest.backend.repository;

import com.codequest.backend.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    // 추가적인 쿼리 메서드가 필요하면 여기에 정의
}
