package com.chengshare.usercenter.service.user;

import com.chengshare.usercenter.dao.user.UserMapper;
import com.chengshare.usercenter.domain.dto.user.UserLoginDTO;
import com.chengshare.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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


    public User login(UserLoginDTO loginDTO, String openId) {
        User user = this.userMapper.selectOne(
                User.builder().wxId(openId).build()
        );
        if (user == null) {
            User userToSave = User.builder()
                    .wxId(openId)
                    .bonus(300)
                    .wxNickname(loginDTO.getWxNickname())
                    .avatarUrl(loginDTO.getAvatarUrl())
                    .roles("user")
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build();
            this.userMapper.insertSelective(userToSave);
            return userToSave;
        }
        return user;
    }

}
