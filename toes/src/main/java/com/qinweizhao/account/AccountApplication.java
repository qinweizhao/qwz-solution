package com.qinweizhao.account;

import com.qinweizhao.account.client.CanalClient;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

import javax.annotation.Resource;

@EnableOpenApi
@EnableRabbit
@SpringBootApplication
public class AccountApplication implements CommandLineRunner {

    @Resource
    private CanalClient canalClient;

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        canalClient.run();
    }
}
