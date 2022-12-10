package org.example.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.example.vertx.config.AppConfig;
import org.example.vertx.methods.Greeter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * GreetingVerticle
 *
 * @Author: taomee
 * @Date: 2020/4/11 0011 16:36
 * @Description:
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class GreetingVerticle extends AbstractVerticle {
    @Autowired
    Greeter greeter;

    @Autowired
    AppConfig appConfig;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.createHttpServer().requestHandler(request -> {
           String name = request.getParam("name");
            if (name == null) {
                request.response().setStatusCode(400).end("Missing name");
            } else {
                String greetMsg = greeter.sayHello(name);
                request.response().end(greetMsg);
            }
        }).listen(appConfig.getHttpPort(),result -> {
            if(result.succeeded()){
                startPromise.complete();
            }else {
                startPromise.fail(result.cause());
            }
        });
    }
}
