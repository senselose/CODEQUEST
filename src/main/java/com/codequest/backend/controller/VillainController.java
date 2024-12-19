package com.codequest.backend.controller;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.codequest.backend.entity.Villain;
import com.codequest.backend.service.VillainService;
import com.codequest.backend.repository.VillainRepository;

import java.net.MalformedURLException;
import java.net.http.HttpHeaders;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/villains")
@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.0.7:3000" })
public class VillainController {

    @Autowired
    private VillainService villainService;

    @Autowired
    private VillainRepository villainRepository;


    private final String imageBasePath = "/path/to/your/images"; // 이미지 저장 경로 지정


    /**
     * 빌런 이미지 업로드 및 저장
     * 
     * @param villainName      빌런 이름
     * @param category         사용자가 선택한 카테고리
     * @param boardId          연관된 보드 ID
     * @param files            업로드할 파일 리스트
     * @return 성공 또는 실패 메시지
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadVillainImages(
            @RequestParam String villainName,
            @RequestParam String category,
            @RequestParam Long boardId,
            @RequestPart List<MultipartFile> files) {
        try {
            // 이미 존재하는 boardId 확인
            if (villainRepository.existsByBoardId(boardId)) {
                throw new IllegalArgumentException("이미 존재하는 보드 ID입니다.");
            }

            // 서비스 호출
            villainService.saveVillainImages(villainName, category, boardId, files);
            return ResponseEntity.ok("업로드 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 상태 코드 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업로드 실패");
        }
    }

    /**
     * 보드 ID 중복 확인
     * 
     * @param boardId 확인할 보드 ID
     * @return 보드 ID의 존재 여부
     */
    @GetMapping("/checkBoardId")
    public ResponseEntity<?> checkBoardId(@RequestParam Long boardId) {
        boolean exists = villainRepository.existsByBoardId(boardId);
        return ResponseEntity.ok().body(exists);
    }

 /**
     * 카테고리별 빌런 목록 가져오기
     * 
     * @param category 카테고리
     * @return 해당 카테고리의 빌런 목록
     */
    @GetMapping
    public ResponseEntity<List<Villain>> getVillainsByCategory(@RequestParam String category) {
        List<Villain> villains = villainService.getVillainsByCategory(category);
        return ResponseEntity.ok(villains);
    }

    // @GetMapping("/images")
    // public ResponseEntity<Resource> getImage(@RequestParam String path) {
    //     try {
    //         // 이미지 파일 경로 생성
    //         Path imagePath = Paths.get(imageBasePath).resolve(path).normalize();
    //         Resource resource = new UrlResource(imagePath.toUri());
    
    //         if (resource.exists() && resource.isReadable()) {
    //             return ResponseEntity.ok()
    //                     .contentType(MediaType.IMAGE_JPEG) // 파일 타입에 맞게 변경 가능
    //                     .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
    //                     .body(resource);
    //         } else {
    //             return ResponseEntity.notFound().build();
    //         }
    //     } catch (MalformedURLException e) {
    //         return ResponseEntity.badRequest().build();
    //     }
    // }
    
}
