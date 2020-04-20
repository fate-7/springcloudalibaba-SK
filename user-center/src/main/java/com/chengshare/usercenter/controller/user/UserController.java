package com.chengshare.usercenter.controller.user;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.chengshare.usercenter.auth.CheckLogin;
import com.chengshare.usercenter.domain.dto.bonus.BonusDTO;
import com.chengshare.usercenter.domain.dto.user.JwtTokenRespDTO;
import com.chengshare.usercenter.domain.dto.user.LoginRespDTO;
import com.chengshare.usercenter.domain.dto.user.UserLoginDTO;
import com.chengshare.usercenter.domain.dto.user.UserRespDTO;
import com.chengshare.usercenter.domain.entity.user.User;
import com.chengshare.usercenter.service.user.UserService;
import com.chengshare.usercenter.util.JwtOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author fate7
 * @Date 2020/4/7 9:15 下午
 **/

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserController {

    private final UserService userService;

    private final WxMaService wxMaService;

    private final JwtOperator jwtOperator;

    @GetMapping("/{id}")
    public User findById(@PathVariable Integer id) {
        log.info("我被调用了...");
        return userService.findById(id);
    }

    @GetMapping("/me")
    @CheckLogin
    public User findme(@RequestHeader("X-Token") String token) {
        return userService.me(token);
    }


    @GetMapping("/sign")
    @CheckLogin
    public User sign(@RequestHeader("X-Token") String token) {
        return userService.sign(token);
    }

    @GetMapping("/bonus-logs")
    @CheckLogin
    public List<BonusDTO>  bonusLogs(@RequestHeader("X-Token") String token) {
        return userService.bonusLogs(token);
    }
    /**
     * 模拟生成token
     * @return
     */
    @GetMapping("/gen-token")
    public String genToken() {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", 1);
        userInfo.put("wxNickname", "chengzhiqi");
        userInfo.put("role", "admin");
        return "X-Token: " + this.jwtOperator.generateToken(userInfo);
    }

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody UserLoginDTO loginDTO) throws WxErrorException {
        //用code请求微信api
        WxMaJscode2SessionResult result = this.wxMaService.getUserService()
                .getSessionInfo(loginDTO.getCode());

        //用户在微信的唯一标识
        String openid = result.getOpenid();

        //看用户是否注册，如果没有就插入
        User user = this.userService.login(loginDTO, openid);
        //如果注册就直接颁发token

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("wxNickname", user.getWxNickname());
        userInfo.put("role", user.getRoles());

        String token = jwtOperator.generateToken(userInfo);
        log.info("用户：{}，登陆成功，生成的token = {}, 有效期的: {}",
                loginDTO.getWxNickname(),
                token,
                jwtOperator.getExpirationTime());

        return LoginRespDTO.builder()
                .user(
                        UserRespDTO.builder()
                        .id(user.getId())
                        .avatarUrl(user.getAvatarUrl())
                        .bonus(user.getBonus())
                        .wxNickname(user.getWxNickname())
                        .build()
                )
                .token(
                        JwtTokenRespDTO.builder()
                                .token(token)
                                .expirationTime(jwtOperator.getExpirationTime().getTime())
                                .build())
                .build();
    }
}
