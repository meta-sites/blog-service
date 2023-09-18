package com.blog.repositories;

import com.blog.dto.CommentDto;
import com.blog.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository< Comment, String> {
    @Query("SELECT c.articleId, COUNT(c.id) FROM Comment c WHERE c.articleId IN :ids GROUP BY c.articleId")
    List<Object[]> countCommentByListArticleId(List<String> ids);

    @Query("SELECT new com.blog.dto.CommentDto(c.articleId, c.content, u.name, u.imagePath, c.createAt) FROM Comment c" +
            " JOIN User u ON u.id = c.createBy " +
            " WHERE c.articleId = :id")
    List< CommentDto > findByArticleId(String id);

    @Query(value = "SELECT a.type, COUNT(*) FROM article a \n" +
            "JOIN comment c ON c.article_id = a.id\n" +
            "GROUP BY a.type", nativeQuery = true)
    List<Object[]> countArticleByType();
}
