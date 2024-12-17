package com.codequest.backend.service;

import com.codequest.backend.entity.Villain;
import com.codequest.backend.repository.VillainRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VillainService {

    @Value("${file.upload-dir}") // 기본 업로드 경로
    private String uploadDir;

    private final VillainRepository villainRepository;

    public VillainService(VillainRepository villainRepository) {
        this.villainRepository = villainRepository;
    }

    public void saveVillainImages(String villainName, String category, Long boardId, List<MultipartFile> files) throws IOException {
        // 폴더 경로 설정
        String folderPath = uploadDir + "/villain/" + category; // 경로 수정
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs(); // 폴더가 없으면 생성
        }
    
        // 파일 저장 및 Villain 엔티티 저장
        List<Villain> villains = new ArrayList<>();
        for (MultipartFile file : files) {
            String uniqueFileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            String filePath = folderPath + "/" + uniqueFileName;
    
            // 파일 저장
            File dest = new File(filePath);
            file.transferTo(dest);
    
            // Villain 엔티티 생성
            Villain villain = new Villain();
            villain.setVillainName(villainName); // 빌런 이름 설정
            villain.setCategory(category); // 카테고리 저장
            villain.setFileName(uniqueFileName); // 고유 파일 이름 저장
            villain.setFilePath("villain/" + category + "/" + uniqueFileName); // 상대 경로 저장
            villain.setBoardId(boardId); // 보드 ID 설정
    
            villains.add(villain); // 리스트에 추가
        }
    
        // 데이터베이스에 저장
        villainRepository.saveAll(villains);
    }

    public List<Villain> getVillainsByCategory(String category) {
        return villainRepository.findByCategory(category);
    }
    
}    