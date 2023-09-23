package com.blog.models;

import com.blog.enums.ArticleEnum;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "article")
public class Article extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "tags")
    private String tags;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "contents_detail")
    private String contentsDetail;

    @Column(name = "num_views")
    private Long numViews;

    @Column(name = "num_shares")
    private Long numShares;

    @Column(name = "num_like")
    private Long numLike;

    @Column(name = "url_friendly")
    private String urlFriendly;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ArticleEnum type;
}
