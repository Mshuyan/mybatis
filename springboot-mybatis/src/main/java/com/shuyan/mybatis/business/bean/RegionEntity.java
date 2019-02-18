package com.shuyan.mybatis.business.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author will
 */
@Data
public class RegionEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Date gmtModified;
}
