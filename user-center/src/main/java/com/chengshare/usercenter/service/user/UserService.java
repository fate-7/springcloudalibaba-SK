package com.chengshare.usercenter.service.user;

import com.chengshare.usercenter.dao.user.UserMapper;
import com.chengshare.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author fate7
 * @Date 2020/4/7 9:13 下午
 **/

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserMapper userMapper;

    public User findById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

}
