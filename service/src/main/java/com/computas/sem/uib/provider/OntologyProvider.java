package com.computas.sem.uib.provider;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import static com.computas.sem.uib.provider.Utils.*;

@Path("/service/ontologi")
public class OntologyProvider {
	@Inject private OntologyHelper ontoHelper;

	@GET
	@Produces(MEDIA_TYPE)
	public Response getOntologi() {
		return getModelAsJsonLd(ontoHelper.getOntologyModel());
	}
	
	@GET
	@Produces("text/turtle")
	public Response getOntologiTurtle() {		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ontoHelper.getOntologyModel().write(out, "TURTLE");
		return Response.ok(out.toString()).build();
	}
	
	@POST
	@Consumes(MEDIA_TYPE)
	@Produces(MEDIA_TYPE)
	public Response postOntology(InputStream data) {
		Model newOnto = ModelFactory.createDefaultModel();
		newOnto.read(data, null, RDF_FORMAT);
		ontoHelper.setOntologyModel(newOnto);
		
		return getOntologi();
	}
	
	@GET
	@Produces(MEDIA_TYPE)
	@Path("person/predicates")
	public Response getPersonPredicates() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return Response.ok(mapper.writeValueAsString(ontoHelper.getPersonPredicates())).build();
	}
}
