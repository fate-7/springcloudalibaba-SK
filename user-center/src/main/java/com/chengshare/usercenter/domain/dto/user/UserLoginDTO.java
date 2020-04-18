package com.chengshare.usercenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author fate7
 * @Date 2020/4/18 10:33 下午
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    private String code;

    private String avatarUrl;

    private String wxNickname;

}
