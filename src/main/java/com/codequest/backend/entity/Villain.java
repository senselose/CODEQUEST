package com.codequest.backend.entity;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class Villain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 설정
    private Long villainId; // 빌런 ID (1씩 증가)

    @Column(nullable = false, unique = true)
    private Long boardId; // 보드 ID (중복 금지)

    @Column(nullable = false, unique = true)
    private String villainName; // 빌런 이름 (중복 금지)

    @Column(nullable = false)
    private String category; // 카테고리 (문자열로 저장)

    @Column(nullable = false)
    private String fileName; // 이미지 파일 이름

    @Column(nullable = false)
    private String filePath; // 이미지 파일 경로 (상대 경로)
}
