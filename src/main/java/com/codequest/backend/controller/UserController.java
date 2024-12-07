package com.codequest.backend.controller;

import com.codequest.backend.dto.LoginRequest;
import com.codequest.backend.dto.KakaoUserDto;
import com.codequest.backend.entity.User;
import com.codequest.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.7:3000"})
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
    // @PostMapping("/register")
    // public ResponseEntity<User> register(@RequestBody User user) {
    //     User savedUser = userService.saveUser(user);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(savedUser); // 리소스 생성으로 201이 반환
    // }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        System.out.println("Marketing: " + user.getMarketing()); // 값 디버깅
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully"); // 200으로
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            boolean isAuthenticated = userService.validateUser(new User(loginRequest.getId(), loginRequest.getPassword()));
            if (isAuthenticated) {
                User user = userService.findById(loginRequest.getId());
                session.setAttribute("userId", user.getId());
                session.setAttribute("nickname", user.getNickName());
                return ResponseEntity.ok(Map.of(
                        "userId", user.getId(),
                        "nickname", user.getNickName()
                ));
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
    public ResponseEntity<Map<String, String>> findId(@RequestParam String name, @RequestParam String phone, @RequestParam String mail) {
        String username = userService.findId(name, phone, mail);
        Map<String, String> response = new HashMap<>();
        response.put("message", username.equals("일치하는 사용자가 없습니다.") ? "실패" : "성공");
        response.put("username", username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody User request) {
        // 사용자 정보 확인
        boolean isValid = userService.validateUserInfo(request.getName(), request.getId(), request.getMail());
        Map<String, String> response = new HashMap<>();
    
        if (isValid) {
            String temporaryPassword = userService.generateTemporaryPassword();
            boolean isUpdated = userService.updatePassword(request.getId(), temporaryPassword);
            if (isUpdated) {
                response.put("message", "임시 비밀번호가 성공적으로 설정되었습니다: " + temporaryPassword);
            } else {
                response.put("message", "비밀번호 변경에 실패했습니다.");
            }
        } else {
            response.put("message", "사용자 정보가 일치하지 않습니다.");
        }
        return ResponseEntity.ok(response);
    }
}
