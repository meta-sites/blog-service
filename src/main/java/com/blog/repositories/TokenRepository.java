package com.blog.repositories;

import com.blog.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends JpaRepository< Token, String> {

    @Query("SELECT T from Token T " +
            "INNER JOIN User as U ON T.userId = U.id " +
            " WHERE U.userName = :userName")
    Optional<Token> findByUserName(String userName);

    @Modifying
    @Query(value = "DELETE FROM tokens WHERE user_id IN (SELECT id FROM user_entity WHERE user_name = :userName)", nativeQuery = true)
    void deleteAllByUserName(@Param("userName") String userName);
}
