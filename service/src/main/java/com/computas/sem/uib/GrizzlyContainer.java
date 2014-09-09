package com.computas.sem.uib;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

public class GrizzlyContainer {
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://0.0.0.0/").port(9998).build();
	}

	private static final URI BASE_URI = getBaseURI();

	public static void main(String[] args) throws IOException {
		org.apache.log4j.BasicConfigurator.configure();
		
		PoCApplication pocApp = new PoCApplication();
		pocApp.prepare();
		
		HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, pocApp);
		System.in.read();
		
		httpServer.shutdown();
		pocApp.tearDown();
	}
}
