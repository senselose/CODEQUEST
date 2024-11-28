package com.codequest.backend.entity;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "boardId")
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId; // 게시글 ID

    @Column(nullable = false, length = 50)
    private String nickname; // 닉네임

    @Column(nullable = false, length = 255)
    private String title; // 제목

    @ToString.Exclude
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 컨텐츠

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성일자

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer views = 0; // 조회수

    @Column(length = 50)
    private String category; // 카테고리

    @Column(length = 255)
    private String hashtags; // 해시태그 (콤마로 구분)

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isHidden = false; // 숨김 토글

    @Column(length = 255)
    private String location; // 위치 정보

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardLikes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    // @Column(columnDefinition = "INT DEFAULT 0")
    // private int likesCount; // 좋아요 개수

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

}

// package com.codequest.backend.entity;

// import javax.persistence.*;

// import com.fasterxml.jackson.annotation.JsonIdentityInfo;
// import com.fasterxml.jackson.annotation.JsonManagedReference;
// import com.fasterxml.jackson.annotation.ObjectIdGenerators;

// import java.time.LocalDateTime;
// import lombok.Data;
// import java.util.List;

// @Entity
// @JsonIdentityInfo(
// generator = ObjectIdGenerators.PropertyGenerator.class,
// property = "id"
// )
// @Data
// public class Board {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long boardId; // 게시글 ID

// @Column(nullable = false, length = 50)
// private String nickname; // 닉네임

// @Column(nullable = false, length = 255)
// private String title; // 제목

// @Column(columnDefinition = "TEXT", nullable = false)
// private String content; // 컨텐츠

// @Column(name = "created_at", updatable = false)
// private LocalDateTime createdAt = LocalDateTime.now(); // 생성일자

// @Column(columnDefinition = "INT DEFAULT 0")
// private Integer views = 0; // 조회수

// @Column(length = 50)
// private String category; // 카테고리

// @Column(length = 255)
// private String hashtags; // 해시태그 (콤마로 구분)

// @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
// private Boolean isHidden = false; // 숨김 토글

// @Column(length = 255)
// private String location; // 위치 정보

// @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval =
// true)
// @JsonManagedReference
// private List<BoardLikes> likes;

// @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval =
// true)
// @JsonManagedReference
// private List<Comment> comments; // 게시글에 달린 댓글

// }
