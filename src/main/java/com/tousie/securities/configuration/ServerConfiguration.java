package com.tousie.securities.configuration;

import com.sonluo.spongebob.spring.server.Server;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    public Server globalServer() {
        Server server = new Server(applicationContext);
        server.init();
        return server;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
