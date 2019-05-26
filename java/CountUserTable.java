package com.gitee.bullshit.code;

import org.nutz.dao.*;
import org.nutz.ioc.loader.annotation.*;
import junit.framework.*;

/**
  统计用户总数,来源于N年前某个用户抱怨执行效率低,贴出类似逻辑的代码.
  @author wendal
*/
@IocBean
public class CountUserTable {

    @Inject Dao dao;

    @Test
    public void test_user_count() {
        // 取出全部用户对象(dao.query返回List<User>),然后取list大小就可以啦,是不是很机智
        int count = dao.query(User.class, null).size();
        System.out.println("用户总数是" + count);
    } 
}