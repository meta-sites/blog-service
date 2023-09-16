package com.blog.controllers;

import com.blog.exception.UserException;
import com.blog.models.EmailPreference;
import com.blog.services.EmailPreferenceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class EmailPreferencesController {

    private EmailPreferenceService emailPreferenceService;

    public EmailPreferencesController(EmailPreferenceService emailPreferenceService) {
        this.emailPreferenceService = emailPreferenceService;
    }

    @PostMapping("/public/api/subscribe-email-preferences")
    public ResponseEntity subscribeEmailPreferences(@RequestBody EmailPreference emailPreference) throws UserException, JsonProcessingException {
        emailPreferenceService.sendMailVerified(emailPreference);
        return ResponseEntity.ok("Đăng ký email preferences thành công");
    }

    @GetMapping("/public/api/subscribe-email-preferences")
    public ResponseEntity<String> verifiedMail(@RequestParam String data) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(emailPreferenceService.verified(data), headers, HttpStatus.OK);
    }

    @GetMapping("/public/api/cancel-subscribe-email-preferences")
    public ResponseEntity<String> cancelSubscribeEmailPreferences(@RequestParam String data) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(emailPreferenceService.cancelSubscribeEmailPreferences(data), headers, HttpStatus.OK);
    }
}