package com.codequest.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.codequest.backend.entity.Villain;
import com.codequest.backend.service.VillainService;

import java.util.List;

@RestController
@RequestMapping("/api/villains")
@CrossOrigin(origins = { "http://localhost:3000", "http://192.168.0.7:3000" })
public class VillainController {

    private final VillainService villainService;

    @Autowired
    public VillainController(VillainService villainService) {
        this.villainService = villainService;
    }

    /**
     * 빌런 이미지 업로드 API
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadVillainImages(
            @RequestParam("villainName") String villainName,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            villainService.saveVillainImages(villainName, files);
            return ResponseEntity.ok("이미지 업로드 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("이미지 업로드 실패: " + e.getMessage());
        }
    }

}
