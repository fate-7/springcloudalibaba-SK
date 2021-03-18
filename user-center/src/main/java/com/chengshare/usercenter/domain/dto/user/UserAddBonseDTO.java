package com.chengshare.usercenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author fate7
 * @Date 2020/4/20 2:27 下午
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddBonseDTO {

    private Integer userId;

    private Integer bonus;
}
