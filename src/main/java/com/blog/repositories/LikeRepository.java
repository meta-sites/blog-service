package com.blog.repositories;

import com.blog.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository< Like, String> {

    @Query("SELECT CASE WHEN COUNT(L) > 0 THEN true ELSE false END FROM Like L where L.articleId = :articleId and L.createBy = :userId")
    Boolean isLike(String articleId, String userId);

    @Modifying
    @Query("DELETE FROM Like L WHERE L.articleId = :articleId AND L.createBy = :userId")
    void disLikeArticle(String articleId, String userId);
}
