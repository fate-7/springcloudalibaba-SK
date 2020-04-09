package com.chengshare.usercenter;


import com.chengshare.usercenter.dao.user.UserMapper;
import com.chengshare.usercenter.domain.entity.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@MapperScan(basePackages = "com.chengshare")
public class UserCenterApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testUserInsert() {
        User user = new User();
        user.setAvatarUrl("xxx");
        user.setBonus(100);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.insertSelective(user);
    }

}
