package com.shuyan.mybatis;

import com.shuyan.mybatis.business.bean.RegionEntity;
import com.shuyan.mybatis.business.mapper.RegionMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisApplicationTests {
    @Autowired
    private RegionMapper regionMapper;

    @Test
    public void contextLoads() {
        RegionEntity region = regionMapper.getRegionById(2L);
        System.out.println(region);
    }

}
