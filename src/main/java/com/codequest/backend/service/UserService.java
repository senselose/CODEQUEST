package com.codequest.backend.service;

import com.codequest.backend.entity.User;
import com.codequest.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// @Service
// public class UserService {

//     @Autowired
//     private UserRepository userRepository;

//     public User saveUser(User user) {
//         return userRepository.save(user);
//     }
// }
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 회원가입: 사용자 저장
    public User saveUser(User user) {
        // ID 중복 확인
        if (userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("ID already exists");
        }
        return userRepository.save(user);
    }

    // 로그인: 사용자 인증
    public boolean validateUser(User user) {
        User foundUser = userRepository.findById(user.getId()).orElse(null);
        return foundUser != null && foundUser.getPassword().equals(user.getPassword());
    }
    // 사용자 조회 
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }
    
}

