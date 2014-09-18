package com.computas.sem.uib.provider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
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
	@Inject private MovieHelper movieHelper;
	private static final String LINKEDMDB_SPARQL = "http://www.linkedmdb.org/sparql";

	@GET
	@Produces(MEDIA_TYPE)
	@Path("search")
	public Response searchMovieByTitle(@QueryParam("title") String title, @QueryParam("limit") int limit) {
		if(limit == 0)
			limit = 4;
		else if(limit > 10)
			limit = 10;
		
		return getModelAsJsonLd(movieHelper.searchForMovie(title, limit));
	}
}

