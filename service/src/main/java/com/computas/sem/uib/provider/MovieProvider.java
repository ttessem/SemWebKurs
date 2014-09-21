package com.computas.sem.uib.provider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.jena.atlas.lib.ArrayUtils;

import com.computas.sem.uib.connection.RdfConnection;
import com.computas.sem.uib.connection.SparqlConnection;
import com.computas.sem.uib.helpers.MovieHelper;
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
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import static com.computas.sem.uib.helpers.Utils.*;

@Path("/service/movie")
@Api(value = "/service/movie/", description= "Movie api")
public class MovieProvider {
	@Inject @Named(RdfConnection.LOCAL) private RdfConnection local;
	@Inject private MovieHelper movieHelper;
	private static final String LINKEDMDB_SPARQL = "http://www.linkedmdb.org/sparql";

	
	@GET
	@Produces(MEDIA_TYPE)
	@ApiOperation( value = "Get all movies", notes = "Returns all movies (that are currently added as seen) by sparql describe query as JSON-LD.", produces = MEDIA_TYPE)
	public Response getAllMovies(){
		return getModelAsJsonLd(movieHelper.getAllMovies(getModel()));
	}
	
	@GET
	@Produces(MEDIA_TYPE)
	@Path("/search")	
	@ApiOperation( value = "Search for a movie.", notes = "Search dbpedia for a movie with a title that starts with the title argument (case sensitive). Returns sparql describe query of possible movies as JSON-LD.", produces = MEDIA_TYPE)
	public Response searchMovieByTitle(
			@ApiParam(required = true, value = "The start of the movie title (case sensitive).") @QueryParam("title") String title, 
			@ApiParam(defaultValue = "4", value = "Limit results [1..10]" )@QueryParam("limit") int limit) {
		if(limit == 0)
			limit = 4;
		else if(limit > 10)
			limit = 10;
		
		return getModelAsJsonLd(movieHelper.searchForMovie(title, limit));
	}
	
	private Model getModel() {
		return local.getModel(RdfConnection.DATA_GRAPH);
	}
}

