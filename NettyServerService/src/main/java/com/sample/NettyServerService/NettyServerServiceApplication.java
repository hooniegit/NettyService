package com.sample.NettyServerService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sample.NettyServerService.Config.IniConfig;

@SpringBootApplication
public class NettyServerServiceApplication {

	public static void main(String[] args) {		
	    SpringApplication application = new SpringApplication(NettyServerServiceApplication.class);
	    
	    // [Initialize] IniConfig: Use `config.ini` Data as Property
	    application.addInitializers(new IniConfig());
	    application.run(args);
	}

}
