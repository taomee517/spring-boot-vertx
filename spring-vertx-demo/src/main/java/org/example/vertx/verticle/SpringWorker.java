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
import io.vertx.serviceproxy.ServiceBinder;
import lombok.extern.slf4j.Slf4j;
import org.example.vertx.service.BookAsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * A worker verticle, exposing the {@link BookAsyncService} over the event bus.
 *
 * Since it is a worker verticle, it is perfectly fine for the registered service to delegate calls to backend Spring beans.
 *
 * @author Thomas Segismont
 */
@Slf4j
@Component
// Prototype scope is needed as multiple instances of this verticle will be deployed
@Scope(SCOPE_PROTOTYPE)
public class SpringWorker extends AbstractVerticle {

  @Autowired
  BookAsyncService bookAsyncService;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    new ServiceBinder(vertx).setAddress(BookAsyncService.ADDRESS)
            .register(BookAsyncService.class, bookAsyncService)
            .completionHandler(ar ->{
              if (ar.succeeded()) {
                log.info("SpringWorker started, service register OK!");
                startPromise.complete();
              } else {
                startPromise.fail(ar.cause());
              }
            });
  }

}
