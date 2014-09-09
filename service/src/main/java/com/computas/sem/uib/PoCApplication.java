package com.computas.sem.uib;

import java.util.LinkedList;
import java.util.List;

import org.glassfish.jersey.server.ResourceConfig;

import com.computas.sem.uib.connection.InMemoryConnection;
import com.computas.sem.uib.connection.RdfConnection;
import com.computas.sem.uib.connection.SparqlConnection;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

class PoCApplication extends ResourceConfig {
	private final List<RdfConnection> connections = new LinkedList<>();
	
	public PoCApplication() {
		Model ontology = ModelFactory.createDefaultModel();
		ontology.read("src/main/resources/kurs.ttl", "TURTLE");
		
		InMemoryConnection localCon = new InMemoryConnection(ontology);
		SparqlConnection lmdbCon = new SparqlConnection("http://data.linkedmdb.org/sparql", null);
		connections.add(localCon);
		connections.add(lmdbCon);
		
		register(new InjectionConfiguration(localCon, lmdbCon));
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