package com.chengshare.contentcenter.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author fate7
 * @Date 2020/4/13 3:41 下午
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddBonusMsgDTO implements Serializable {

    private Integer userId;

    private Integer bonus;
}
