package com.netty.config;


import com.netty.server.WebSocketServerPipelineFactory;


import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ikryvenko on 20.06.17.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class NettyConfig {

    @Value("${netty.port}")
    private int port;

    @Value("${netty.host}")
    private String localAddress;


    @Bean
    public ServerBootstrap serverBootstrap() {
        ServerBootstrap server = new ServerBootstrap(new NioServerSocketChannelFactory(bossGroup(),workerGroup(),4));

        server.setOption("backlog",500);
        server.setOption("connectTimeoutMillis",10000);
        server.setPipelineFactory(new WebSocketServerPipelineFactory());
        server.bind(new InetSocketAddress(localAddress,port));
        return server;
    }

    @Bean(name = "bossGroup")
    public ExecutorService bossGroup() {
        return new OrderedMemoryAwareThreadPoolExecutor(1,4000,6000,60, TimeUnit.SECONDS);
    }

    @Bean(name = "workerGroup")
    public ExecutorService workerGroup(){
        return new OrderedMemoryAwareThreadPoolExecutor(1,4000,6000,60,TimeUnit.SECONDS);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
