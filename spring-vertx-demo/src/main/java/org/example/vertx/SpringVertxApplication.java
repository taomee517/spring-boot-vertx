package org.example.vertx;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.example.vertx.verticle.IndexServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

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
    @Autowired
    IndexServer indexServer;

    public static void main(String[] args) {
        SpringApplication.run(SpringVertxApplication.class);
        log.info("恭喜！启动成功！");
    }

    @PostConstruct
    public void loadVerticle(){
        Vertx.vertx().deployVerticle(indexServer);
    }
}
