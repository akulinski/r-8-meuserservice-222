package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.config.cloud.GCPStorageConfig;
import com.akulinski.r8meservice.domain.User;
import com.akulinski.r8meservice.repository.UserRepository;
import com.akulinski.r8meservice.repository.search.UserSearchRepository;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class GCPPhotoStorageService implements PhotoStorageService {

    private final Logger log = LoggerFactory.getLogger(GCPStorageConfig.class);

    private final Bucket bucket;

    private final UserRepository userRepository;

    private final UserSearchRepository userSearchRepository;

    public GCPPhotoStorageService(@Qualifier("userImgBucket") Bucket bucket, UserRepository userRepository, UserSearchRepository userSearchRepository) {
        this.bucket = bucket;
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
    }

    /**
     * Stores profile picture for user
     *  @param file img if File object
     * @param user User
     * @return
     */
    @Override
    public String storeProfilePicture(MultipartFile file, User user) {

        try {

            final var link = uploadFile(file, user.getLogin() + "/profile/");

            user.setImageUrl(link);
            userRepository.save(user);
            userSearchRepository.save(user);
            return link;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }

        return "";
    }

    @Override
    public String getLinkForUser(User user) {
        return null;
    }

    /**
     * Uploads a file to Google Cloud Storage to the bucket specified in the BUCKET_NAME
     * environment variable, appending a timestamp to end of the uploaded filename.
     */
    private String uploadFile(MultipartFile file, final String folder) throws IOException {

        final DateTimeFormatter dtf = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");
        final DateTime dt = DateTime.now(DateTimeZone.UTC);
        final String dtString = dt.toString(dtf);

        final String fileName = file.getName() + dtString;

        final InputStream is = file.getInputStream();

        final var blobWriteOption = Bucket.BlobWriteOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ);

        BlobInfo blobInfo = bucket.create(folder+fileName+".jpg", is, blobWriteOption);
        return blobInfo.getMediaLink();
    }
}
