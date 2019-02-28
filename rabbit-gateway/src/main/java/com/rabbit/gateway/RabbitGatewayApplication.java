package com.rabbit.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableZuulProxy
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.rabbit.gateway","com.rabbit.common.bean","com.rabbit.feign"})
@SpringBootApplication
public class RabbitGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitGatewayApplication.class, args);
	}

	@Bean
	/*负载均衡拦截器*/
	LoadBalancerInterceptor loadBalancerInterceptor(LoadBalancerClient loadBalance) {
		return new LoadBalancerInterceptor(loadBalance);
	}

}

