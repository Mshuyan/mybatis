package com.shuyan.po;

import com.shuyan.po.User;

import java.util.ArrayList;
import java.util.List;

public class UserCustom {
    private List<Integer> ids;
    private User user;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserCustom(){

    }
    public UserCustom(User user,int ...args){
        this.user = user;
        ids = new ArrayList();
        for (int id:args) {
            ids.add(id);
        }
    }

}
