package com.codequest.backend.entity;

import lombok.Data;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId; // 댓글 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @JsonBackReference(value = "board-comments") // 참조 구분
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id") // 부모 댓글 ID (null이면 최상위 댓글)
    @JsonBackReference(value = "parent-child-comments") // 참조 구분
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "parent-child-comments") // 참조 구분
    private List<Comment> childComments = new ArrayList<>(); // 대댓글 리스트

    @Column(nullable = false, length = 50)
    private String nickname; // 작성자 닉네임

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 댓글 내용

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 작성일자

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer likes = 0; // 댓글 좋아요 수
}


// package com.codequest.backend.entity;

// import lombok.Data;
// import javax.persistence.*;

// import com.fasterxml.jackson.annotation.JsonBackReference;
// import com.fasterxml.jackson.annotation.JsonManagedReference;

// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;

// @Entity
// @Data
// public class Comment {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long commentId; // 댓글 ID

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "board_id", nullable = false)
//     @JsonBackReference(value = "board-comments") // 참조 구분
//     private Board board;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "parent_comment_id") // 대댓글의 부모 댓글 ID
//     @JsonBackReference(value = "parent-child-comments") // 참조 구분
//     private Comment parentComment;

//     @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
//     @JsonManagedReference(value = "parent-child-comments") // 참조 구분
//     private List<Comment> childComments = new ArrayList<>(); // 대댓글 리스트

//     @Column(nullable = false, length = 50)
//     private String nickname; // 작성자 닉네임

//     @Column(columnDefinition = "TEXT", nullable = false)
//     private String content; // 댓글 내용

//     @Column(name = "created_at", updatable = false)
//     private LocalDateTime createdAt = LocalDateTime.now(); // 작성일자

//     @Column(columnDefinition = "INT DEFAULT 0")
//     private Integer likes = 0; // 댓글 좋아요 수
// }
