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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            boolean isAuthenticated = userService
                    .validateUser(new User(loginRequest.getId(), loginRequest.getPassword()));
            if (isAuthenticated) {
                User user = userService.findById(loginRequest.getId());
                session.setAttribute("userId", user.getId());
                session.setAttribute("nickname", user.getNickName());
                return ResponseEntity.ok(Map.of(
                        "userId", user.getId(),
                        "nickname", user.getNickName()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
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
}

// package com.codequest.backend.controller;
// import com.codequest.backend.dto.LoginRequest;
// import com.codequest.backend.entity.User;
// import com.codequest.backend.service.UserService;
// import java.util.Map;
// import javax.servlet.http.HttpSession;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import com.codequest.backend.dto.KakaoUserDto;
// import java.util.HashMap;

// @RestController
// @RequestMapping("/api/auth")
// @CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.7:3000"})
// public class UserController {

// @Autowired
// private UserService userService;

// // // 회원가입 (Register)
// // @PostMapping("/register")
// // public User registerUser(@RequestBody User user) {
// // // 중복 확인 로직 추가 가능
// // return userService.saveUser(user);
// // }
// // ID 중복 확인

// @GetMapping("/checkId")
// public ResponseEntity<Boolean> checkIdDuplicate(@RequestParam String id) {
// boolean exists = !userService.existsById(id); // 아이디 중복 여부 확인
// return ResponseEntity.ok(exists);
// }
// // 이메일 중복 확인
// @GetMapping("/checkMail")
// public ResponseEntity<Boolean> checkMail(@RequestParam String mail) {
// boolean isAvailable = !userService.existsByMail(mail);
// return ResponseEntity.ok(isAvailable);
// }
// // 닉네임 중복 확인
// @GetMapping("/checkNickName")
// public ResponseEntity<Boolean> checkNickName(@RequestParam String nickName) {
// boolean isAvailable = !userService.existsByNickName(nickName);
// return ResponseEntity.ok(isAvailable);
// }
// // 회원가입
// @PostMapping("/register")
// public ResponseEntity<User> register(@RequestBody User user) {
// User savedUser = userService.saveUser(user);
// return ResponseEntity.ok(savedUser);
// }
// @PostMapping("/login")
// public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
// HttpSession session) {
// try {
// boolean isAuthenticated = userService.validateUser(new
// User(loginRequest.getId(), loginRequest.getPassword()));
// if (isAuthenticated) {
// User user = userService.findById(loginRequest.getId());
// session.setAttribute("userId", user.getId());
// session.setAttribute("nickname", user.getNickName());
// return ResponseEntity.ok(Map.of(
// "userId", user.getId(),
// "nickname", user.getNickName()
// ));
// } else {
// return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid
// credentials");
// }
// } catch (Exception e) {
// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error
// occurred: " + e.getMessage());
// }
// }

// @PostMapping("/kakaoLogin")
// public ResponseEntity<String> handleKakaoLogin(@RequestBody KakaoUserDto
// kakaoUserDto) {
// // 서비스 호출하여 데이터 저장
// userService.saveKakaoUser(kakaoUserDto);
// return ResponseEntity.ok("Kakao user data saved successfully!");
// }

// /**
// * 아이디 찾기
// */
// @PostMapping("/findId")
// public ResponseEntity<Map<String, String>> findId(@RequestParam String name,
// @RequestParam String phone, @RequestParam String mail) {
// String userInfo = userService.findId(name, phone, mail);

// Map<String, String> response = new HashMap<>();
// response.put("message", userInfo.equals("일치하는 사용자가 없습니다.") ? "실패" : "성공");
// response.put("userInfo", userInfo);
// return ResponseEntity.ok(response);
// }

// /**
// * 비밀번호 재설정
// */
// @PostMapping("/resetPassword")
// public ResponseEntity<Map<String, String>> resetPassword(@RequestParam String
// id, @RequestParam String mail, @RequestParam String newPassword) {
// String message = userService.resetPassword(id, mail, newPassword);

// Map<String, String> response = new HashMap<>();
// response.put("message", message.equals("비밀번호가 성공적으로 변경되었습니다.") ? "성공" :
// "실패");
// response.put("description", message);
// return ResponseEntity.ok(response);
// }
// }
