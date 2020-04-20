package com.chengshare.contentcenter.controller.content;

import com.chengshare.contentcenter.auth.CheckLogin;
import com.chengshare.contentcenter.domain.dto.content.ShareDTO;
import com.chengshare.contentcenter.domain.dto.content.ShareRequestDTO;
import com.chengshare.contentcenter.domain.entity.content.Share;
import com.chengshare.contentcenter.service.content.ShareService;
import com.chengshare.contentcenter.util.JwtOperator;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author fate7
 * @Date 2020/4/7 9:46 下午
 **/
@RestController
@RequestMapping("/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {

    private final ShareService shareService;

    private final JwtOperator jwtOperator;

    @GetMapping("/{id}")
    @CheckLogin
    public ShareDTO findById(
            @PathVariable Integer id) {
        return shareService.findbyId(id);
    }

    @GetMapping("/q")
    public PageInfo<Share> q(
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestHeader("X-Token") String token) {


        //pageSize需要做控制
        if (pageSize > 100) {
            pageNo = 100;
        }

        Integer userId = null;

        if (StringUtils.isNotBlank(token) && !"[object Undefined]".equals(token)) {
            Claims claims = this.jwtOperator.getClaimsFromToken(token);
            userId = (Integer) claims.get("id");
        }
        return this.shareService.q(title, pageNo, pageSize, userId);
    }

    @GetMapping("/exchange/{id}")
    @CheckLogin
    public Share exchangeById(@PathVariable Integer id, HttpServletRequest request) {
        return this.shareService.exchangeById(id, request);
    }


    @PostMapping("/contribute")
    @CheckLogin
    public Share createContribute(@RequestBody ShareRequestDTO shareRequestDTO, HttpServletRequest request) {
        return this.shareService.createContribute(shareRequestDTO, request);
    }


    @PutMapping("/contribute/{id}")
    @CheckLogin
    public Share updateContribute(@PathVariable Integer id, @RequestBody ShareRequestDTO shareRequestDTO, HttpServletRequest request) {
        return this.shareService.updateContribute(id, shareRequestDTO, request);
    }

    @GetMapping("/my")
    @CheckLogin
    public List<Share> myShares(HttpServletRequest request) {
        return this.shareService.myShares(request);
    }

    @GetMapping("/my/contributions")
    @CheckLogin
    public List<Share> myContributions(HttpServletRequest request) {
        return this.shareService.myContributions(request);
    }

    @GetMapping("preview/{id}")
    @CheckLogin
    public Share preview(@PathVariable Integer id) {
        return this.shareService.preview(id);
    }

}
