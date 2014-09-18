package com.computas.sem.uib.provider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.jena.atlas.lib.ArrayUtils;

import com.computas.sem.uib.connection.SparqlConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.shared.PrefixMapping;

import static com.computas.sem.uib.provider.Utils.*;

@Path("/service/movie")
public class MovieProvider {
	
	private static final String LINKEDMDB_SPARQL = "http://www.linkedmdb.org/sparql";

	@GET
	@Produces(MEDIA_TYPE)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response getMovieByTitle(@QueryParam("title") String title) {
		Query q = QueryFactory.create(
			"PREFIX dc: <http://purl.org/dc/terms/> \n"+
			"PREFIX movie: <http://data.linkedmdb.org/resource/movie/> \n"+
			"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n"+		
			"CONSTRUCT {"+
				"?movie a movie:film ."+
				"?movie dc:title \""+title+"\" ."+
				"?movie dc:date ?date ." +
				"?movie foaf:page ?page ." +
			"}"+
			"WHERE { "+
				"SERVICE <"+LINKEDMDB_SPARQL+"> {"+
					"SELECT ?movie ?date ?page "+
					"WHERE { "+
						"?movie a movie:film . "+
						"?movie dc:title \""+title+"\" . "+
						"?movie dc:date ?date ."+
						"?movie foaf:page ?page ."+
					"}"+
				"}"+
				"FILTER(STRSTARTS(STR(?page), \"http://www.imdb.com/title\"))"+
			"} LIMIT 100 ");
		Model movies = ModelFactory.createDefaultModel();
		QueryExecutionFactory.create(q, movies).execConstruct(movies);
		
		return getModelAsJsonLd(movies);		
	}
}
