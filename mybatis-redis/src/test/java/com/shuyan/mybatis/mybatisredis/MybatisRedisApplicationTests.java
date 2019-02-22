package com.shuyan.mybatis.mybatisredis;

import com.shuyan.mybatis.mybatisredis.business.bean.RegionEntity;
import com.shuyan.mybatis.mybatisredis.business.mapper.RegionMapper;
import lombok.Synchronized;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static com.sun.scenario.Settings.set;

@RunWith(SpringRunner.class)
@SpringBootTest
@SuppressWarnings("all")
public class MybatisRedisApplicationTests {
    @Autowired
    private RegionMapper regionMapper;
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    @Test
    public void contextLoads() {
        RegionEntity region = regionMapper.getRegionById(2L);
        System.out.println(region);
    }

    @Test
    public void test(){
        ValueOperations<Object,Object> opsForValue = redisTemplate.opsForValue();
        opsForValue.set("hello","world");
        Object hello = redisTemplate.opsForValue().get("hello");
        System.out.println(hello);
    }
}
