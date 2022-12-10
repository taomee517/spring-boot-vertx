/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.example.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.serviceproxy.ServiceProxyBuilder;
import lombok.extern.slf4j.Slf4j;
import org.example.vertx.config.AppConfig;
import org.example.vertx.dao.Book;
import org.example.vertx.service.BookAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_CREATED;

/**
 * A standard verticle, consuming the {@link BookAsyncService} over the event bus to expose a REST API.
 *
 * @author Thomas Segismont
 */
@Component
@Slf4j
public class BookRestApi extends AbstractVerticle {

  @Autowired
  AppConfig appConfig;

  private BookAsyncService bookAsyncService;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    bookAsyncService = new ServiceProxyBuilder(vertx).setAddress(BookAsyncService.ADDRESS).build(BookAsyncService.class);

    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());

    router.post("/book/save").handler(this::addBook);
    router.get("/book/list").handler(this::getAllBooks);

    StaticHandler staticHandler = StaticHandler.create();
    router.route().handler(staticHandler);

    int port = appConfig.getHttpPort();
    vertx.createHttpServer().requestHandler(router)
            .listen(port, listen -> {
              if (listen.succeeded()) {
                log.info("BookRestApi started at port: {}", port);
                startPromise.complete();
              } else {
                startPromise.fail(listen.cause());
              }
            });
  }

  private void addBook(RoutingContext routingContext) {
    Book book = new Book(routingContext.getBodyAsJson());
    bookAsyncService.add(book, ar -> {
      if (ar.succeeded()) {
        routingContext.response().setStatusCode(HTTP_CREATED).end();
      } else {
        routingContext.fail(ar.cause());
      }
    });
  }

  private void getAllBooks(RoutingContext routingContext) {
    bookAsyncService.getAll(ar -> {
      if (ar.succeeded()) {
        List<Book> result = ar.result();
        JsonArray jsonArray = new JsonArray(result);
        routingContext.response()
                .putHeader("Content-type", "text/html;charset=utf-8")
                .end(jsonArray.encodePrettily());
      } else {
        routingContext.fail(ar.cause());
      }
    });
  }
}
