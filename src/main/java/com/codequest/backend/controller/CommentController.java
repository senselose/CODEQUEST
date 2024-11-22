// package com.codequest.backend.controller;

// import com.codequest.backend.entity.Board;
// import com.codequest.backend.entity.Comment;
// import com.codequest.backend.entity.CommentLikes;
// import com.codequest.backend.entity.User;
// import com.codequest.backend.repository.BoardRepository;
// import com.codequest.backend.repository.CommentRepository;
// import com.codequest.backend.service.CommentLikesService;
// import com.codequest.backend.service.UserService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/api/comments")
// @CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.7:3000"})
// public class CommentController {

//     @Autowired
//     private CommentRepository commentRepository;

//     @Autowired
//     private BoardRepository boardRepository;

//     @Autowired
//     private CommentLikesService commentLikesService;

//     @Autowired
//     private UserService userService;

//     // 특정 게시글의 댓글 조회
//     @GetMapping("/board/{boardId}")
//     public ResponseEntity<List<Map<String, Object>>> getCommentsByBoardId(
//             @PathVariable Long boardId,
//             @RequestParam String userId) { // userId를 항상 포함하도록 설정
//         Board board = boardRepository.findById(boardId)
//                 .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));
//         List<Comment> comments = commentRepository.findByBoard(board);

//         List<Map<String, Object>> commentsWithLikeStatus = comments.stream()
//                 .map(comment -> {
//                     Map<String, Object> commentData = new HashMap<>();
//                     commentData.put("comment", comment);
//                     commentData.put("liked", commentLikesService.isLiked(comment, userService.findById(userId)));
//                     commentData.put("likeCount", commentLikesService.getLikeCount(comment));
//                     return commentData;
//                 })
//                 .collect(Collectors.toList());

//         return ResponseEntity.ok(commentsWithLikeStatus);
//     }


//     // 댓글 작성
//     @PostMapping("/board/{boardId}")
//     public ResponseEntity<Comment> addCommentToBoard(@PathVariable Long boardId, @RequestBody Comment comment) {
//         Board board = boardRepository.findById(boardId)
//                 .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));
//         comment.setBoard(board);
//         Comment savedComment = commentRepository.save(comment);
//         return ResponseEntity.ok(savedComment);
//     }

//     // 댓글 수정
//     @PutMapping("/{commentId}")
//     public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody Comment updatedCommentDetails) {
//         Comment existingComment = commentRepository.findById(commentId)
//                 .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
//         existingComment.setContent(updatedCommentDetails.getContent());
//         existingComment.setLikes(updatedCommentDetails.getLikes());
//         Comment updatedComment = commentRepository.save(existingComment);
//         return ResponseEntity.ok(updatedComment);
//     }

//     // 댓글 삭제
//     @DeleteMapping("/{commentId}")
//     public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
//         Comment comment = commentRepository.findById(commentId)
//                 .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
//         commentRepository.delete(comment);
//         return ResponseEntity.ok("댓글이 삭제되었습니다.");
//     }

//     // 댓글 좋아요 토글
//     @PostMapping("/{commentId}/like")
//     public ResponseEntity<Map<String, Object>> toggleCommentLike(@PathVariable Long commentId, @RequestParam String userId) {
//         Comment comment = commentRepository.findById(commentId)
//             .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
    
//         User user = userService.findById(userId);
//         if (user == null) {
//             throw new RuntimeException("사용자를 찾을 수 없습니다: " + userId);
//         }

//         String result = commentLikesService.toggleLike(comment, user);
//         boolean isLiked = commentLikesService.isLiked(comment, user);
//         int likeCount = commentLikesService.getLikeCount(comment);

//         Map<String, Object> response = new HashMap<>();
//         response.put("message", result);
//         response.put("isLiked", isLiked);
//         response.put("likeCount", likeCount);

//         return ResponseEntity.ok(response);
//     }


//     // 댓글 좋아요 상태 확인
//     @GetMapping("/{commentId}/like-status")
//     public ResponseEntity<Map<String, Object>> getCommentLikeStatus(@PathVariable Long commentId, @RequestParam String userId) {
//         Comment comment = commentRepository.findById(commentId)
//                 .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
//         User user = userService.findById(userId);
//         if (user == null) {
//             throw new RuntimeException("사용자를 찾을 수 없습니다: " + userId);
//         }

