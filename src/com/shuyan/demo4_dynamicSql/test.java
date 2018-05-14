package com.shuyan.demo4_dynamicSql;

import com.shuyan.po.User;
import com.shuyan.po.UserCustom;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class test {
    InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

    public test() throws IOException {
    }

    @Test
    public void findUserByCondition0Test() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //通过sqlSession获得代理对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //调用UserMapper中的方法
        User user = new User();
        user.setUsername("陈小明");
        user.setSex("1");
        List<User> userList = userMapper.findUserByCondition0(user);
        System.out.println(userList);
        sqlSession.close();
    }

    @Test
    public void findUserByCondition1Test() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //通过sqlSession获得代理对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //调用UserMapper中的方法
        User user = new User();
        user.setUsername("陈小明");
        user.setSex("1");
        List<User> userList = userMapper.findUserByCondition1(user);
        System.out.println(userList);
        sqlSession.close();
    }

    @Test
    public void findUserByCondition2Test() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //通过sqlSession获得代理对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //调用UserMapper中的方法
        User user = new User();
        user.setUsername("陈小明");
        user.setSex("1");
        List<User> userList = userMapper.findUserByCondition2(user);
        System.out.println(userList);
        sqlSession.close();
    }

    @Test
    public void findUserByCondition3Test() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //通过sqlSession获得代理对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //调用UserMapper中的方法
        User user = new User();
        user.setSex("1");
        UserCustom uc = new UserCustom(user,1,10,16,22);
        List<User> userList = userMapper.findUserByCondition3(uc);
        System.out.println(userList);
        sqlSession.close();
    }

    @Test
    public void findUserByCondition4Test() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //通过sqlSession获得代理对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //调用UserMapper中的方法
        User user = new User();
        user.setSex("1");
        UserCustom uc = new UserCustom(user,1,10,16,22);
        List<User> userList = userMapper.findUserByCondition4(uc);
        System.out.println(userList);
        sqlSession.close();
    }
}
