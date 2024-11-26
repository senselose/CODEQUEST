package com.codequest.backend.controller;

import com.codequest.backend.dto.KakaoUserDto;
import com.codequest.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/kakaoLogin")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.7:3000"})
public class KakaoController {

    @Autowired
    private UserService userService;

    // 카카오 로그인 데이터 저장
    @PostMapping("/login")
    public ResponseEntity<String> handleKakaoLogin(@RequestBody KakaoUserDto kakaoUserDto) {
        userService.saveKakaoUser(kakaoUserDto);
        return ResponseEntity.ok("Kakao user data saved successfully!");
    }
}
