package com.shuyan.demo3_mappersLoad;

import com.shuyan.po.User;

public interface UserMapper {
    public User findUserById(int id) throws Exception;

    public void insertUserAndGetId(User user) throws Exception;
}
