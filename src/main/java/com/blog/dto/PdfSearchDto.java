package com.blog.dto;

import com.blog.enums.PdfTypeEnum;
import lombok.Data;

@Data
public class PdfSearchDto {
    private PdfTypeEnum type;
    private String textSearch;
    private Integer numsPerPage;
    private Integer pageNumber;
    private String fileName;
    private String name;
    private boolean filterBySubscribe;
}
