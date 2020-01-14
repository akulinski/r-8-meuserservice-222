package com.akulinski.r8meservice.repository.search;

import com.akulinski.r8meservice.domain.NotificaitonBoard;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationBoardRepository extends ElasticsearchCrudRepository<NotificaitonBoard, String> {

    Optional<NotificaitonBoard> findByProfileId(long profileId);

}

