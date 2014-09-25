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

import com.computas.sem.uib.helpers.OntologyHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import static com.computas.sem.uib.helpers.Utils.*;

@Path("/service/ontologi")
@Api(value = "/service/ontology/", description= "Admin data api")
public class OntologyProvider {
	@Inject private OntologyHelper ontoHelper;

	@GET
	@Produces(MEDIA_TYPE)
	@ApiOperation( value = "Get the current ontology.", notes = "Gets the current ontology/vocabulary as JSON-LD.")
	public Response getOntologi() {
		return getModelAsJsonLd(ontoHelper.getOntologyModel());
	}
	
	@GET
	@Produces("text/turtle")
	@ApiOperation( value = "Get the current ontology.", notes = "Gets the current ontology/vocabulary as TURTLE.")
	public Response getOntologiTurtle() {		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ontoHelper.getOntologyModel().write(out, "TURTLE");
		return Response.ok(out.toString()).build();
	}
	
	@POST
	@Consumes(MEDIA_TYPE)
	@Produces(MEDIA_TYPE)
	@ApiOperation(value = "Replace the current ontology", notes = "Replaces the current ontology. Consumes JSON-LD.")
	public Response postOntology(@ApiParam(value="The new ontology as JSON-LD.", required=true) InputStream data) {
		try {
			Model newOnto = ModelFactory.createDefaultModel();
			newOnto.read(data, null, RDF_FORMAT);
			ontoHelper.setOntologyModel(newOnto);
		} catch(Exception e){
			e.printStackTrace();
		}
		return getOntologi();
	}
	
	@GET
	@Produces(MEDIA_TYPE)
	@Path("/person/predicates")
	@ApiOperation(value = "Get all predicates for Person.", notes = "Gets the predicates with person as domain. Produces json.")
	public Response getPersonPredicates() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return Response.ok(mapper.writeValueAsString(ontoHelper.getPersonPredicates())).build();
	}
}
