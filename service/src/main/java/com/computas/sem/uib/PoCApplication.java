package com.computas.sem.uib;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.glassfish.jersey.server.ResourceConfig;

import com.computas.sem.uib.connection.InMemoryConnection;
import com.computas.sem.uib.connection.RdfConnection;
import com.computas.sem.uib.connection.SparqlConnection;
import com.computas.sem.uib.helpers.OntologyHelper;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class PoCApplication extends ResourceConfig {
	public static final String AUTH_PATH = "src/main/resources/auth.ttl";
	public static final String DATA_PATH = "src/main/resources/data.ttl";
	private final List<RdfConnection> connections = new LinkedList<>();
	private Model auth;
	private Model data;
	private InMemoryConnection localCon;
	
	public PoCApplication() {
		Model ontology = ModelFactory.createDefaultModel();
		ontology.read("src/main/resources/kurs.ttl", "TURTLE");
		
		data = ModelFactory.createDefaultModel();
		if((new File(DATA_PATH)).exists()){
			data.read(DATA_PATH, "TURTLE");
		}
		auth = ModelFactory.createDefaultModel();
		if((new File(AUTH_PATH)).exists()){
			data.read(AUTH_PATH, "TURTLE");	
		}
		
		localCon = new InMemoryConnection(ontology);
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
		// legg til data fra disk til minne
		localCon.setModel(data, RdfConnection.DATA_GRAPH);
		localCon.setModel(auth, RdfConnection.AUTH_GRAPH);
		
	}
	
	public void tearDown(){
		for(RdfConnection c: connections){
			c.destroy();
		}
	}
	
}