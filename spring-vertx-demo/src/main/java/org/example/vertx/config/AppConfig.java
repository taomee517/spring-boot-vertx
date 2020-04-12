package org.example.vertx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * AppConfig
 *
 * @Author: taomee
 * @Date: 2020/4/11 0011 15:45
 * @Description:
 */
@Component
public class AppConfig {

    @Autowired
    Environment env;

    public int getHttpPort(){
        return env.getProperty("http.port", Integer.class);
    }

}
