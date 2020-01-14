package com.akulinski.r8meservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "scheduler_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerLog extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "last_run")
    private Instant timestamp = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "scheduler_type")
    private SchedulerType schedulerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "scheduler_status")
    private SchedulerStatus schedulerStatus;
}
