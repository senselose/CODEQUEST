package com.codequest.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codequest.backend.entity.TestEntity;

public interface TestRepository extends JpaRepository<TestEntity, String>{
    

    
}
