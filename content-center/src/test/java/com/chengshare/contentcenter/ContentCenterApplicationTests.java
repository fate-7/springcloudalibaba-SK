package com.chengshare.contentcenter;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootTest
@RunWith(SpringRunner.class)
@MapperScan(basePackages = "com.chengshare")
public class ContentCenterApplicationTests {

    @Test
    public void contextLoads() {
    }

}
