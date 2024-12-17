package com.codequest.backend.service;
import com.codequest.backend.dto.KakaoUserDto;
import com.codequest.backend.entity.User;
import com.codequest.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
public class UserService {
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
    private final String uploadDir = "/Users/ahncoco/uploads/profile";
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
}