package com.computas.sem.uib.connection;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;

public class SparqlConnection implements RdfConnection {
	List<QueryExecution> openedConnection = new LinkedList<QueryExecution>();
	
	private final String sparqlEndpoint;
	private final String sparqlUpdate;
	
	public SparqlConnection(String sparqlEndpoint, String sparqlUpdate) {
		super();
		this.sparqlEndpoint = sparqlEndpoint;
		this.sparqlUpdate = sparqlUpdate;
	}

	private QueryExecution createQueryExecution(Query query){
		return QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
	}
	
	@Override
	public Model executeConstruct(Query constructQuery) {
		QueryExecution queryExecution = createQueryExecution(constructQuery);
		openedConnection.add(queryExecution);
		//removed finally block because the connection got closed before the data was completely fetched
        return queryExecution.execConstruct();
	}

	@Override
	public ResultSet executeSelect(Query selectQuery) {
		QueryExecution queryExecution = createQueryExecution(selectQuery);
		openedConnection.add(queryExecution);
		//removed finally block because the connection got closed before the data was completely fetched
		return queryExecution.execSelect();
	}

	@Override
	public boolean executeAsk(Query query) {
		QueryExecution queryExecution = createQueryExecution(query);
		return queryExecution.execAsk();
	}
	
	@Override
	public Model getModel(String graphName) {
		return executeConstruct(QueryFactory.create("CONSTRUCT {?s ?p ?o} WHERE { GRAPH "+asIRI(graphName)+" {?s ?p ?o}}"));
	}

	@Override
	public void addModel(Model model, String graphName) {
		StringWriter sw = new StringWriter();
		sw.write("INSERT DATA { GRAPH "+asIRI(graphName)+" {");
		//copy model to get a model without prefixes
		model = ModelFactory.createDefaultModel().add(model);
		model.write(sw, "TURTLE");
		sw.write("}}");
		
		UpdateRequest update = UpdateFactory.create(sw.toString());
		executeUpdate(update);
	}

	@Override
	public void executeUpdate(UpdateRequest queryString) {
		UpdateExecutionFactory.createRemoteForm(queryString, sparqlUpdate).execute();
	}

	@Override
	@PostConstruct
	public void init() {}

	@Override
	@PreDestroy
	public void destroy() {
		while(openedConnection.size() > 0)
			openedConnection.remove(openedConnection.size()-1).close();
	}

	private String asIRI(String r){
		return "<"+r+">";
	}

	@Override
	public void setModel(Model model, String graphName) {
		throw new Error(SparqlConnection.class.toString()+".setModel not implemented yet.");		
	}
}
