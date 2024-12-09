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

    /**
     * 빌런 이미지 저장
     * @param villainName 빌런 이름
     * @param files 업로드할 이미지 파일 리스트
     * @throws IOException 파일 저장 중 발생하는 예외
     */
    public void saveVillainImages(String villainName, List<MultipartFile> files) throws IOException {
        // 빌런 이름으로 Villain 엔티티 조회
        Villain villain = villainRepository.findByName(villainName);
        if (villain == null) {
            throw new IllegalArgumentException("빌런 이름이 유효하지 않습니다: " + villainName);
        }

        // 빌런별 폴더 경로 설정
        String villainFolder = uploadDir + "/" + villain.getFolderName();
        File folder = new File(villainFolder);
        if (!folder.exists()) {
            folder.mkdirs(); // 폴더가 없으면 생성
        }

        List<Villain> images = new ArrayList<>();
        for (MultipartFile file : files) {
            // 고유 파일 이름 생성
            String uniqueFileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            String filePath = villainFolder + "/" + uniqueFileName;

            File dest = new File(filePath);
            file.transferTo(dest); // 파일 저장

            // Villain 엔티티의 이미지 정보 업데이트
            Villain image = new Villain();
            image.setFilePath(villain.getFolderName() + "/" + uniqueFileName);
            image.setFileName(uniqueFileName);

            images.add(image);
        }

        // VillainRepository를 통해 저장
        villainRepository.saveAll(images);
    }
}
