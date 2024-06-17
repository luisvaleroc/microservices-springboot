package com.apine.report_ms;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
//implements CommandLineRunner
public class ReportMsApplication {

	//@Autowired
	//private  EurekaClient eurekaClient;

	public static void main(String[] args) {
		SpringApplication.run(ReportMsApplication.class, args);
	}


	//@Override
	//public void run(String... args) throws Exception {
	//	this.eurekaClient.getAllKnownRegions().forEach(System.out::println);
	//	System.out.println(this.eurekaClient.getApplication("companies-crud"));
//	}
}
