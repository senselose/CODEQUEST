package com.codequest.backend.entity;

import javax.persistence.*;
import lombok.Data; 

@Entity
@Data
public class Villain {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long villainId; // 빌런 ID

    @Column(nullable = false, unique = true)
    private String name; // 빌런 이름

    @Column(nullable = false)
    private String filePath; // 파일 경로

    @Column(nullable = false)
    private String fileName; // 파일 이름
    
    @Column(nullable = false, unique = true)
    private String folderName; // 빌런 폴더 이름 (예: sloth, fiery)

}
