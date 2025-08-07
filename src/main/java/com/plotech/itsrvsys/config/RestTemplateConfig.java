package com.plotech.itsrvsys.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置类，用于配置RestTemplate的连接池、连接超时等参数。
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 配置HTTP连接池。
     *
     * @return HttpClientConnectionManager HTTP连接池
     */
    @Bean
    public HttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        // 设置连接池的最大连接数
        poolingHttpClientConnectionManager.setMaxTotal(500);
        // 设置每个路由的最大连接数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(100);
        return poolingHttpClientConnectionManager;
    }

    /**
     * 从HTTP连接池中创建HTTP连接。
     *
     * @param poolingHttpClientConnectionManager HTTP连接池
     * @return HTTP连接
     */
    @Bean
    public HttpClient httpClient(HttpClientConnectionManager poolingHttpClientConnectionManager) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 设置连接池
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
        return httpClientBuilder.build();
    }

    /**
     * HTTP连接的工厂。
     *
     * @param httpClient HTTP连接
     * @return HTTP连接的工厂方法
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        // 设置HTTP连接
        clientHttpRequestFactory.setHttpClient(httpClient);
        // 设置与服务器连接的超时时间（毫秒）
        clientHttpRequestFactory.setConnectTimeout(5 * 1000);
        // 设置传递数据的超时时间（毫秒）
        clientHttpRequestFactory.setReadTimeout(60 * 1000);
        // 设置从连接池中获取可用连接的超时时间（毫秒）
        clientHttpRequestFactory.setConnectionRequestTimeout(10 * 1000);
        return clientHttpRequestFactory;
    }

    /**
     * 创建RestTemplate实例。
     *
     * @param clientHttpRequestFactory HTTP连接的工厂方法
     * @return RestTemplate实例
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate();
        // 设置HTTP连接的工厂方法
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        return restTemplate;
    }
}