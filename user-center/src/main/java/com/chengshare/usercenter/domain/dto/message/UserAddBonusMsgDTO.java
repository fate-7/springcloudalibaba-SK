package com.chengshare.usercenter.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author fate7
 * @Date 2020/4/13 3:41 下午
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddBonusMsgDTO {

    private Integer userId;

    private Integer bonus;

    private String description;

    private String event;
}
