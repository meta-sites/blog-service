package com.blog.dto;

import com.blog.enums.ArticleEnum;
import com.blog.models.Comment;
import com.blog.models.User;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;

@Data
public class ArticleDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String description;
    private String tags;
    private String imageUrl;
    private String contentsDetail;
    private Long numViews;
    private Long numShares;
    private String urlFriendly;
    private ArticleEnum type;
    private List< CommentDto > comments;
    private Boolean isLikes;
}
