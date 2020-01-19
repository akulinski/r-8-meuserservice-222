package com.akulinski.r8meservice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FollowerDTO implements Serializable {
    private String username;
    private String link;
}
