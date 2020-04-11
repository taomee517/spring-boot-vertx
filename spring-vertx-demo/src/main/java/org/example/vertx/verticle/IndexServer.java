package org.example.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
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
    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.route().handler(StaticHandler.create());
        vertx.createHttpServer().requestHandler(router).listen(9026);
    }
}
