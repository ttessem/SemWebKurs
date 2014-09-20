package com.computas.sem.uib.connection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.update.UpdateRequest;

public interface RdfConnection {
	public static final String ONTOLOGY_GRAPH = "ontology";
	public static final String DATA_GRAPH = "data";
    public static final String LOCAL 	= "LocalConnection";
	public static final String LMDB 	= "LmdbConnection";
	
	Model executeConstruct(Query constructQuery);

    ResultSet executeSelect(Query selectQuery);

    Model getModel(String graphName);

    void addModel(Model model, String graphName);

    void executeUpdate(UpdateRequest queryString);
    void setModel(Model model, String graphName);
    
    @PostConstruct
    public void init();
    @PreDestroy
    public void destroy();

	boolean executeAsk(Query query);
}
