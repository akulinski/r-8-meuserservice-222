package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.security.AuthoritiesConstants;
import com.akulinski.r8meservice.service.R8Meuserservice2KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/r-8-meuserservice-2-kafka")
@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
public class R8Meuserservice2KafkaResource {

    private final Logger log = LoggerFactory.getLogger(R8Meuserservice2KafkaResource.class);

    private R8Meuserservice2KafkaProducer kafkaProducer;

    public R8Meuserservice2KafkaResource(R8Meuserservice2KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.send(message);
    }
}
