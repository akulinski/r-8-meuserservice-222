package com.akulinski.r8meservice.service.dto;

public class EmailChangeDTO {

    private String newEmail;

    public EmailChangeDTO(){

    }

    public EmailChangeDTO(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
}
