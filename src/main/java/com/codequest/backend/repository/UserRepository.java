// package com.codequest.backend.repository;

// import com.codequest.backend.entity.User;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// @Repository
// public interface UserRepository extends JpaRepository<User, String> {
// }

package com.codequest.backend.repository;

import com.codequest.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // 중복 확인 메서드
    boolean existsById(String id);      // 아이디 중복 확인
    boolean existsByMail(String mail); // 이메일 중복 확인
    boolean existsByNickName(String nickName); // 닉네임 중복 확인

    // 사용자 검색 메서드
    Optional<User> findByNameAndPhoneAndMail(String name, String phone, String mail);

    Optional<User> findByIdAndNameAndMail(String id, String name, String mail);
}
