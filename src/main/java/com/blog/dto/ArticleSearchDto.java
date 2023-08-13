package com.blog.dto;

import com.blog.enums.ArticleEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleSearchDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String urlFriendly;
    private ArticleEnum type;
    private Integer numsPerPage;
    private Integer pageNumber;
    private String textSearch;
    private Boolean isOrderAllByView;
    private Boolean isOrderAllByTime;
    private String tags;
}
