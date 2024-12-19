package com.codequest.backend.service;

import java.util.Date; // 지은 추가

import com.codequest.backend.dto.KakaoUserDto;
import com.codequest.backend.entity.User;
import com.codequest.backend.repository.UserRepository;

import io.jsonwebtoken.Claims;// 지은 추가
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;// 지은 추가
import io.jsonwebtoken.SignatureAlgorithm;// 지은 추가
import java.security.Key;// 지은 추가

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // 지은 추가
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Optional;
import java.util.UUID;
// @Service
// public class UserService {
//     @Autowired
//     private UserRepository userRepository;
//     public User saveUser(User user) {
//         return userRepository.save(user);
//     }
// }

import javax.crypto.spec.SecretKeySpec;


// @Service
// public class UserService {
//     @Autowired
//     private UserRepository userRepository;
//     // // 로그인: 사용자 인증
//     public boolean validateUser(User user) {
//         User foundUser = userRepository.findById(user.getId()).orElse(null);
//         return foundUser != null && foundUser.getPassword().equals(user.getPassword());
//     }
//     // 사용자 조회 (카카오 로그인)
//     public User findById(String id) {
//         return userRepository.findById(id).orElse(null);
//     }
//     // ID 중복 확인
//     public boolean existsById(String id) {
//         return userRepository.existsById(id);
//     }
//     // 이메일 중복 확인
//     public boolean existsByMail(String mail) {
//         return userRepository.existsByMail(mail);
//     }
//     // 닉네임 중복 확인
//     public boolean existsByNickName(String nickName) {
//         return userRepository.existsByNickName(nickName);
//     }
//     // 사용자 저장 (회원가입)
//     public User saveUser(User user) {
//         if (existsById(user.getId())) {
//             throw new IllegalArgumentException("ID already exists");
//         }
//         if (existsByMail(user.getMail())) {
//             throw new IllegalArgumentException("mail already exists");
//         }
//         if (existsByNickName(user.getNickName())) {
//             throw new IllegalArgumentException("Nickname already exists");
//         }
//         return userRepository.save(user);
//     }
//     // 카카오 사용자 저장
//     public void saveKakaoUser(KakaoUserDto kakaoUserDto) {
//         // 중복 확인 로직 (닉네임 또는 다른 조건으로 중복 확인 가능)
//         if (userRepository.existsByNickName(kakaoUserDto.getNickname())) {
//             throw new IllegalArgumentException("Nickname already exists");
//         }
//         // KakaoUserDto 데이터를 User 엔티티로 변환
//         User user = new User();
//         user.setNickName(kakaoUserDto.getNickname());
//         user.setProfilePicturePath(kakaoUserDto.getProfileImageUrl());
//         // 데이터 저장
//         userRepository.save(user);
//     }
//     // 아이디 비밀번호 찾기
//     /**
//      * 아이디 찾기
//      */
//     public String findId(String name, String phone, String mail) {
//         Optional<User> user = userRepository.findByNameAndPhoneAndMail(name, phone, mail);
//         return user.map(User::getId).orElse("일치하는 사용자가 없습니다.");
//     }
//     /**
//      * 비밀번호 변경
//      */
//     public boolean validateUserInfo(String name, String id, String mail) {
//         Optional<User> user = userRepository.findByIdAndNameAndMail(id, name, mail);
//         return user.isPresent();
//     }
//     public boolean updatePassword(String id, String newPassword) {
//         Optional<User> user = userRepository.findById(id);
//         if (user.isPresent()) {
//             user.get().setPassword(newPassword); // 프론트에서 전달된 password를 새 비밀번호로 저장
//             userRepository.save(user.get()); // 변경된 사용자 정보 저장
//             return true;
//         }
//         return false;
//     }
//     private final String uploadDir = "/Users/jieunseo/uploads/profile";
//     public String saveProfilePicture(String userId, MultipartFile file) throws IOException {
//         // 디렉토리가 없으면 생성
//         File directory = new File(uploadDir);
//         if (!directory.exists()) {
//             directory.mkdirs();
//         }
//         // 파일 이름 생성
//         String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//         // 파일 저장 경로
//         Path filePath = Paths.get(uploadDir, uniqueFileName);
//         file.transferTo(filePath.toFile());
//         // 데이터베이스에 사용자 정보 업데이트
//         String profilePicturePath = "/uploads/profile/" + uniqueFileName;
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
//         user.setProfilePicturePath(profilePicturePath);
//         userRepository.save(user); // 사용자 엔티티 저장
//         return profilePicturePath; // 저장된 이미지 경로 반환
//     }

//     // 마이페이지 프로필 수정 후 업데이트
//     public User updateUser(String id, User updateUser) {
//         return userRepository.findById(id).map(user -> {
//             user.setNickName(updateUser.getNickName());
//             user.setInfo(updateUser.getInfo());
//             user.setPhone(updateUser.getPhone());
//             user.setMarketing(updateUser.getMarketing());
//             if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
//                 user.setPassword(updateUser.getPassword());
//             }
//             return userRepository.save(user);
//         }).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다" + id));
//     }

