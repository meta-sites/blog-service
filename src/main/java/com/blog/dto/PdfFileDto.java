package com.blog.dto;
import com.blog.enums.PdfTypeEnum;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class PdfFileDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String fileName;
    private Long fileSize;
    private PdfTypeEnum fileType;
    private String description;
    private Long price;
    private String author;
    private String tags;
    private Long numSub;
    private String logoPath;
}
