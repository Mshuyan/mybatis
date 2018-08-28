package com.shuyan.demo2_mapperDao;

import com.shuyan.po.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class UserMapperDaoTest {
    InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

    public UserMapperDaoTest() throws IOException {
    }

    @Test
    public void UserMapperDaoTest1() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //通过sqlSession获得代理对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //调用UserMapper中的方法
        User user = userMapper.findUserById(28);
        System.out.println(user);
        sqlSession.close();
    }

    @Test
    public void UserMapperDaoTest2() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //通过sqlSession获得代理对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //调用UserMapper中的方法
        User user = new User("小黄",new Date(),"女","十里河");
        userMapper.insertUserAndGetId(user);
        //仍然需要commit
        sqlSession.commit();
        System.out.println(user);
        sqlSession.close();
    }

    @Test
    public void UserMapperDaoTest3() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //通过sqlSession获得代理对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //调用UserMapper中的方法
        User user = userMapper.findUserByIdResultMap(1);
        System.out.println(user);
        sqlSession.close();
    }
}
