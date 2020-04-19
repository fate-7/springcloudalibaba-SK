package com.chengshare.contentcenter.controller.content;

import com.chengshare.contentcenter.auth.CheckLogin;
import com.chengshare.contentcenter.domain.dto.content.ShareDTO;
import com.chengshare.contentcenter.service.content.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author fate7
 * @Date 2020/4/7 9:46 下午
 **/
@RestController
@RequestMapping("/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {

    private final ShareService shareService;

    @GetMapping("/{id}")
    @CheckLogin
    public ShareDTO findById(
            @PathVariable Integer id,
            @RequestHeader("X-Token") String token) {
        return shareService.findbyId(id, token);
    }
}
