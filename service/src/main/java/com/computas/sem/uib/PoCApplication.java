package com.computas.sem.uib;

import java.util.LinkedList;
import java.util.List;

import org.glassfish.jersey.server.ResourceConfig;

import com.computas.sem.uib.connection.InMemoryConnection;
import com.computas.sem.uib.connection.RdfConnection;
import com.computas.sem.uib.connection.SparqlConnection;
import com.hp.hpl.jena.rdf.model.ModelFactory;

class PoCApplication extends ResourceConfig {
	private final List<RdfConnection> connections = new LinkedList<>();
	
	public PoCApplication() {
		InMemoryConnection localCon = new InMemoryConnection(ModelFactory.createDefaultModel());
//		SparqlConnection festCon = new SparqlConnection("http://helse.data.computas.com/openrdf-sesame/repositories/fest", 
//									"http://helse.data.computas.com/openrdf-sesame/repositories/fest/statements");
//		connections.add(localCon);
//		connections.add(festCon);
		
		register(new InjectionConfiguration(localCon, null));
		packages(true, "com.computas.sem.uib.provider");
	}

	public void prepare(){
		for(RdfConnection c: connections){
			c.init();
		}
	}
	
	public void tearDown(){
		for(RdfConnection c: connections){
			c.destroy();
		}
	}
	
}