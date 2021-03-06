package com.computas.sem.uib.helpers;

import javax.inject.Inject;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class MovieHelper {
	
//	public static final String MOVIE = "<http://schema.org/Movie>";
	public static final String MOVIE = "<http://dbpedia.org/ontology/Film>";
	public static final String TV = "<http://dbpedia.org/ontology/TelevisionShow>";
	
	@Inject private OntologyHelper ontoHelper;

	public Model searchForMovie(String name, int limit){
		try {
			Query q = QueryFactory.create(
					  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
					+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n"
					+ "CONSTRUCT { "
					+ 	"?movie a ?type . "
					+ 	"?movie a <"+ontoHelper.getFilmClass().getURI()+"> . "
					+ 	"?movie foaf:name ?name . "
					+ 	"?movie rdfs:label ?label . "
					+ "}" 
					+ "WHERE {"
					+ 	"?movie a ?type . "
					+ 	"FILTER(?type = "+MOVIE+ " || ?type = "+TV+") "
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
				return queryExec.execConstruct();
			} finally {
				queryExec.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ModelFactory.createDefaultModel();
		}
	}

	public Model getMovie(String film) {
		try {
			film = "<"+film+">";
			Query q = QueryFactory.create(
					  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
					+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n"
					+ "CONSTRUCT { "
					+ 	film+" a ?type . "
					+ 	film+" a <"+ontoHelper.getFilmClass().getURI()+"> . "
					+ 	film+" foaf:name ?nameEn . "
					+ 	film+" rdfs:label ?labelEn . "
					+ "}" 
					+ "WHERE {"
					+	film+" a ?type ."
					+ 	film+" foaf:name ?name . "
					+ 	film+" rdfs:label ?label . "
					+ 	"FILTER(langMatches(lang(?name), \"EN\"))"
					+ 	"FILTER(langMatches(lang(?label), \"EN\"))"
					+	"BIND(str(?name) AS ?nameEn)"
					+	"BIND(str(?label) AS ?labelEn)"
					+ "} ");
			QueryExecution queryExec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", q);
			try {
				return queryExec.execConstruct();
			} finally {
				queryExec.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ModelFactory.createDefaultModel();
		}
	}
	
	public Model getAllMovies(Model data) {
		Query q = QueryFactory.create("DESCRIBE ?s WHERE { ?s a "+MOVIE+" }");
		QueryExecution queryExec = QueryExecutionFactory.create(q, data);
		try {
			return queryExec.execDescribe();
		}
		finally {
			queryExec.close();
		}
	}

	public Model suggestMoviesFromModel(String title, Model data) {
		try {
			Query q = QueryFactory.create(
					  "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n"
					+ "DESCRIBE ?movie " 
					+ "WHERE {"
					+ 	"?movie foaf:name ?title ." 
					+ 	"FILTER(STRSTARTS(LCASE(?title), LCASE(\""+title+"\")))" 
					+ "} LIMIT 10");
			QueryExecution queryExec = QueryExecutionFactory.create(q, data);
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
