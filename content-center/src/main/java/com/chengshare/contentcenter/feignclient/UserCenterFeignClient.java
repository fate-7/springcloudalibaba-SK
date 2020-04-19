package com.chengshare.contentcenter.feignclient;

import com.chengshare.contentcenter.configuration.UserCenterFeignConfiguration;
import com.chengshare.contentcenter.domain.dto.user.UserDTO;
import com.chengshare.contentcenter.feignclient.fallback.UserCenterFeignClientFallback;
import com.chengshare.contentcenter.feignclient.fallbackFactory.UserCenterFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author fate7
 * @Date 2020/4/10 1:56 下午
 **/

@FeignClient(
        name = "user-center" ,
//        fallback = UserCenterFeignClientFallback.class,//配置自己的处理
        fallbackFactory = UserCenterFeignClientFallbackFactory.class //可以拿到异常
        )
public interface UserCenterFeignClient {

    /**
     * 调用user-center服务的url
     * http://user-center/users/{id}
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    UserDTO findById(@PathVariable Integer id);
}
