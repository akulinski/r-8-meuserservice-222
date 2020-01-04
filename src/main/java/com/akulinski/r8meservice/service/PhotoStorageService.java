package com.akulinski.r8meservice.service;


import com.akulinski.r8meservice.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoStorageService {

    String storeProfilePicture(MultipartFile file, User user);

    String getLinkForUser(User user);

    String storeQuestionPhoto(User user, String question, MultipartFile file) throws IOException;
}
