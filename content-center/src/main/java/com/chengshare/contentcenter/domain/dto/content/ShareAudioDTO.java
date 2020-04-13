package com.chengshare.contentcenter.domain.dto.content;

import com.chengshare.contentcenter.domain.dto.enums.AuditStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author fate7
 * @Date 2020/4/13 2:40 下午
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareAudioDTO {

    /**
     * 审核状态
     */
    private AuditStatusEnum auditStatusEnum;

    /**
     * 原因
     */
    private String reason;
}
