package com.codequest.backend.controller;

import com.codequest.backend.entity.User;
import com.codequest.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


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
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.0.34:3000"})
public class UserController {

    @Autowired
    private UserService userService;

    // 회원가입 (Register)
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // 중복 확인 로직 추가 가능
        return userService.saveUser(user);
    }

    // 로그인 (Login)
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        boolean isValid = userService.validateUser(user);
        if (isValid) {
            return ResponseEntity.ok("Login Successful");
        } else {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
    }
}

