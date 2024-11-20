package com.codequest.backend.controller;

import com.codequest.backend.dto.LoginRequest;
import com.codequest.backend.entity.User;
import com.codequest.backend.service.UserService;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.7:3000"})
public class UserController {

    @Autowired
    private UserService userService;

    // 회원가입 (Register)
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // 중복 확인 로직 추가 가능
        return userService.saveUser(user);
    }

    // // 로그인 (Login)
    // @PostMapping("/login")
    // public ResponseEntity<String> loginUser(@RequestBody User user) {
    //     boolean isValid = userService.validateUser(user);
    //     if (isValid) {
    //         return ResponseEntity.ok("Login Successful");
    //     } else {
    //         return ResponseEntity.status(401).body("Invalid Credentials");
    //     }
    // }

    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody User user) {
    // boolean isValid = userService.validateUser(user);
    // if (isValid) {
    //     return ResponseEntity.ok(Map.of(
    //         "userId", user.getId(),
    //         "nickname", user.getNickName()
    //     ));
    // } else {
    //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    // }

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

}




// /*register url 추가*/
// @RestController
// @RequestMapping("/api/auth")
// @CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.34:3000"})
// public class UserController {

//     @Autowired
//     private UserService userService;

//     @PostMapping("/login")
//     public User createUser(@RequestBody User user) {
//         return userService.saveUser(user);
//     }
// }