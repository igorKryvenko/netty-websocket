package com.netty;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SocketApplication {

	@Autowired
	private ServerBootstrap serverBootstrap;

	public static void main(String[] args) {
		SpringApplication.run(SocketApplication.class, args);
	}


}
