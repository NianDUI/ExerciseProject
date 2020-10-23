package top.niandui.controller.estest;

import lombok.Data;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Title: EsConfig.java
 * @description: EsConfig
 * @time: 2020/10/13 14:15
 * @author: liyongda
 * @version: 1.0
 */
@Data
//@Configuration
//@EnableReactiveElasticsearchRepositories
//@ConfigurationProperties(prefix = "spring.elasticsearch.rest")
public class EsConfig extends AbstractElasticsearchConfiguration {
    private List<String> uris = new ArrayList<>(Collections.singletonList("localhost:9200"));

    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration config = ClientConfiguration.builder()
                .connectedTo(uris.toArray(new String[]{}))
                //.withConnectTimeout(Duration.ofSeconds(5))
                //.withSocketTimeout(Duration.ofSeconds(3))
                //.useSsl()
                //.withDefaultHeaders(defaultHeaders)
                //.withBasicAuth(username, password)
                // ... other options
                .build();
        RestHighLevelClient client = RestClients.create(config).rest();
        return client;
    }
}
