package com.chengshare.contentcenter.feignclient;

import com.chengshare.contentcenter.domain.dto.user.UserDTO;
import com.chengshare.contentcenter.feignclient.fallback.UserCenterFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author fate7
 * @Date 2020/4/10 3:42 下午
 **/
@FeignClient(name = "user-center")
public interface TestFeignClient {

    //多参数
    @GetMapping(value = "/q")
    public UserDTO query(@SpringQueryMap UserDTO userDTO);
}
