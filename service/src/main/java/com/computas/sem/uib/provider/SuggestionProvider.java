package com.computas.sem.uib.provider;

import static com.computas.sem.uib.provider.Utils.getModelAsJsonLd;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.computas.sem.uib.connection.RdfConnection;
import com.hp.hpl.jena.rdf.model.Model;

@Path("/service/suggest")
public class SuggestionProvider {
	@Inject @Named(RdfConnection.LOCAL) private RdfConnection local;
	@Inject private OntologyHelper ontoHelper;

	@GET
	@Path("person")
	public Response getPersonSuggestions(@QueryParam("name") String name) {
		return getModelAsJsonLd(ontoHelper.suggestPersonsFromModel(name, getModel()));
	}
	

	private Model getModel() {
		return local.getModel(RdfConnection.DATA_GRAPH);
	}
}
