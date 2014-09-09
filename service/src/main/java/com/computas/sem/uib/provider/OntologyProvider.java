package com.computas.sem.uib.provider;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.computas.sem.uib.connection.RdfConnection;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

@Path("ontologi")
public class OntologyProvider {
	private static final String MEDIA_TYPE = MediaType.APPLICATION_JSON;
	private static final String RDF_FORMAT = "JSON-LD";

	
	@Inject @Named(RdfConnection.LOCAL) private RdfConnection local;
	
	@GET
	@Produces(MEDIA_TYPE)
	public Response getOntologi() {
		Model ontology = local.getModel(RdfConnection.ONTOLOGY_GRAPH);
		return getModelAsJsonLd(ontology);
	}
	
	@POST
	@Consumes(MEDIA_TYPE)
	@Produces(MEDIA_TYPE)
	public Response postOntology(InputStream data) {
		Model newOnto = ModelFactory.createDefaultModel();
		newOnto.read(data, null, RDF_FORMAT);
		local.setModel(newOnto, RdfConnection.ONTOLOGY_GRAPH);
		
		return getModelAsJsonLd(local.getModel(RdfConnection.ONTOLOGY_GRAPH));
	}
	
	private Response getModelAsJsonLd(Model m) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		m.write(out, RDF_FORMAT);
		return Response.ok(out.toString()).build();
	}
}
