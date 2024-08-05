package com.sample.NettyClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sample.NettyClientService.Config.IniConfig;
import com.sample.NettyClientService.NettyClient.Service.ClientService;

@SpringBootApplication
public class NettyClientServiceApplication implements CommandLineRunner {

    @Autowired
    private ClientService clientService;
	
	public static void main(String[] args) {		
	    SpringApplication application = new SpringApplication(NettyClientServiceApplication.class);
	    
	    // [Initialize] IniConfig: Use `config.ini` Data as Property
	    application.addInitializers(new IniConfig());
	    application.run(args);
	}
	
    @Override
    public void run(String... args) throws Exception {
    	// [Start] Client Service
        clientService.start();
    }

}
