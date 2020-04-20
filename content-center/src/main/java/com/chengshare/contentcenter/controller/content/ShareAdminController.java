package com.chengshare.contentcenter.controller.content;

import com.chengshare.contentcenter.auth.CheckAuthorization;
import com.chengshare.contentcenter.domain.dto.content.ShareAudioDTO;
import com.chengshare.contentcenter.domain.entity.content.Share;
import com.chengshare.contentcenter.service.content.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author fate7
 * @Date 2020/4/13 2:34 下午
 **/

@RestController
@RequestMapping("/admin/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareAdminController {

    private final ShareService shareService;


    @PutMapping("/audit/{id}")
    @CheckAuthorization("admin")
    public Share auditById(@PathVariable Integer id, @RequestBody ShareAudioDTO audioDTO) {

        //认证授权

        return this.shareService.auditById(id, audioDTO);
    }
}
