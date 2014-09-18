package com.computas.sem.uib.provider;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class MovieHelper {
	
	public Model searchForMovie(String name, int limit){
		try {
			Query q = QueryFactory.create(
					  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
					+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n"
					+ "CONSTRUCT { "
					+ 	"?movie a <http://schema.org/Movie> . "
					+ 	"?movie foaf:name ?name . "
					+ 	"?movie rdfs:label ?label . "
					+ "}" 
					+ "WHERE {"
					+ 	"?movie a <http://schema.org/Movie> . "
					+ 	"?movie foaf:name ?name . "
					+ 	"?movie rdfs:label ?label . "
					+ 	"FILTER(langMatches(lang(?name), \"EN\"))"
					+ 	"FILTER(langMatches(lang(?label), \"EN\"))"
					+ 	"FILTER(STRLEN(?name) >= "+name.length()+")"
					+ 	"FILTER(STRSTARTS(?name,\""+name+"\"))"
					+ "} "
					+ "ORDER BY ASC(STRLEN(?name)) "
					+ " LIMIT "+limit);
			QueryExecution queryExec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", q);
			try {
				return queryExec.execDescribe();
			} finally {
				queryExec.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ModelFactory.createDefaultModel();
		}
	}
}
