package org.example.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.example.vertx.config.AppConfig;
import org.example.vertx.dao.Book;
import org.example.vertx.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * BookVerticle
 *
 * @Author: taomee
 * @Date: 2020/4/11 0011 16:36
 * @Description:
 */
@Component
//@Scope(SCOPE_PROTOTYPE)
public class BookVerticle extends AbstractVerticle {
    @Autowired
    BookService bookService;

    @Autowired
    AppConfig appConfig;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Router router = Router.router(vertx);
        router.route("/getbook").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext context) {
                HttpServerRequest request = context.request();
                long id = Long.parseLong(request.getParam("id"));
                Book book = bookService.getBook(id);
                if (book == null) {
                    request.response().setStatusCode(400).end("no book");
                } else {
                    HttpServerResponse response = request.response();
                    //解决中文乱码
                    response.putHeader("Content-type", "text/html;charset=utf-8");
                    response.end(Json.encodePrettily(book));
                }
            }
        });
        vertx.createHttpServer().requestHandler(router)
                .listen(appConfig.getHttpPort(),result -> {
                    if(result.succeeded()){
                        startPromise.complete();
                    }else {
                        startPromise.fail(result.cause());
                    }
                });
    }
}
