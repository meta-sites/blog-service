package com.blog.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "`like`")
public class Like extends BaseEntity {

    @Column(name = "article_id")
    private String articleId;
}
