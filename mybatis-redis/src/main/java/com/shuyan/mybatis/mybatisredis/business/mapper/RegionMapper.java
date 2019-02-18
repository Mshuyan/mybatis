package com.shuyan.mybatis.mybatisredis.business.mapper;

import com.shuyan.mybatis.mybatisredis.business.bean.RegionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author will
 */
public interface RegionMapper {
    /**
     * 根据id获取省份信息
     * @param id 省份id
     * @return 省份信息
     */
    RegionEntity getRegionById(Long id);

    /**
     * 获取所有省份
     * @return 所有省份列表
     */
    List<RegionEntity> getAllRegion();
}
