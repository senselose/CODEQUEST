package com.codequest.backend.entity;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId; // 이미지 ID

    @Column(nullable = false)
    private String filePath; // 파일 경로

    @Column(nullable = false)
    private String fileName; // 파일 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
}