package com.elephant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author ai-interview
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AIInterViewApp {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(AIInterViewApp.class, args);
        System.out.println(" -------------------------------------------------------- \n");
        System.out.println(" ------------------- Ai面试官上线 ------------------------ \n");
        System.out.println(" -------------------------------------------------------- \n");
    }
}
