package com.audit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MailRequest {
    private String[] toMail;
    private String[] cc;
    private String[] bcc;
    private String subject;
    private String content;
    private String fullName;
    private String username;
    private String password;
    private String time;
    private String location;
    private String auditor;
}
