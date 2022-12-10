package org.example.vertx.verticle.factory;

import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SpringVerticleFactory
 *
 * @Author: taomee
 * @Date: 2020/4/11 0011 16:24
 * @Description:
 */
@Component
public class SpringVerticleFactory implements ApplicationContextAware, VerticleFactory {
    private ApplicationContext applicationContext;
    private Map<String, Verticle> verticleMap = new ConcurrentHashMap<>();

    @Override
    public String prefix() {
        return "vertx-verticle";
    }

    @Override
    public Verticle createVerticle(String s, ClassLoader classLoader) throws Exception {
        //去掉前缀，前缀与类名必须用：连接否则非法
        String className = VerticleFactory.removePrefix(s);
        Class<?> clazz = Class.forName(className);
        //从spring容器中获得verticle类
        return ((Verticle) applicationContext.getBean(clazz));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean blockingCreate() {
        return true;
    }

    public Map<String, Verticle> loadAllVerticle(){
        Map<String, Verticle> ctxVerticles = this.applicationContext.getBeansOfType(Verticle.class);
        if(!CollectionUtils.isEmpty(ctxVerticles)){
            ctxVerticles.values().forEach(verticle -> {
                String clzName = verticle.getClass().getName();
                StringBuilder verticleRegisterSb = new StringBuilder(prefix());
                verticleRegisterSb.append(":");
                verticleRegisterSb.append(clzName);
                String verticleRegisterName = verticleRegisterSb.toString();
                this.verticleMap.put(verticleRegisterName, verticle);
            });
        }
        return this.verticleMap;
    }
}
