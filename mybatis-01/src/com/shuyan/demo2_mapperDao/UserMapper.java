package com.shuyan.demo2_mapperDao;

import com.shuyan.po.User;

public interface UserMapper {
    public User findUserById(int id) throws Exception;

    public void insertUserAndGetId(User user) throws Exception;

    public User findUserByIdResultMap(int id) throws Exception;
}
