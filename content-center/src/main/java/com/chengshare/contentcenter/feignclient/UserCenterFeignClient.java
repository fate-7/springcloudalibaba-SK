package com.chengshare.contentcenter.feignclient;

import com.chengshare.contentcenter.domain.dto.user.UserAddBonseDTO;
import com.chengshare.contentcenter.domain.dto.user.UserDTO;
import com.chengshare.contentcenter.feignclient.fallbackFactory.UserCenterFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author fate7
 * @Date 2020/4/10 1:56 下午
 **/

@FeignClient(
        name = "user-center",
        fallbackFactory = UserCenterFeignClientFallbackFactory.class //可以拿到异常
)
public interface UserCenterFeignClient {

    /**
     * 调用user-center服务的url
     * http://user-center/users/{id}
     *
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    UserDTO findById(@PathVariable Integer id);


    @PutMapping("/users/add-bonus")
    UserDTO addBonus(@RequestBody UserAddBonseDTO userAddBonseDTO);
}
