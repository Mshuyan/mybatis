package com.shuyan.demo1;

import com.shuyan.po.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class UserTest {
    @Test
    public void findUserById() throws IOException {
        //读取SqlMapConfig.xml全局配置文件，文件名基于resources目录
        InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        //创建回话工厂
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //通过工程得到sqlsession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        /*
         * 通过sqlsession执行sql语句
         * selectOne:查询1条记录
         * 参数：
         *      参数1：命名空间+.+statementID,通过命名空间和statementID就可以找到要执行的sql
         *      参数2：输入参数，对应该sql中的占位符，参数类型对应该sql的parameterType
         * 返回值：
         *      PO类对象，类型对应该sql的resultType
         * 注意：
         *      当查询结果为多条记录时，不能使用selectOne进行查询
         */
        User user = sqlSession.selectOne("test.findUserById", 1);
        System.out.println(user);
        //关闭sqlsession
        sqlSession.close();
    }

    @Test
    public void findUserByUsernameLike() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        /*
         * selectList：查询多条记录
         */
        List<User> list = sqlSession.selectList("test.findUserByUsernameLike", "小明");
        for (User user: list) {
            System.out.println(user);
        }
        sqlSession.close();
    }

    @Test
    public void insertUser() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User("小红",new Date(),"男","立水桥");
        /*
         * insert：插入数据
         */
        sqlSession.insert("test.insertUser", user);
        //对数据库有更改的操作需要提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void insertUserAndGetId() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User("小蓝",new Date(),"男","分钟寺");
        /*
         * insert：插入数据
         */
        sqlSession.insert("test.insertUserAndGetId", user);
        //对数据库有更改的操作需要提交事务
        sqlSession.commit();
        //因为插入操作后执行了查询主键并保存到user对象中的操作，所以这里user对象的id就是插入后返回的id
        //User{id=29, username='小蓝', birthday=Fri May 11 10:11:01 CST 2018, sex='男', address='分钟寺'}
        System.out.println(user);
        sqlSession.close();
    }

    @Test
    public void deleteUserById() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        /*
         * delete：删除数据
         * 删除user表中id为29的记录
         */
        sqlSession.delete("test.deleteUserById", 29);
        //对数据库有更改的操作需要提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void updateUserById() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User("小蓝",new Date(),"男","回龙观");
        //根据id修改数据时，传入的对象必须设置要修改的记录的id
        user.setId(28);
        /*
         * update：修改数据
         */
        sqlSession.update("test.updateUserById", user);
        //对数据库有更改的操作需要提交事务
        sqlSession.commit();
        sqlSession.close();
    }
}
