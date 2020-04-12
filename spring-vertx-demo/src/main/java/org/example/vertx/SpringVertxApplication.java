package org.example.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.extern.slf4j.Slf4j;
import org.example.vertx.verticle.BookRestApi;
import org.example.vertx.verticle.SpringWorker;
import org.example.vertx.verticle.factory.SpringVerticleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;

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
//    @Autowired
//    IndexServer indexServer;
//
//    @Autowired
//    BookVerticle bookVerticle;

    @Autowired
    SpringVerticleFactory verticleFactory;

    @Value("${vertx.worker.pool.size}")
    int workerPoolSize;

    @Value("${vertx.springWorker.instances}")
    int springWorkerInstances;

    public static void main(String[] args) {
        SpringApplication.run(SpringVertxApplication.class);
        log.info("恭喜！启动成功！");
    }

//    @PostConstruct
//    public void loadVerticle(){
//        Vertx.vertx().deployVerticle(indexServer);
//        Vertx.vertx().deployVerticle(bookVerticle);
//    }

    @EventListener
    public void deployVerticles(ApplicationReadyEvent event) {
        //配置Vertx
        VertxOptions options = new VertxOptions();
        options.setWorkerPoolSize(workerPoolSize);
        Vertx vertx = Vertx.vertx(options);
        vertx.registerVerticleFactory(verticleFactory);



        CountDownLatch deployLatch = new CountDownLatch(2);
        AtomicBoolean failed = new AtomicBoolean(false);

        String restApiVerticleName = verticleFactory.prefix() + ":" + BookRestApi.class.getName();
        vertx.deployVerticle(restApiVerticleName, ar -> {
            if (ar.failed()) {
                log.error("Failed to deploy verticle", ar.cause());
                failed.compareAndSet(false, true);
            }
            deployLatch.countDown();
        });

        DeploymentOptions workerDeploymentOptions = new DeploymentOptions()
                .setWorker(true)
                // As worker verticles are never executed concurrently by Vert.x by more than one thread,
                // deploy multiple instances to avoid serializing requests.
                .setInstances(springWorkerInstances);
        String workerVerticleName = verticleFactory.prefix() + ":" + SpringWorker.class.getName();
        vertx.deployVerticle(workerVerticleName, workerDeploymentOptions, ar -> {
            if (ar.failed()) {
                log.error("Failed to deploy verticle", ar.cause());
                failed.compareAndSet(false, true);
            }
            deployLatch.countDown();
        });

        try {
            if (!deployLatch.await(5, SECONDS)) {
                throw new RuntimeException("Timeout waiting for verticle deployments");
            } else if (failed.get()) {
                throw new RuntimeException("Failure while deploying verticles");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
