package com.akulinski.r8meservice.config.cloud;

import com.google.cloud.storage.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Google cloud storage related config
 */
@Configuration
public class GCPStorageConfig {
    private final Logger log = LoggerFactory.getLogger(GCPStorageConfig.class);

    private final Storage storage;

    private final Environment environment;

    public GCPStorageConfig(Storage storage, Environment environment) {
        this.storage = storage;
        this.environment = environment;
    }

    /**
     * Creates user images bucket
     * <p>
     * If name is not available adds random string  to name until bucket is created
     *
     * @return user img bucket
     */
    @Bean
    @Qualifier("userImgBucket")
    public Bucket userImgBucket() {
        var profiles = Arrays.asList(environment.getActiveProfiles()).stream().collect(Collectors.joining("_"));

        final var bucketName = "r8me_user_img_" + profiles;

        final long count = checkIfBucketExistsInGCPAccount(bucketName);


        if (count == 0) {
            BucketInfo bucketInfo = BucketInfo.newBuilder(bucketName)
                .setAcl(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER), Acl.of(Acl.User.ofAllAuthenticatedUsers(), Acl.Role.WRITER), Acl.of(Acl.User.ofAllAuthenticatedUsers(), Acl.Role.OWNER)))
                .setName(bucketName).build();
            Bucket bucket = createBucket(bucketName, bucketInfo);

            return bucket;
        }

        return storage.get(bucketName);
    }

    /**
     * Creates bucket with given name but if name is not available adds random string
     *
     * @param bucketName
     * @param bucketInfo
     * @return
     */
    private Bucket createBucket(String bucketName, BucketInfo bucketInfo) {
        Bucket bucket = null;

        while (bucket == null) {
            try {
                bucket = storage.create(bucketInfo);
            } catch (StorageException st) {
                log.error(st.getLocalizedMessage());
                log.error(String.format("Bucket creation failed for name: %s trying new name", bucketInfo.getName()));
                bucketInfo = BucketInfo.newBuilder(bucketName + RandomStringUtils.randomAlphabetic(20).toLowerCase())
                    .setName(bucketName + RandomStringUtils.randomAlphabetic(20).toLowerCase()).build();
            }
        }
        return bucket;
    }

    private long checkIfBucketExistsInGCPAccount(String bucketName) {
        var buckets = Lists.newArrayList(storage.list().iterateAll());

        return buckets.stream().filter(bucket -> bucket.getName().equals(bucketName)).count();
    }

}
