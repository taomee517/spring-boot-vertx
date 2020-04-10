package org.example.vertx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringVertxApplication
 *
 * @Author: taomee
 * @Date: 2020/4/9 0009 16:34
 * @Description:
 */
@Slf4j
@SpringBootApplication
public class SpringVertxApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringVertxApplication.class);
        log.info("spring-vertx 项目启动！");
    }
}
