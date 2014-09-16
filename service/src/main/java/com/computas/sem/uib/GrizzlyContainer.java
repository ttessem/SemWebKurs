package com.computas.sem.uib;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

public class GrizzlyContainer {
	
	private static URI getBaseURI(int port) {
		return UriBuilder.fromUri("http://0.0.0.0/").port(port).build();
	}

	public static void main(String[] args) throws IOException {
		if(!validateArgs(args)){
			return;
		}
		
		
		org.apache.log4j.BasicConfigurator.configure();
		
		PoCApplication pocApp = new PoCApplication();
		pocApp.prepare();
		
		HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(getBaseURI(Integer.parseInt(args[0])), pocApp);
		System.in.read();
		
		httpServer.shutdown();
		pocApp.tearDown();
	}

	private static boolean validateArgs(String[] args) {
		if(args.length != 1){
			System.out.println("(Only) Listen port must be specified.");
			return false;
		}
		try {
			int port = Integer.parseInt(args[0]);
			if(port < 1 || port > 65535){
				System.out.println("The port must be in the range [1-65535].");
				return false;
			}
		} 
		catch (NumberFormatException e) {
			System.out.println("Argument must be an integer.");
			return true;
		}
		
		return true;
	}
}
