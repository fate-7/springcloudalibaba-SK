package com.chengshare.contentcenter.domain.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author fate7
 * @Date 2020/4/13 3:05 下午
 **/
@Getter
@AllArgsConstructor
public enum AuditStatusEnum {

    /**
     * 待审核
     */
    NOT_YET,

    /**
     * 审核通过
     */
    PASS,

    /**
     * 审核不通过
     */
    REJECT
}
