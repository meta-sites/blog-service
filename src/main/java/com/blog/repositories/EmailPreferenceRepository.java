package com.blog.repositories;

import com.blog.models.EmailPreference;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface EmailPreferenceRepository extends CrudRepository< EmailPreference, String> {

    List<EmailPreference> findAllByVerified(boolean verified);

    @Modifying
    @Query(value = "UPDATE email_preference SET verified = true WHERE email = :email", nativeQuery = true)
    void verified(String email);

    @Modifying
    @Query(value = "UPDATE email_preference SET verified = false WHERE email = :email", nativeQuery = true)
    void cancel(String email);
}