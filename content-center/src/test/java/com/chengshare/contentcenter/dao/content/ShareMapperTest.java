package com.chengshare.contentcenter.dao.content;

import com.chengshare.contentcenter.ContentCenterApplicationTests;
import com.chengshare.contentcenter.domain.entity.content.Share;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Slf4j
public class ShareMapperTest extends ContentCenterApplicationTests {

    @Autowired
    private ShareMapper shareMapper;

    @Test
    public void testUpdate() {
        Share share = new Share();
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        share.setTitle("xxx");
        share.setCover("xxx");
        share.setAuthor("cheng");
        share.setBuyCount(1);

        shareMapper.insertSelective(share);

        share.setAuthor("ktCheng");
        share.setTitle("XXX");
        shareMapper.updateByPrimaryKeySelective(share);

    }


}