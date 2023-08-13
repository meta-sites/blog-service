package com.blog.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Date createAt;

    protected String createBy;

    protected String modifiedBy;

    protected Date modifiedAt;
}
