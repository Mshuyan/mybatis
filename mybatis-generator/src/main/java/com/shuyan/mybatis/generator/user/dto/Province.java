package com.shuyan.mybatis.generator.user.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class Province implements Serializable {
    private Integer id;

    private String name;

    private Date gmtModified;

    private Date gmtCreate;

    private static final long serialVersionUID = 1L;
}