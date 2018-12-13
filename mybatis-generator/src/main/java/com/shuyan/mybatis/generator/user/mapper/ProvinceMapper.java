package com.shuyan.mybatis.generator.user.mapper;

import com.shuyan.mybatis.generator.user.dto.Province;
import java.util.List;

public interface ProvinceMapper {
    int insert(Province record);

    List<Province> selectAll();

    int updateByPrimaryKey(Province record);
}