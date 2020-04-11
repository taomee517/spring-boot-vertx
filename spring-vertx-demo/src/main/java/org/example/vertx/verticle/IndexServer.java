package org.example.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.example.vertx.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * IndexServer
 *
 * @Author: taomee
 * @Date: 2020/4/11 0011 14:57
 * @Description:
 */
@Component
public class IndexServer extends AbstractVerticle {

    @Autowired
    AppConfig appConfig;

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        //静态资源处理类
        router.route().handler(StaticHandler.create());
        vertx.createHttpServer().requestHandler(router).listen(appConfig.getHttpPort());
    }
}
