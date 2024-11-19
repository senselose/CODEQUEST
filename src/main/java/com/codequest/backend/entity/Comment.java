package com.codequest.backend.entity;

import lombok.Data;

@Data
public class Comment {
    private Long id;           // 댓글 ID
    private String nickname;   // 작성자 닉네임
    private String content;    // 댓글 내용
    private String createdAt;  // 작성 시간
}