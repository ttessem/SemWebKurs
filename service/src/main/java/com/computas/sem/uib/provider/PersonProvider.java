package com.computas.sem.uib.provider;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import com.computas.sem.uib.connection.RdfConnection;
import com.computas.sem.uib.helpers.AuthHelper;
import com.computas.sem.uib.helpers.MovieHelper;
import com.computas.sem.uib.helpers.OntologyHelper;
import com.hp.hpl.jena.rdf.model.Model;

import static com.computas.sem.uib.helpers.Utils.*;

@Path("/service/uib/person")
public class PersonProvider {
	@Inject @Named(RdfConnection.LOCAL) private RdfConnection local;
	@Inject private OntologyHelper ontoHelper;
	@Inject private MovieHelper movieHelper;
	@Inject private AuthHelper authHelper;
	private String cx_auth;
	
	public PersonProvider(@HeaderParam("cx_auth") String cx_auth) {
		this.cx_auth = cx_auth;
	}
	
	@GET
	@Path("test")
	public Response getTest() {
		return Response.ok("heisann").build();
	}
	
	@GET
	@Produces(MEDIA_TYPE)
	public Response getAll() {
		return getModelAsJsonLd(ontoHelper.getAllPersonsFromModel(getModel()));
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MEDIA_TYPE)
	public Response addPerson(@FormParam("fornavn") String fornavn, 
							  @FormParam("etternavn") String etternavn, 
							  @FormParam("alder") int alder, 
							  @FormParam("studieretning") String studieretning){
		try {
			String id = ontoHelper.addPersonToModel(fornavn, etternavn, alder, studieretning, getModel());
			String uuid = UUID.randomUUID().toString();
			authHelper.setAuth(ontoHelper.getPersonURI(id), uuid);
			
			Model personModel = ontoHelper.getPersonFromModel(id, getModel());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			personModel.write(out, RDF_FORMAT);
			return Response.ok(out.toString()).header("cx_secret", uuid).build();
		} catch (Exception e){
			e.printStackTrace();
			return Response.status(422).entity("Invalid data").build();
		}
	}
	
	@GET
	@Path("{id}")
	@Produces(MEDIA_TYPE)
	public Response getPerson(@PathParam("id") String id){
		Model personModel = ontoHelper.getPersonFromModel(id, getModel());
		if(personModel.isEmpty())
			return Response.status(404).build();
		return getModelAsJsonLd(personModel);
	}
	
	@PUT
	@Path("{id}/kjenner/{kjenner_id}")
	public Response addKjenner(@PathParam("id") String id, @PathParam("kjenner_id") String kjennerId) {
		if(!isAuthorized(id)){
			return forbidden(); 
		}
			
		Model data = getModel();
		if(!ontoHelper.isPerson(id, data) || !ontoHelper.isPerson(kjennerId, data)){
			return Response.status(404).build();
		}
		
		ontoHelper.addKjenner(id, kjennerId, data);
		return Response.noContent().build();
	}
	
	@PUT
	@Path("{id}/harSett")
	public Response addHarSett(@PathParam("id") String id, String film) {
		if(!isAuthorized(id)){
			return forbidden(); 
		}
		
		Model data = getModel();
		if(!ontoHelper.isPerson(id, data)){
			return Response.status(404).build();
		}
		if(film == null || film.isEmpty()){
			return Response.status(400).build();
		}
		Model movieModel = movieHelper.getMovie(film);
		if(movieModel.isEmpty()){
			return Response.status(400).build();
		}
		ontoHelper.addHarSett(id, film, data);
		local.addModel(movieModel, RdfConnection.DATA_GRAPH);
		return Response.noContent().build();
	}

	private boolean isAuthorized(String id) {
		return authHelper.checkAuth(ontoHelper.getPersonURI(id), cx_auth);
	}
	
	private Response forbidden(){
		return Response.status(403).build();
	}
	
	private Model getModel() {
		return local.getModel(RdfConnection.DATA_GRAPH);
	}
}