//         boolean isLiked = commentLikesService.isLiked(comment, user);
//         int likeCount = commentLikesService.getLikeCount(comment);

//         Map<String, Object> response = new HashMap<>();
//         response.put("isLiked", isLiked);
//         response.put("likeCount", likeCount);

//         return ResponseEntity.ok(response);

        


//     // 댓글 좋아요 수 조회
//     @GetMapping("/{commentId}/like-count")
//     public ResponseEntity<Integer> getCommentLikeCount(@PathVariable Long commentId) {
//         Comment comment = commentRepository.findById(commentId)
//                 .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
//         int likeCount = commentLikesService.getLikeCount(comment);
//         return ResponseEntity.ok(likeCount);
//     }

//     }
//     @GetMapping("/{commentId}")
//     public ResponseEntity<Comment> getCommentById(@PathVariable Long commentId) {
//         Comment comment = commentRepository.findById(commentId)
//                 .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
//         return ResponseEntity.ok(comment);
//     }
// }


package com.codequest.backend.controller;

import com.codequest.backend.entity.Board;
import com.codequest.backend.entity.Comment;
import com.codequest.backend.entity.User;
import com.codequest.backend.repository.BoardRepository;
import com.codequest.backend.repository.CommentRepository;
import com.codequest.backend.service.CommentLikesService;
import com.codequest.backend.service.CommentService;
import com.codequest.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.7:3000"})
public class CommentController {

    @Autowired
    private CommentLikesService commentLikesService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    // 대댓글 추가
    @PostMapping("/{parentCommentId}/reply")
    public ResponseEntity<Comment> addReply(
            @PathVariable Long parentCommentId,
            @RequestBody Comment replyRequest
    ) {
        System.out.println("Received nickname: " + replyRequest.getNickname());
    System.out.println("Received content: " + replyRequest.getContent());
        if (replyRequest.getNickname() == null || replyRequest.getContent() == null) {
            return ResponseEntity.badRequest().build();
        }

        Comment reply = commentService.addReply(
                parentCommentId,
                replyRequest.getNickname(),
                replyRequest.getContent()
        );
        return ResponseEntity.ok(reply);
    }

    // 특정 게시글의 댓글 조회
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<Comment>> getCommentsByBoardId(@PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));
        List<Comment> comments = commentRepository.findByBoard(board);
        return ResponseEntity.ok(comments);
    }

    // 댓글 작성
    @PostMapping("/board/{boardId}")
    public ResponseEntity<Comment> addCommentToBoard(@PathVariable Long boardId, @RequestBody Comment comment) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + boardId));
        comment.setBoard(board);
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok(savedComment);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody Comment updatedCommentDetails) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
        existingComment.setContent(updatedCommentDetails.getContent());
        existingComment.setLikes(updatedCommentDetails.getLikes());
        Comment updatedComment = commentRepository.save(existingComment);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
        commentRepository.delete(comment);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
        // 댓글 좋아요 토글
        @PostMapping("/{commentId}/like")
        public ResponseEntity<Map<String, Object>> toggleCommentLike(
                @PathVariable Long commentId,
                @RequestBody Map<String, String> payload) {
        String userId = payload.get("userId");
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
    
        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + userId);
        }

        String result = commentLikesService.toggleLike(comment, user);
        boolean isLiked = commentLikesService.isLiked(comment, user);
        int likeCount = commentLikesService.getLikeCount(comment);

        Map<String, Object> response = new HashMap<>();
        response.put("message", result);
        response.put("isLiked", isLiked);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }


    // 댓글 좋아요 상태 확인
    @GetMapping("/{commentId}/like-status")
    public ResponseEntity<Map<String, Object>> getCommentLikeStatus(@PathVariable Long commentId, @RequestParam String userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + userId);
        }

        boolean isLiked = commentLikesService.isLiked(comment, user);
        int likeCount = commentLikesService.getLikeCount(comment);

        Map<String, Object> response = new HashMap<>();
        response.put("isLiked", isLiked);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);

    }


    // 댓글 좋아요 수 조회
    @GetMapping("/{commentId}/like-count")
    public ResponseEntity<Integer> getCommentLikeCount(@PathVariable Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다: " + commentId));
        int likeCount = commentLikesService.getLikeCount(comment);
        return ResponseEntity.ok(likeCount);
    }
    
}
