package com.qinweizhao.account.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * @author Monday_1201
 * @since 2021/3/23 9:52
 * </p>
 */
@Configuration
public class EsConfig {
    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization", "Bearer " + TOKEN);
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory
//                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient(){

//        //TODO 修改为线上的地址
//        RestClientBuilder builder = null;
//        //final String hostname, final int port, final String scheme
//
//        builder = RestClient.builder(new HttpHost("192.168.56.10", 9200, "http"));
////        builder = RestClient.builder(HttpHost.create(esUrl));
//        RestHighLevelClient client = new RestHighLevelClient(builder);
////        RestHighLevelClient client = new RestHighLevelClient(
////                RestClient.builder(
////                        new HttpHost("192.168.56.10", 9200, "http")));

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.56.10", 9200, "http")));

        return client;
    }

}
