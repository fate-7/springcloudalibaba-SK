package com.chengshare.contentcenter.controller;

import com.chengshare.contentcenter.domain.dto.user.UserDTO;
import com.chengshare.contentcenter.feignclient.TestBaiduFeignClient;
import com.chengshare.contentcenter.feignclient.TestFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author fate7
 * @Date 2020/4/10 3:44 下午
 **/

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    private final TestFeignClient testFeignClient;

    private final TestBaiduFeignClient testBaiduFeignClient;

    @GetMapping("test-get")
    public UserDTO query(UserDTO userDTO) {
        return testFeignClient.query(userDTO);
    }

    @GetMapping("baidu")
    public String baidu() {
        return testBaiduFeignClient.index();
    }
}