//     // 비밀번호 확인 메서드 (마이페이지내 프로필 수정 시 비밀번호 확인 구문)
//     public boolean checkPassword(String id, String password) {
//         User user = userRepository.findById(id).orElse(null);
//         if(user == null) {
//             return false; // 사용자 없음
//         }
//         return user.getPassword().equals(password); // 비밀번호 비교 
//     }
// }

@Service
public class UserService {

    @Value("${jwt.secret}")
    private String secretKey;

     // Key 객체 생성
    private Key getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(); // 문자열을 바이트 배열로 변환
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName()); // Key 객체 생성
    }

    // JWT 생성
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId()) // User의 ID를 JWT의 subject로 설정
                .claim("nickname", user.getNickName()) // 추가 클레임 설정
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1일 유효
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Deprecated 방식 수정
                .compact();
    }

    // JWT 검증
    public boolean validateToken(String token) {
        try {
            System.out.println("Generated JWT: " + token);
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token); // 유효하면 예외가 발생하지 않음
            return true;
        } catch (JwtException e) {
            System.err.println("JWT 검증 실패: " + e.getMessage());
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody(); // JWT의 Body에서 클레임 추출
        return claims.getSubject(); // subject에 저장된 userId 반환
    }

    @Autowired
    private UserRepository userRepository;
    // // 로그인: 사용자 인증
    public boolean validateUser(User user) {
        User foundUser = userRepository.findById(user.getId()).orElse(null);
        return foundUser != null && foundUser.getPassword().equals(user.getPassword());
    }
    // 사용자 조회 (카카오 로그인)
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }
    // ID 중복 확인
    public boolean existsById(String id) {
        return userRepository.existsById(id);
    }
    // 이메일 중복 확인
    public boolean existsByMail(String mail) {
        return userRepository.existsByMail(mail);
    }
    // 닉네임 중복 확인
    public boolean existsByNickName(String nickName) {
        return userRepository.existsByNickName(nickName);
    }
    // 사용자 저장 (회원가입)
    public User saveUser(User user) {
        if (existsById(user.getId())) {
            throw new IllegalArgumentException("ID already exists");
        }
        if (existsByMail(user.getMail())) {
            throw new IllegalArgumentException("mail already exists");
        }
        if (existsByNickName(user.getNickName())) {
            throw new IllegalArgumentException("Nickname already exists");
        }
        return userRepository.save(user);
    }
    // 카카오 사용자 저장
    public void saveKakaoUser(KakaoUserDto kakaoUserDto) {
        // 중복 확인 로직 (닉네임 또는 다른 조건으로 중복 확인 가능)
        if (userRepository.existsByNickName(kakaoUserDto.getNickname())) {
            throw new IllegalArgumentException("Nickname already exists");
        }
        // KakaoUserDto 데이터를 User 엔티티로 변환
        User user = new User();
        user.setNickName(kakaoUserDto.getNickname());
        user.setProfilePicturePath(kakaoUserDto.getProfileImageUrl());
        // 데이터 저장
        userRepository.save(user);
    }
    // 아이디 비밀번호 찾기
    /**
     * 아이디 찾기
     */
    public String findId(String name, String phone, String mail) {
        Optional<User> user = userRepository.findByNameAndPhoneAndMail(name, phone, mail);
        return user.map(User::getId).orElse("일치하는 사용자가 없습니다.");
    }
    /**
     * 비밀번호 변경
     */
    public boolean validateUserInfo(String name, String id, String mail) {
        Optional<User> user = userRepository.findByIdAndNameAndMail(id, name, mail);
        return user.isPresent();
    }
    public boolean updatePassword(String id, String newPassword) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setPassword(newPassword); // 프론트에서 전달된 password를 새 비밀번호로 저장
            userRepository.save(user.get()); // 변경된 사용자 정보 저장
            return true;
        }
        return false;
    }
    private final String uploadDir = "/Users/jieunseo/uploads/profile";
    public String saveProfilePicture(String userId, MultipartFile file) throws IOException {
        // 디렉토리가 없으면 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // 파일 이름 생성
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        // 파일 저장 경로
        Path filePath = Paths.get(uploadDir, uniqueFileName);
        file.transferTo(filePath.toFile());
        // 데이터베이스에 사용자 정보 업데이트
        String profilePicturePath = "/uploads/profile/" + uniqueFileName;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        user.setProfilePicturePath(profilePicturePath);
        userRepository.save(user); // 사용자 엔티티 저장
        return profilePicturePath; // 저장된 이미지 경로 반환
    }

    // 마이페이지 프로필 수정 후 업데이트
    public User updateUser(String id, User updateUser) {
        return userRepository.findById(id).map(user -> {
            user.setNickName(updateUser.getNickName());
            user.setInfo(updateUser.getInfo());
            user.setPhone(updateUser.getPhone());
            user.setMarketing(updateUser.getMarketing());
            if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
                user.setPassword(updateUser.getPassword());
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다" + id));
    }

    // 비밀번호 확인 메서드 (마이페이지내 프로필 수정 시 비밀번호 확인 구문)
    public boolean checkPassword(String id, String password) {
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            return false; // 사용자 없음
        }
        return user.getPassword().equals(password); // 비밀번호 비교 
    }

    // 자기소개 수정 메서드
    public boolean updateUserInfo(String userId, String newInfo) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setInfo(newInfo); // 자기소개 업데이트
            userRepository.save(user);
            return true;
        }
        return false;
    }
}