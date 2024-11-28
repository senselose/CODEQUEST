package com.codequest.backend.service;

import com.codequest.backend.config.UploadProperties;
import com.codequest.backend.entity.Board;
import com.codequest.backend.entity.Image;
import com.codequest.backend.repository.ImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    @Value("${file.upload-dir}") // 업로드 경로
    private String uploadDir;

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void saveImages(List<MultipartFile> files, Board board) throws IOException {
        System.out.println("saveImages 호출");
        System.out.println("Board ID: " + board.getBoardId());
    
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            boolean dirCreated = uploadPath.mkdirs(); // 디렉토리 생성
            System.out.println("업로드 디렉토리 생성 여부: " + dirCreated);
        }
    
        List<Image> images = new ArrayList<>();
    
        for (MultipartFile file : files) {
            System.out.println("파일 처리 중: " + file.getOriginalFilename());
    
            // 고유 파일 이름 생성
            String uniqueFileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            String filePath = uploadDir + "/" + uniqueFileName;
            String relativeFilePath = "uploads/" + uniqueFileName;
            File dest = new File(filePath);
    
            try {
                file.transferTo(dest);
                System.out.println("파일 저장 완료: " + dest.getAbsolutePath());
    
                // 이미지 엔티티 생성
                Image image = new Image();
                image.setFilePath(relativeFilePath);
                image.setFileName(uniqueFileName);
                image.setBoard(board);
                images.add(image);
    
            } catch (IOException e) {
                System.err.println("파일 저장 중 오류: " + e.getMessage());
                throw e;
            }
        }
    
        // 데이터베이스에 저장
        System.out.println("이미지 저장 전 데이터베이스 저장 시도");
        imageRepository.saveAll(images);
        System.out.println("이미지 데이터베이스 저장 완료");
    }
    
    
        
}
