package com.codequest.backend.controller;
import com.codequest.backend.dto.LoginRequest;
import com.codequest.backend.dto.KakaoUserDto;
import com.codequest.backend.entity.User;
import com.codequest.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.io.File;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.0.7:3000" })
public class UserController {
    @Autowired
    private UserService userService;
    // ID 중복 확인
    @GetMapping("/checkId")
    public ResponseEntity<Boolean> checkIdDuplicate(@RequestParam String id) {
        boolean exists = userService.existsById(id);
        return ResponseEntity.ok(!exists);
    }
    // 이메일 중복 확인
    @GetMapping("/checkMail")
    public ResponseEntity<Boolean> checkMailDuplicate(@RequestParam String mail) {
        boolean exists = userService.existsByMail(mail);
        return ResponseEntity.ok(!exists);
    }
    // 닉네임 중복 확인
    @GetMapping("/checkNickName")
    public ResponseEntity<Boolean> checkNickNameDuplicate(@RequestParam String nickName) {
        boolean exists = userService.existsByNickName(nickName);
        return ResponseEntity.ok(!exists);
    }
    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            boolean isAuthenticated = userService
                    .validateUser(new User(loginRequest.getId(), loginRequest.getPassword()));
            if (isAuthenticated) {
                User user = userService.findById(loginRequest.getId());

                // JWT 생성
                String token = userService.generateToken(user);

                // HttpOnly 쿠키에 JWT 저장
                Cookie cookie = new Cookie("token", token);
                cookie.setHttpOnly(true);
                cookie.setSecure(true); // HTTPS에서만 동작 (개발 환경에서는 false 가능)
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 24); // 1일
                response.addCookie(cookie);

                return ResponseEntity.ok(Map.of(
                        "token", token, // 클라이언트에 JWT 반환 (지은 추가))
                        "userId", user.getId(),
                        "nickname", user.getNickName()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // 인증 확인 API 추가
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(@RequestHeader(name = "Authorization", required = false) String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    
        String token = authorizationHeader.replace("Bearer ", ""); // Bearer 제거
        if (!userService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    
        String userId = userService.getUserIdFromToken(token); // 토큰에서 사용자 ID 추출
        return ResponseEntity.ok(Map.of("userId", userId));
    }


    // 카카오 로그인
    @PostMapping("/kakaoLogin")
    public ResponseEntity<String> handleKakaoLogin(@RequestBody KakaoUserDto kakaoUserDto) {
        userService.saveKakaoUser(kakaoUserDto);
        return ResponseEntity.ok("Kakao user data saved successfully!");
    }
    // 아이디 찾기
    @PostMapping("/findId")
    public ResponseEntity<Map<String, String>> findId(@RequestParam String name, @RequestParam String phone,
            @RequestParam String mail) {
        String username = userService.findId(name, phone, mail);
        Map<String, String> response = new HashMap<>();
        response.put("message", username.equals("일치하는 사용자가 없습니다.") ? "실패" : "성공");
        response.put("username", username);
        return ResponseEntity.ok(response);
    }
    // 비밀번호 재설정
    @PostMapping("/resetPassword")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody User request) {
        // 사용자 정보 확인
        boolean isValid = userService.validateUserInfo(request.getName(), request.getId(), request.getMail());
        Map<String, String> response = new HashMap<>();
        if (isValid) {
            // 비밀번호 변경
            boolean isUpdated = userService.updatePassword(request.getId(), request.getPassword());
            if (isUpdated) {
                response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
            } else {
                response.put("message", "비밀번호 변경에 실패했습니다.");
            }
        } else {
            response.put("message", "사용자 정보가 일치하지 않습니다.");
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.findById(id); // id로 사용자 조회
        System.out.println("가져온 사용자 데이터: " + user.toString());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping("/{id}/uploadProfilePicture")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {
        try {
            System.out.println("요청 ID: " + id);
            System.out.println("업로드된 파일 이름: " + file.getOriginalFilename());
            System.out.println("업로드된 파일 크기: " + file.getSize());
            String profilePicturePath = userService.saveProfilePicture(id, file);
            System.out.println("저장된 파일 경로: " + profilePicturePath);
            return ResponseEntity.ok(Map.of("profilePicturePath", profilePicturePath));
        } catch (IOException e) {
            System.err.println("이미지 업로드 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "이미지 업로드 실패: " + e.getMessage()));
        }
    }


    // 마이페이지 회원정보 수정 후 업데이트
    @PutMapping("/{id}/update")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updateUser) {
        try {
            User user = userService.updateUser(id, updateUser);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 마이페이지 회원정보 수정시 비밀번호 확인
    @PostMapping("/{id}/check-password")
    public ResponseEntity<Map<String, String>> checkPassword(@PathVariable String id, @RequestBody Map<String, String> request) {
        String password = request.get("password");
        boolean isValid = userService.checkPassword(id, password);
        if (isValid) {
            return ResponseEntity.ok(Map.of("success", "true", "message", "비밀번호가 일치합니다."));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", "false", "message", "비밀번호가 일치하지 않습니다."));
        }
    }
    
    // 자기소개 수정 API
    @PutMapping("/{userId}/updateInfo")
    public ResponseEntity<?> updateUserBio(
            @PathVariable String userId, 
            @RequestBody Map<String, String> payload) {

        String newInfo = payload.get("info"); // 수정된 자기소개 내용

        boolean isUpdated = userService.updateUserInfo(userId, newInfo);
        if (isUpdated) {
            return ResponseEntity.ok().body(Map.of("message", "자기소개 수정 성공"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "자기소개 수정 실패"));
        }
    }
}









