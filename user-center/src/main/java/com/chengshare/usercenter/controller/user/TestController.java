package com.chengshare.usercenter.controller.user;

import com.chengshare.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author fate7
 * @Date 2020/4/10 3:50 下午
 **/
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    //q?id=1&wxId=aaa&...
    @GetMapping("/q")
    public User query(User user) {
        return user;
    }
}
