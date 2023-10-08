package com.example.minio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MinioDemoApplicationTests {

    @Test
    void contextLoads() {
        String osName = System.getProperty("os.name");
        System.out.println("操作系统名称：" + osName);

        System.out.println("================");
        String osVersion = System.getProperty("os.version");
        System.out.println("操作系统版本：" + osVersion);
    }

}
