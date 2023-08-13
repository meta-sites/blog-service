package com.blog.repositories;

import com.blog.dto.ArticleShortDto;
import com.blog.enums.ArticleEnum;
import com.blog.models.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository< Article, String> {

    Article findByUrlFriendly(String urlFriendly);

    @Query(value = "SELECT A.id, A.title, A.description, A.tags, A.image_url as imageUrl , " +
            " A.num_views as numViews, A.num_shares as numShares, A.url_friendly as urlFriendly, A.type, A.create_at as createAt  FROM article A where MATCH(title, description) AGAINST (:textSearch)", nativeQuery = true)
    List<Object[]> getFullTextSearch(String textSearch, Pageable pageable);

    @Query("SELECT new com.blog.dto.ArticleShortDto(A.id, A.title, A.description, A.tags, A.imageUrl, " +
            "  A.numViews, A.numShares, A.urlFriendly, A.type, A.createAt)  from Article A where A.type = :type order by A.createAt desc")
    List<ArticleShortDto> getByType(ArticleEnum type, Pageable pageable);

    @Query("SELECT new com.blog.dto.ArticleShortDto(A.id, A.title, A.description, A.tags, A.imageUrl," +
            " A.numViews, A.numShares, A.urlFriendly, A.type, A.createAt) from Article A order by A.numViews desc")
    List< ArticleShortDto > getOrderByViews(Pageable pageable);

    @Query("SELECT new com.blog.dto.ArticleShortDto(A.id, A.title, A.description, A.tags, A.imageUrl," +
            " A.numViews, A.numShares, A.urlFriendly, A.type, A.createAt) from Article A order by A.createAt desc")
    List<ArticleShortDto> getOrderByTime(Pageable pageable);

    @Query("SELECT new com.blog.dto.ArticleShortDto(A.id, A.title, A.description, A.tags, A.imageUrl," +
            " A.numViews, A.numShares, A.urlFriendly, A.type, A.createAt) from Article A WHERE A.tags like %:tag%")
    List<ArticleShortDto> getOrderByTags(String tag, Pageable pageable);

    @Query(value = "SELECT A.type, count(*), SUM(A.num_views) FROM article A group by A.type", nativeQuery = true)
    List<Object[]> countArticleByType();
}
