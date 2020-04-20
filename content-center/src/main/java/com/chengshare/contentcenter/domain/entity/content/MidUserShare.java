package com.chengshare.contentcenter.domain.entity.content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "mid_user_share")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidUserShare {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * share.id
     */
    @Column(name = "share_id")
    private Integer shareId;

    /**
     * user.id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取share.id
     *
     * @return share_id - share.id
     */
    public Integer getShareId() {
        return shareId;
    }

    /**
     * 设置share.id
     *
     * @param shareId share.id
     */
    public void setShareId(Integer shareId) {
        this.shareId = shareId;
    }

    /**
     * 获取user.id
     *
     * @return user_id - user.id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置user.id
     *
     * @param userId user.id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}