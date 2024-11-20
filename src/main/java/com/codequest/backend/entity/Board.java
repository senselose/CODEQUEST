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
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "boardId"
)
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
//     generator = ObjectIdGenerators.PropertyGenerator.class,
//     property = "id"
// )
// @Data
// public class Board {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long boardId; // 게시글 ID

//     @Column(nullable = false, length = 50)
//     private String nickname; // 닉네임

//     @Column(nullable = false, length = 255)
//     private String title; // 제목

//     @Column(columnDefinition = "TEXT", nullable = false)
//     private String content; // 컨텐츠

//     @Column(name = "created_at", updatable = false)
//     private LocalDateTime createdAt = LocalDateTime.now(); // 생성일자

//     @Column(columnDefinition = "INT DEFAULT 0")
//     private Integer views = 0; // 조회수

//     @Column(length = 50)
//     private String category; // 카테고리

//     @Column(length = 255)
//     private String hashtags; // 해시태그 (콤마로 구분)

//     @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
//     private Boolean isHidden = false; // 숨김 토글

//     @Column(length = 255)
//     private String location; // 위치 정보

//     @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
//     @JsonManagedReference
//     private List<BoardLikes> likes;

//     @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
//     @JsonManagedReference
//     private List<Comment> comments; // 게시글에 달린 댓글


// }


// package com.codequest.backend.entity;

// import javax.persistence.*;
// import java.time.LocalDateTime;
// import lombok.Data;
// import java.util.List;

// @Entity
// public class Board {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long boardId; // 게시글 ID

//     @Column(nullable = false, length = 50)
//     private String nickname; // 닉네임

//     @Column(nullable = false, length = 255)
//     private String title; // 제목

//     @Column(columnDefinition = "TEXT", nullable = false)
//     private String content; // 컨텐츠

//     @Column(name = "created_at", updatable = false)
//     private LocalDateTime createdAt = LocalDateTime.now(); // 생성일자

//     @Column(columnDefinition = "INT DEFAULT 0")
//     private Integer views = 0; // 조회수

//     @Column(length = 50)
//     private String category; // 카테고리

//     @Column(length = 255)
//     private String hashtags; // 해시태그 (콤마로 구분)

//     @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
//     private Boolean isHidden = false; // 숨김 토글

//     @Column(length = 255)
//     private String location; // 위치 정보

//     @Column(columnDefinition = "TEXT")
//     @Convert(converter = CommentListConverter.class)
//     private List<Comment> comments;

//     @Column(columnDefinition = "INT DEFAULT 0")
//     private Integer likes = 0; // 좋아요 수

//     // Getters and Setters
//     public Long getBoardId() {
//         return boardId;
//     }

//     public void setBoardId(Long boardId) {
//         this.boardId = boardId;
//     }

//     public String getNickname() {
//         return nickname;
//     }

//     public void setNickname(String nickname) {
//         this.nickname = nickname;
//     }

//     public String getTitle() {
//         return title;
//     }

//     public void setTitle(String title) {
//         this.title = title;
//     }

//     public String getContent() {
//         return content;
//     }

//     public void setContent(String content) {
//         this.content = content;
//     }

//     public LocalDateTime getCreatedAt() {
//         return createdAt;
//     }

//     public void setCreatedAt(LocalDateTime createdAt) {
//         this.createdAt = createdAt;
//     }

//     public Integer getViews() {
//         return views;
//     }

//     public void setViews(Integer views) {
//         this.views = views;
//     }

//     public String getCategory() {
//         return category;
//     }

//     public void setCategory(String category) {
//         this.category = category;
//     }

//     public String getHashtags() {
//         return hashtags;
//     }

//     public void setHashtags(String hashtags) {
//         this.hashtags = hashtags;
//     }

//     public Boolean getIsHidden() {
//         return isHidden;
//     }

//     public void setIsHidden(Boolean isHidden) {
//         this.isHidden = isHidden;
//     }

//     public String getLocation() {
//         return location;
//     }

//     public void setLocation(String location) {
//         this.location = location;
//     }

//     public List<Comment> getComments() {
//         return comments;
//     }

//     public void setComments(List<Comment> comments) {
//         this.comments = comments;
//     }

//     public Integer getLikes() {
//         return likes;
//     }

//     public void setLikes(Integer likes) {
//         this.likes = likes;
//     }

 
    
// }
