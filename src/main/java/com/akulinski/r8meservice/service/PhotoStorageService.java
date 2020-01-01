package com.akulinski.r8meservice.service;


import com.akulinski.r8meservice.domain.User;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorageService {

    String storeProfilePicture(MultipartFile file, User user);

    String getLinkForUser(User user);

}
