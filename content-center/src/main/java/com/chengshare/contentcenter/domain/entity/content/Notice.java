package com.chengshare.contentcenter.domain.entity.content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "notice")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notice {
    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否显示 0:否 1:是
     */
    @Column(name = "show_flag")
    private Boolean showFlag;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取内容
     *
     * @return content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取是否显示 0:否 1:是
     *
     * @return show_flag - 是否显示 0:否 1:是
     */
    public Boolean getShowFlag() {
        return showFlag;
    }

    /**
     * 设置是否显示 0:否 1:是
     *
     * @param showFlag 是否显示 0:否 1:是
     */
    public void setShowFlag(Boolean showFlag) {
        this.showFlag = showFlag;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}