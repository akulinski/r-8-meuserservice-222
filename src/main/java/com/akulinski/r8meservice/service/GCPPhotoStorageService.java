package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.config.cloud.GCPStorageConfig;
import com.akulinski.r8meservice.domain.User;
import com.akulinski.r8meservice.repository.UserRepository;
import com.akulinski.r8meservice.repository.search.UserSearchRepository;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.common.collect.Lists;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class GCPPhotoStorageService implements PhotoStorageService {

    private final Logger log = LoggerFactory.getLogger(GCPStorageConfig.class);

    private static final String REGEX = "-\\d\\d\\d\\d-\\d\\d-\\d\\d-\\d\\d\\d\\d\\d\\d";

    private final Bucket bucket;

    private final UserRepository userRepository;

    private final UserSearchRepository userSearchRepository;

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmss");

    public GCPPhotoStorageService(@Qualifier("userImgBucket") Bucket bucket, UserRepository userRepository, UserSearchRepository userSearchRepository) {
        this.bucket = bucket;
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
    }

    /**
     * Stores profile picture for user
     *
     * @param file img if File object
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

        throw new IllegalStateException("Photo upload failed ");
    }

    /**
     * Returns Last avatar for user
     * Photos on GCloud are saved with timestamp at the end of filename
     * So to get last photo this function has to parse all the names
     * to dates and return photo with the youngest name
     *
     * @param user
     * @return
     */
    @Override
    public String getLinkForUser(User user) {

        final var list = bucket.list(Storage.BlobListOption.prefix(user.getLogin() + "/profile/"));

        final var pattern = Pattern.compile(REGEX);

        ArrayList<Blob> blobs = Lists.newArrayList(list.iterateAll());

        final var collect = blobs.stream()
            .sorted(Comparator.comparing(parsePhotoNameToDate(pattern)))
            .collect(Collectors.toList());

        if (collect.isEmpty()) {
            log.error("No photos found in bucket for user: {}", user.getLogin());
            throw new IllegalStateException(String.format("No photos found in bucket for user: %s", user.getLogin()));
        }

        return collect.get(collect.size() - 1).getMediaLink();
    }


    private Function<Blob, DateTime> parsePhotoNameToDate(Pattern pattern) {
        return e -> DateTime.parse(getTimestampFromBlob(pattern, e), DATE_TIME_FORMATTER);
    }

    private String getTimestampFromBlob(Pattern pattern, Blob e) {
        Matcher matcher = pattern.matcher(e.getName());
        matcher.find(); //Have to call this before .group()
        return matcher.group();
    }


    @Override
    public String storeQuestionPhoto(User user, String question, MultipartFile file) throws IOException {
        final String fileName = file.getName();

        final InputStream is = file.getInputStream();


        final var blobWriteOption = Bucket.BlobWriteOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ);
        String folder = user.getLogin()+"/"+question+"/";

        BlobInfo blobInfo = bucket.create(folder + fileName + ".jpg", is, blobWriteOption);

        return blobInfo.getMediaLink();
    }

    /**
     * Uploads a file to Google Cloud Storage to the bucket specified in the BUCKET_NAME
     * environment variable, appending a timestamp to end of the uploaded filename.
     */
    private String uploadFile(MultipartFile file, final String folder) throws IOException {

        final DateTimeFormatter dtf = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmss");
        final DateTime dt = DateTime.now(DateTimeZone.UTC);
        final String dtString = dt.toString(dtf);

        final String fileName = file.getName() + dtString;

        final InputStream is = file.getInputStream();

        final var blobWriteOption = Bucket.BlobWriteOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ);

        BlobInfo blobInfo = bucket.create(folder + fileName + ".jpg", is, blobWriteOption);
        return blobInfo.getMediaLink();
    }
}
