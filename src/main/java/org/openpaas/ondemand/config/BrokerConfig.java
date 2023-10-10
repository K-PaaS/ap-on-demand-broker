package org.openpaas.ondemand.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaRepositories("org.openpaas.ondemand.repo")
@EntityScan(value = "org.openpaas.ondemand.model")
@ComponentScan(basePackages = { "org.openpaas.ondemand","org.openpaas.servicebroker"})
public class BrokerConfig {
}
