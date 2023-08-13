package com.blog.models;

import com.blog.enums.PdfTypeEnum;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "pdf_file")
public class PdfFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private PdfTypeEnum fileType;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Long price;

    @Column(name = "tags")
    private String tags;

    @Column(name = "author")
    private String author;

    @Column(name = "num_sub")
    private Long numSub;

    @Column(name = "logo")
    private String logoPath;

    @Column(name = "is_publish")
    private Boolean isPublic;
}
