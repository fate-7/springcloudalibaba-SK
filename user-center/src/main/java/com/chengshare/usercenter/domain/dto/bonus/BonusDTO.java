package com.chengshare.usercenter.domain.dto.bonus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author fate7
 * @Date 2020/4/20 4:14 下午
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BonusDTO {

    private Integer id;

    private Integer userId;

    private Integer value;

    private String event;

    private Date createTime;

    private String description;
}
