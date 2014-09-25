package com.computas.sem.uib.provider;

import static com.computas.sem.uib.helpers.Utils.MEDIA_TYPE;

import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.computas.sem.uib.PoCApplication;
import com.computas.sem.uib.connection.RdfConnection;
import com.computas.sem.uib.helpers.MovieHelper;
import com.computas.sem.uib.helpers.OntologyHelper;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/service/admin")
@Api(value = "/service/admin/", description= "Admin data api")
public class AdminProvider {
	@Inject @Named(RdfConnection.LOCAL) private RdfConnection local;
	@Inject private OntologyHelper ontoHelper;
	@Inject private MovieHelper movieHelper;

	@POST
	@Path("/save")
	@ApiOperation( value = "Persist to disk", notes = "Persist the currently stored data to disk.")
	public Response saveAll() {
		try (	FileWriter data_fw = new FileWriter(PoCApplication.DATA_PATH); 
				FileWriter auth_fw = new FileWriter(PoCApplication.AUTH_PATH)) {
			local.getModel(RdfConnection.DATA_GRAPH).write(data_fw, "TURTLE");
			local.getModel(RdfConnection.AUTH_GRAPH).write(auth_fw, "TURTLE");
		} catch (IOException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		
		return Response.status(400).build();
	}
	
	@POST
	@Path("/clear")
	@ApiOperation( value = "Clears the in-memory models", notes = "Clears all data stored in the in-memory models.")
	public Response clearAll() {
		local.setModel(ModelFactory.createDefaultModel(), RdfConnection.AUTH_GRAPH);
		local.setModel(ModelFactory.createDefaultModel(), RdfConnection.DATA_GRAPH);
		return Response.noContent().build();
	}
	
	@POST
	@Path("/clear/{type}")
	@ApiOperation( value = "Clear a dataset", notes = "Clear the person or movie dataset.")
	public Response clearType(@ApiParam(value = "Dataset person or movie.", required = true) @PathParam("type") String type) {
		try {
		if(type.equals("person")) {
			local.setModel(ModelFactory.createDefaultModel(), RdfConnection.AUTH_GRAPH);
			Model m = local.getModel(RdfConnection.DATA_GRAPH);
			local.setModel(m.difference(ontoHelper.getAllPersonsFromModel(m)), RdfConnection.DATA_GRAPH);
		}
		else if(type.equals("movie")) {
			Model m = local.getModel(RdfConnection.DATA_GRAPH);
			local.setModel(m.difference(movieHelper.getAllMovies(m)), RdfConnection.DATA_GRAPH);
		}
		} catch(Exception e){
			e.printStackTrace();
		}
		return Response.status(400).build();
	}
	
	
}
