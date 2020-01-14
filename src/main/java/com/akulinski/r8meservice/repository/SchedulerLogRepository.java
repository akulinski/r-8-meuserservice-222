package com.akulinski.r8meservice.repository;

import com.akulinski.r8meservice.domain.SchedulerLog;
import com.akulinski.r8meservice.domain.SchedulerType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchedulerLogRepository extends CrudRepository<SchedulerLog, Long> {

    Optional<SchedulerLog> findFirstBySchedulerTypeOrderByTimestampDesc(SchedulerType schedulerType);
}
