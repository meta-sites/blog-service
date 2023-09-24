package com.blog.dto;

import com.blog.enums.ArticleEnum;
import com.blog.util.UtilFunction;
import lombok.Data;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Data
public class ArticleShortDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String description;
    private String tags;
    private String imageUrl;
    private Long numViews;
    private Long numShares;
    private Long numLike;
    private String urlFriendly;
    private Long numComments;
    private ArticleEnum type;

    public ArticleShortDto(){}

    public ArticleShortDto(String id, String title, String description, String tags, String imageUrl,
    Long numViews, Long numShares, String urlFriendly, ArticleEnum type, Date createAt, Long numLike) throws ParseException {
        this.id = id;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.imageUrl = imageUrl;
        this.numViews = numViews;
        this.numShares = numShares;
        this.urlFriendly = urlFriendly;
        this.type = type;
        this.createAt = createAt;
        this.numLike = numLike;
    }
}