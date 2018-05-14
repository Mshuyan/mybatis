package com.shuyan.demo4_dynamicSql;

import com.shuyan.po.User;
import com.shuyan.po.UserCustom;

import java.util.List;

public interface UserMapper {
    public List<User> findUserByCondition0(User user) throws Exception;
    public List<User> findUserByCondition1(User user) throws Exception;
    public List<User> findUserByCondition2(User user) throws Exception;
    public List<User> findUserByCondition3(UserCustom uc) throws Exception;
    public List<User> findUserByCondition4(UserCustom uc) throws Exception;
}
