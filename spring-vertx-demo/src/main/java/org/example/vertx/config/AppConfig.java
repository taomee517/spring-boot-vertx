package org.example.vertx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * AppConfig
 *
 * @Author: taomee
 * @Date: 2020/4/11 0011 15:45
 * @Description:
 */
@Configuration
public class AppConfig {

    @Autowired
    Environment env;

    public int getHttpPort(){
        return env.getProperty("http.port", Integer.class);
    }

}
