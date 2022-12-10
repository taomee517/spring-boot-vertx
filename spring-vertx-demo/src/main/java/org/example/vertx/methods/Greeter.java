package org.example.vertx.methods;

import org.springframework.stereotype.Component;

/**
 * Greeter
 *
 * @Author: taomee
 * @Date: 2020/4/11 0011 16:22
 * @Description:
 */
@Component
public class Greeter {
    public String sayHello(String name){
        return "Hello," + name + "!";
    }
}
