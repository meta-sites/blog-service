package com.blog.services;
import com.blog.models.EmailPreference;
import java.util.List;

public interface EmailPreferenceService {
    void insert(EmailPreference emailPreference);

    String verified(String data) throws Exception;

    String cancelSubscribeEmailPreferences(String email) throws Exception;

    List<EmailPreference> findEmailPreferencesByVerified();

    void sendMailVerified(EmailPreference emailPreference);

    String generateUnsubscribe(String email) throws Exception;
}