package org.nitk.requestservice.config;

import com.mongodb.client.MongoClient;
import org.nitk.common.mongo.MongoCollections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonBeans {
    @Bean
    public MongoCollections mongoCollections(@Autowired MongoClient mongoClient) {
        return new MongoCollections(mongoClient);
    }
}
