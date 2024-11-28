package com.codequest.backend.service;
import com.codequest.backend.dto.KakaoUserDto;
import com.codequest.backend.entity.User;
import com.codequest.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

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
    //     // 로그인: 사용자 인증
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
        // 기본 가입 방식 설정

        if (existsById(user.getId())) {
            throw new IllegalArgumentException("ID already exists");
        }
        if (existsByMail(user.getMail())) {
            throw new IllegalArgumentException("mail already exists");
        }
        if (existsByNickName(user.getNickName())) {
            throw new IllegalArgumentException("Nickname already exists");
        }
          // 기본값 설정
          if (user.getMethod() == null) {
            user.setMethod("local");
        }
        if (user.getMarketing() == null) {
            user.setMarketing(null);
        }
        // 마케팅 동의 값이 없으면 null로 설정
        if (user.getMarketing() == null) {
            user.setMarketing(null);
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

    //아이디 비밀번호 찾기
        /**
     * 아이디 찾기
     */
    public String findId(String name, String phone, String mail) {
        Optional<User> user = userRepository.findByNameAndPhoneAndMail(name, phone, mail);
        return user.map(User::getId).orElse("일치하는 사용자가 없습니다.");
    }


    public String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 10; i++) { // 10자리 비밀번호 생성
            int randomIndex = (int) (Math.random() * chars.length());
            password.append(chars.charAt(randomIndex));
        }
        return password.toString();
    }

    public boolean updatePassword(String userId, String newPassword) {
        // 비밀번호 암호화 후 DB 업데이트 로직
        // 예: userRepository.updatePassword(userId, encodedPassword);
        return true; // 성공 여부 반환
    }

    public boolean validateUserInfo(String name, String id, String mail) {
        // 사용자 정보 검증 로직
        return true; // 검증 성공 여부
    }
    
     
}
