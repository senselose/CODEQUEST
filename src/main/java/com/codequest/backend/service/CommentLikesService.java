package com.codequest.backend.service;

import com.codequest.backend.entity.Comment;
import com.codequest.backend.entity.CommentLikes;
import com.codequest.backend.entity.User;
import com.codequest.backend.repository.CommentLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikesService {

    private final CommentLikesRepository commentLikesRepository;

    // 좋아요 토글
    public String toggleLike(Comment comment, User user) {
        Optional<CommentLikes> existingLike = commentLikesRepository.findByCommentAndUser(comment, user);

        if (existingLike.isPresent()) {
            commentLikesRepository.delete(existingLike.get());
            return "좋아요 취소";
        } else {
            CommentLikes like = new CommentLikes();
            like.setComment(comment);
            like.setUser(user);
            commentLikesRepository.save(like);
            return "좋아요";
        }
    }

    // 댓글 좋아요 수 조회
    public int getLikeCount(Comment comment) {
        return commentLikesRepository.countByComment(comment);
    }

    // 사용자 좋아요 여부 확인
    public boolean isLiked(Comment comment, User user) {
        return commentLikesRepository.findByCommentAndUser(comment, user).isPresent();
    }
}
