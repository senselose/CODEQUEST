package com.codequest.backend.dto;

import org.springframework.web.multipart.MultipartFile;

public class BoardRequestDto {
    private String nickname;
    private String title;
    private String content;
    private String location;
    private String hashtags;
    private String category;
    private boolean isHidden;
    private MultipartFile image;

    
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getHashtags() {
        return hashtags;
    }
    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public boolean isHidden() {
        return isHidden;
    }
    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }
    public MultipartFile getImage() {
        return image;
    }
    public void setImage(MultipartFile image) {
        this.image = image;
    }

    // Getters and Setters
    

}


