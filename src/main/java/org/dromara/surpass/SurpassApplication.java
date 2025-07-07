package org.dromara.surpass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author tomsun28
 * @date 23:13 2019-05-29
 */
@SpringBootApplication
@ServletComponentScan
@EnableCaching
public class SurpassApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurpassApplication.class, args);
    }
}