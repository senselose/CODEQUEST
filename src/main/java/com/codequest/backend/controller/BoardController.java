package com.codequest.backend.controller;

import com.codequest.backend.entity.Board;
import com.codequest.backend.entity.BoardLikes;
import com.codequest.backend.repository.BoardLikesRepository;
import com.codequest.backend.repository.BoardRepository;
import com.codequest.backend.service.BoardService;
import com.codequest.backend.service.ImageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/boards")
@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.0.7:3000" })
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private ImageService imageService; // ImageService 주입

    @Autowired
    private BoardLikesRepository boardLikesRepository; // 좋아요 리포지토리 추가

    // 검색 및 모든 게시글 조회 (통합)
    // @GetMapping
    // public Page<Board> getBoards(
    // @RequestParam(value = "search", required = false) String search,
    // Pageable pageable) {
    // if (search != null && !search.isEmpty()) {
    // return boardService.searchBoards(search, pageable);
    // } else {
    // return boardService.getAllBoards(pageable);
    // }
    // }

    // 수정된 getBoards 메서드
    @GetMapping
    public Page<Board> getBoards(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "filterDate", required = false) Integer filterDate,
            @RequestParam(value = "sort", required = false) String sort,
            Pageable pageable) {

        System.out.println("Received Pageable: " + pageable.getSort());

        // 정렬 조건이 없는 경우 기본 정렬 설정
        Sort sorting;
        if (sort == null) {
            sorting = Sort.by(Sort.Direction.DESC, "createdAt"); // 기본 정렬: createdAt 내림차순
        } else {
            switch (sort) {
                case "createdAt":
                    sorting = Sort.by(Sort.Direction.DESC, "createdAt"); // 최신순 정렬
                    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting);
                    break;

                case "likes":
                    // 좋아요 수 기준 정렬
                    return boardRepository.findAllByLikesCount(pageable);

                case "views":
                    sorting = Sort.by(Sort.Direction.DESC, "views"); // 조회수 기준 정렬
                    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting);
                    break;

                default:
                    sorting = Sort.by(Sort.Direction.DESC, "createdAt"); // 기본값 (최신순)
                    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting);
            }
        }

        // 날짜 필터링이 있는 경우
        if (filterDate != null) {
            LocalDateTime startDate = LocalDateTime.now().minusDays(filterDate);
            return boardRepository.findByCreatedAtAfter(startDate, pageable);
        }

        // 검색어가 있는 경우
        if (search != null && !search.isEmpty()) {
            return boardService.searchBoards(search, pageable);
        }

        // 기본적으로 모든 게시글 반환
        return boardService.getAllBoards(pageable);
    }

    public BoardController(BoardRepository boardRepository, ImageService imageService) {
        this.boardRepository = boardRepository;
        this.imageService = imageService;
    }

    // @PostMapping
    // @Transactional
    // public ResponseEntity<String> createBoard(
    // @RequestPart("nickname") String nickname,
    // @RequestPart("title") String title,
    // @RequestPart("content") String content,
    // @RequestPart("category") String category,
    // @RequestPart(value = "hashtags", required = false) String hashtags,
    // @RequestPart(value = "location", required = false) String location,
    // @RequestPart(value = "images", required = false) List<MultipartFile> files) {
    // try {
    // System.out.println("createBoard 호출됨");
    // System.out.println("받은 이미지 개수: " + (files != null ? files.size() : 0));

    // // if (files == null || files.isEmpty()) {
    // // System.out.println("파일이 전달되지 않았습니다.");
    // // return ResponseEntity.badRequest().body("이미지가 전달되지 않았습니다.");
    // // }

    // for (MultipartFile file : files) {
    // System.out.println("파일 이름: " + file.getOriginalFilename());
    // System.out.println("파일 크기: " + file.getSize());
    // }
    // // 게시글 생성
    // Board board = new Board();
    // board.setNickname(nickname);
    // board.setTitle(title);
    // board.setContent(content);
    // board.setCategory(category);
    // board.setHashtags(hashtags);
    // board.setLocation(location);

    // boardRepository.save(board); // 게시글 저장
    // System.out.println("hi");
    // // 이미지 저장
    // if (files != null && !files.isEmpty()) {
    // imageService.saveImages(files, board);
    // }

    // return ResponseEntity.ok("게시글 저장 완료");
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body("게시글 저장 중 오류가 발생했습니다: " + e.getMessage());
    // }
    // }

    @PostMapping
    @Transactional
    public ResponseEntity<String> createBoard(
            @RequestPart("nickname") String nickname,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart("category") String category,
            @RequestPart(value = "hashtags", required = false) String hashtags,
            @RequestPart(value = "location", required = false) String location,
            @RequestPart(value = "images", required = false) List<MultipartFile> files) {
        try {
            System.out.println("createBoard 호출됨");
            System.out.println("받은 이미지 개수: " + (files != null ? files.size() : 0));

            // // 디버깅: 필드 값 확인
            // System.out.println("nickname: " + nickname);
            // System.out.println("title: " + title);
            // System.out.println("content: " + content);
            // System.out.println("category: " + category);
            // System.out.println("hashtags: " + hashtags);
            // System.out.println("location: " + location);

            // 게시글 생성
            Board board = new Board();
            board.setNickname(nickname);
            board.setTitle(title);
            board.setContent(content);
            board.setCategory(category);
            board.setHashtags(hashtags);
            board.setLocation(location);

            System.out.println("board 생성 완료, 저장 시도");

            boardRepository.save(board); // 게시글 저장
            System.out.println("게시글 저장 성공");

            // 이미지 저장
            if (files != null && !files.isEmpty()) {
                System.out.println("이미지 저장 시작");
                imageService.saveImages(files, board);
                System.out.println("이미지 저장 완료");
            }

            return ResponseEntity.ok("게시글 저장 완료");
        } catch (Exception e) {
            e.printStackTrace(); // 예외 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("게시글 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }  

    // 특정 게시글 조회
    @GetMapping("/{id}")
    public Board getBoardById(@PathVariable Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다: " + id));
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public Board updateBoard(@PathVariable Long id, @RequestBody Board boardDetails) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다: " + id));

        // 제목, 내용 등 기본 정보 업데이트
        board.setTitle(boardDetails.getTitle());
        board.setContent(boardDetails.getContent());
        board.setCategory(boardDetails.getCategory());
        board.setIsHidden(boardDetails.getIsHidden());
        board.setLocation(boardDetails.getLocation());
        board.setLikes(boardDetails.getLikes());

        // 조회수 증가
        board.setViews(board.getViews() + 1);

        return boardRepository.save(board);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다: " + id));
        boardRepository.delete(board);
    }

    // 조회수 증가 API
    @PutMapping("/{id}/views")
    public Board incrementViews(@PathVariable Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        board.setViews(board.getViews() + 1);
        return boardRepository.save(board);
    }

    // 좋아요 증가 API
    @PostMapping("/{boardId}/like")
    public ResponseEntity<String> toggleLike(
            @PathVariable String boardId,
            @RequestBody Map<String, String> payload) { // Long -> String
        String userId = payload.get("userId");

        // 기존 로직 수정 없이 사용
        Board board = boardRepository.findById(Long.parseLong(boardId))
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Optional<BoardLikes> existingLike = boardLikesRepository.findByUserIdAndBoard_BoardId(userId,
                Long.parseLong(boardId));

        if (existingLike.isPresent()) {
            boardLikesRepository.delete(existingLike.get());
            return ResponseEntity.ok("좋아요 취소");
        } else {
            BoardLikes newLike = new BoardLikes();
            newLike.setUserId(userId);
            newLike.setBoard(board);
            boardLikesRepository.save(newLike);
            return ResponseEntity.ok("좋아요");
        }
    }

    @GetMapping("/{boardId}/like-status")
    public ResponseEntity<Boolean> checkLikeStatus(
            @PathVariable Long boardId,
            @RequestParam String userId) {
        boolean liked = boardLikesRepository
                .findByUserIdAndBoard_BoardId(userId, boardId)
                .isPresent();
        return ResponseEntity.ok(liked);
    }

}

// package com.codequest.backend.controller;

// import com.codequest.backend.entity.Board;
// import com.codequest.backend.entity.BoardLikes;
// import com.codequest.backend.repository.BoardLikesRepository;
// import com.codequest.backend.repository.BoardRepository;
// import com.codequest.backend.service.BoardService;

// import java.time.LocalDateTime;
// import java.util.Map;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.ResponseEntity;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.web.bind.annotation.*;

// import org.springframework.data.domain.Sort;

// @RestController
// @RequestMapping("/api/boards")
// @CrossOrigin(origins = { "http://localhost:3000", "http://192.168.0.7:3000"
// })
// public class BoardController {

// @Autowired
// private BoardRepository boardRepository;

// @Autowired
// private BoardService boardService;

// @Autowired
// private BoardLikesRepository boardLikesRepository; // 좋아요 리포지토리 추가

// // 검색 및 모든 게시글 조회 (통합)
// // @GetMapping
// // public Page<Board> getBoards(
// // @RequestParam(value = "search", required = false) String search,
// // Pageable pageable) {
// // if (search != null && !search.isEmpty()) {
// // return boardService.searchBoards(search, pageable);
// // } else {
// // return boardService.getAllBoards(pageable);
// // }
// // }

// // 수정된 getBoards 메서드
// @GetMapping
// public Page<Board> getBoards(
// @RequestParam(value = "search", required = false) String search,
// @RequestParam(value = "filterDate", required = false) Integer filterDate,
// @RequestParam(value = "sort", required = false) String sort,
// Pageable pageable) {

// System.out.println("Received Pageable: " + pageable.getSort());

// // 정렬 조건이 없는 경우 기본 정렬 설정
// Sort sorting;
// if (sort == null) {
// sorting = Sort.by(Sort.Direction.DESC, "createdAt"); // 기본 정렬: createdAt 내림차순
// } else {
// switch (sort) {
// case "createdAt":
// sorting = Sort.by(Sort.Direction.DESC, "createdAt"); // 최신순 정렬
// pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
// sorting);
// break;

// case "likes":
// // 좋아요 수 기준 정렬
// return boardRepository.findAllByLikesCount(pageable);

// case "views":
// sorting = Sort.by(Sort.Direction.DESC, "views"); // 조회수 기준 정렬
// pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
// sorting);
// break;

// default:
// sorting = Sort.by(Sort.Direction.DESC, "createdAt"); // 기본값 (최신순)
// pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
// sorting);
// }
// }

// // 날짜 필터링이 있는 경우
// if (filterDate != null) {
// LocalDateTime startDate = LocalDateTime.now().minusDays(filterDate);
// return boardRepository.findByCreatedAtAfter(startDate, pageable);
// }

// // 검색어가 있는 경우
// if (search != null && !search.isEmpty()) {
// return boardService.searchBoards(search, pageable);
// }

// // 기본적으로 모든 게시글 반환
// return boardService.getAllBoards(pageable);
// }

// // 게시글 생성
// @PostMapping
// public Board createBoard(@RequestBody Board board) {
// return boardRepository.save(board);
// }

// // 특정 게시글 조회
// @GetMapping("/{id}")
// public Board getBoardById(@PathVariable Long id) {
// return boardRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다: " + id));
// }

// // 게시글 수정
// @PutMapping("/{id}")
// public Board updateBoard(@PathVariable Long id, @RequestBody Board
// boardDetails) {
// Board board = boardRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다: " + id));

// // 제목, 내용 등 기본 정보 업데이트
// board.setTitle(boardDetails.getTitle());
// board.setContent(boardDetails.getContent());
// board.setCategory(boardDetails.getCategory());
// board.setIsHidden(boardDetails.getIsHidden());
// board.setLocation(boardDetails.getLocation());
// board.setLikes(boardDetails.getLikes());

// // 조회수 증가
// board.setViews(board.getViews() + 1);

// return boardRepository.save(board);
// }

// // 게시글 삭제
// @DeleteMapping("/{id}")
// public void deleteBoard(@PathVariable Long id) {
// Board board = boardRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다: " + id));
// boardRepository.delete(board);
// }

// // 조회수 증가 API
// @PutMapping("/{id}/views")
// public Board incrementViews(@PathVariable Long id) {
// Board board = boardRepository.findById(id).orElseThrow(() -> new
// RuntimeException("게시글을 찾을 수 없습니다."));
// board.setViews(board.getViews() + 1);
// return boardRepository.save(board);
// }

// // 좋아요 증가 API
// @PostMapping("/{boardId}/like")
// public ResponseEntity<String> toggleLike(
// @PathVariable String boardId,
// @RequestBody Map<String, String> payload) { // Long -> String
// String userId = payload.get("userId");

// // 기존 로직 수정 없이 사용
// Board board = boardRepository.findById(Long.parseLong(boardId))
// .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

// Optional<BoardLikes> existingLike =
// boardLikesRepository.findByUserIdAndBoard_BoardId(userId,
// Long.parseLong(boardId));

// if (existingLike.isPresent()) {
// boardLikesRepository.delete(existingLike.get());
// return ResponseEntity.ok("좋아요 취소");
// } else {
// BoardLikes newLike = new BoardLikes();
// newLike.setUserId(userId);
// newLike.setBoard(board);
// boardLikesRepository.save(newLike);
// return ResponseEntity.ok("좋아요");
// }
// }

// @GetMapping("/{boardId}/like-status")
// public ResponseEntity<Boolean> checkLikeStatus(
// @PathVariable Long boardId,
// @RequestParam String userId) {
// boolean liked = boardLikesRepository
// .findByUserIdAndBoard_BoardId(userId, boardId)
// .isPresent();
// return ResponseEntity.ok(liked);
// }

// }
