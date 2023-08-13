package com.blog.models;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "content")
    private String content;

}
