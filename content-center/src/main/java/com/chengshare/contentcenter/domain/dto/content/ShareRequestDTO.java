package com.chengshare.contentcenter.domain.dto.content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author fate7
 * @Date 2020/4/20 5:16 下午
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareRequestDTO {

    /**
     * 作者
     */
    private String author;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 是否原创 0:否 1:是
     */
    private Boolean isOriginal;

    /**
     * 价格（需要的积分）
     */
    private Integer price;

    /**
     * 概要信息
     */
    private String summary;

    /**
     * 标题
     */
    private String title;


}
