package com.computas.sem.uib.provider;

import static com.computas.sem.uib.helpers.Utils.getModelAsJsonLd;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.computas.sem.uib.connection.RdfConnection;
import com.computas.sem.uib.helpers.MovieHelper;
import com.computas.sem.uib.helpers.OntologyHelper;
import com.hp.hpl.jena.rdf.model.Model;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/service/suggest")
@Api(value = "/service/suggest", description = "Suggestion api")
public class SuggestionProvider {
	@Inject @Named(RdfConnection.LOCAL) private RdfConnection local;
	@Inject private OntologyHelper ontoHelper;
	@Inject private MovieHelper movieHelper;

	@GET
	@Path("/person")
	@ApiOperation(value = "Suggest a person.", notes = "Suggest a person that has a first or last name which starts with argument name.")
	public Response getPersonSuggestions(@ApiParam(required = true, value = "The name to suggest (case insensitive).") @QueryParam("name") String name) {
		return getModelAsJsonLd(ontoHelper.suggestPersonsFromModel(name, getModel()));
	}
	
	@GET
	@Path("/movie")
	@ApiOperation(value = "Suggest a movie.", notes = "Suggest a movie with a title that starts with argument title (Only searches movies already added as seen by someone).")
	public Response getMovieSuggestions(@ApiParam(required = true, value = "The title to suggest (case insensitive).") @QueryParam("title") String title){
		return getModelAsJsonLd(movieHelper.suggestMoviesFromModel(title, getModel()));
	}
	
	private Model getModel() {
		return local.getModel(RdfConnection.DATA_GRAPH);
	}
}
