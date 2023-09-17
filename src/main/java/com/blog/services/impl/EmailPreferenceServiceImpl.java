package com.blog.services.impl;

import com.blog.models.EmailPreference;
import com.blog.repositories.EmailPreferenceRepository;
import com.blog.services.EmailPreferenceService;
import com.blog.services.MailService;
import com.blog.services.ResourceService;
import com.blog.util.AESCrypt;
import com.blog.util.Constants;
import com.blog.util.ResourceConstants;
import com.blog.util.MailConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import javax.transaction.Transactional;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class EmailPreferenceServiceImpl implements EmailPreferenceService {

    private EmailPreferenceRepository emailPreferenceRepository;
    private MailService mailService;
    private ResourceService resourceService;

    public EmailPreferenceServiceImpl(EmailPreferenceRepository emailPreferenceRepository, MailService mailService, ResourceService resourceService) {
        this.emailPreferenceRepository = emailPreferenceRepository;
        this.mailService = mailService;
        this.resourceService = resourceService;
    }

    @Override
    @Transactional
    public void insert(EmailPreference emailPreference) {
        emailPreferenceRepository.save(emailPreference);
    }

    @Override
    @Transactional
    public String verified(String data) throws Exception {
        String decrypt = AESCrypt.decrypt(data);
        List<String> dataList = Arrays.asList(decrypt.split(Constants.HYPHEN));
        String email = dataList.get(0);
        long expired = Long.valueOf(dataList.get(1));
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("link", resourceService.getResourcePath(ResourceConstants.SERVER_DOMAIN_URL));

        if (!IsExpireTime(expired)) {
            return mailService.getTemplate(MailConstant.VERIFIED_RECEIVE_MAIL_EXPIRED_TEMPLATE, context);
        };
        emailPreferenceRepository.verified(email);
        return mailService.getTemplate(MailConstant.VERIFIED_RECEIVE_MAIL_SUCCESS_TEMPLATE, context);
    }

    @Override
    @Transactional
    public String cancelSubscribeEmailPreferences(String email) throws Exception {
        String decryptEmail = AESCrypt.decrypt(email);
        Context context = new Context();
        context.setVariable("email", decryptEmail);
        context.setVariable("link", resourceService.getResourcePath(ResourceConstants.SERVER_DOMAIN_URL));
        emailPreferenceRepository.cancel(decryptEmail);
        return mailService.getTemplate(MailConstant.CANCEL_RECEIVE_MAIL_TEMPLATE, context);
    }


    @Override
    public List< EmailPreference > findEmailPreferencesByVerified() {
        return emailPreferenceRepository.findAllByVerified(true);
    }

    @Override
    @Transactional
    public void sendMailVerified(EmailPreference emailPreference) {
        emailPreference.setVerified(false);
        insert(emailPreference);
        try {
            sendMailToVerifier(emailPreference.getEmail());
        } catch (Exception e) {
           log.error("Không thể gửi mail tới " + emailPreference.getEmail());
        }
    }

    private void sendMailToVerifier(String email) throws Exception {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("link", generateVerifiedLink(email));
        setContextFooter(context, email);
        mailService.sendEmailWithTemplate(email,MailConstant.VERIFIED_RECEIVE_MAIL_SUBJECT, MailConstant.VERIFIED_RECEIVE_MAIL_TEMPLATE, context);
    }

    private void setContextFooter(Context context, String email) throws Exception {
        context.setVariable("unsubscribeLink", generateUnsubscribe(email));
    }

    private String generateVerifiedLink(String email) throws Exception {
        return new StringBuilder(resourceService.getResourcePath(ResourceConstants.SERVER_DOMAIN_URL))
                .append(Constants.SLASH)
                .append("public/api/subscribe-email-preferences")
                .append(Constants.QUESTION_MARK)
                .append("data=")
                .append(buildDataVerified(email))
                .toString();

    }

    @Override
    public String generateUnsubscribe(String email) throws Exception {
        return new StringBuilder(resourceService.getResourcePath(ResourceConstants.SERVER_DOMAIN_URL))
                .append(Constants.SLASH)
                .append("public/api/cancel-subscribe-email-preferences")
                .append(Constants.QUESTION_MARK)
                .append("data=")
                .append(buildDataEmailCancel(email))
                .toString();
    }

    private String buildDataEmailCancel(String email) throws Exception {
        String data = new StringBuilder(email)
                .toString();
        return URLEncoder.encode(AESCrypt.encrypt(data), StandardCharsets.UTF_8.toString());
    }

    private String buildDataVerified(String email) throws Exception {
        String data = new StringBuilder(email)
                .append(Constants.HYPHEN)
                .append(generateExpireTime())
                .toString();
        return URLEncoder.encode(AESCrypt.encrypt(data), StandardCharsets.UTF_8.toString());
    }

    private long generateExpireTime() {
        Instant now = Instant.now();
        Instant fiveMinutesLater = now.plusSeconds(5 * 60);
        return fiveMinutesLater.toEpochMilli();
    }

    private boolean IsExpireTime(long expire) {
        return Instant.now().toEpochMilli() < expire;
    }
}