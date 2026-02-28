package com.meritcap.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ProfileImageService {
    Map<String, Object> uploadProfileImage(String username, MultipartFile file) throws Exception;
}
