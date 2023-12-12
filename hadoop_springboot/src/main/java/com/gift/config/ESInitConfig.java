/*
package com.gift.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ESInitConfig {
    //注入IOC容器
    @Bean
    public ElasticsearchClient elasticsearchClient(){
        RestClient client = RestClient.builder(new HttpHost("http://elasticsearch", 9200,"http")).build();
        ElasticsearchTransport transport = new RestClientTransport(client,new JacksonJsonpMapper());
        log.info("开始初始化ES客户端");
        return new ElasticsearchClient(transport);
    }
}
*/
