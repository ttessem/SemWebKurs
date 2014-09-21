package com.computas.sem.uib.provider;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.ApplicationPath;
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
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import static com.computas.sem.uib.helpers.Utils.*;

@Path("/service/uib/person")
@Api(value = "/service/uib/person/", description= "Person api")
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
	@Produces(MEDIA_TYPE)
	@ApiOperation( value = "Get all persons", notes = "Returns a sparql describe query of all persons as JSON-LD.", produces = MEDIA_TYPE)
	public Response getAll() {
		return getModelAsJsonLd(ontoHelper.getAllPersonsFromModel(getModel()));
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MEDIA_TYPE)
	@ApiOperation(value = "Post new person", notes = "Add a new person and returns it as JSON-LD. The persons auth key is in header as cx_secret.", consumes = MediaType.APPLICATION_FORM_URLENCODED)
	@ApiResponses(value = {
			@ApiResponse(code=200, message = "Successfully added the person."),
			@ApiResponse(code=422, message = "Error parsing the form data.")
	})
	public Response addPerson(@ApiParam(required = true) @FormParam("fornavn") String fornavn, 
							  @ApiParam(required = true) @FormParam("etternavn") String etternavn, 
							  @ApiParam(required = true) @FormParam("alder") int alder, 
							  @ApiParam(required = true) @FormParam("studieretning") String studieretning){
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
	@Path("/{id}")
	@Produces(MEDIA_TYPE)
	@ApiOperation(value = "Get a person.", notes = "Gets a person by id and returns as JSON-LD.")	
	@ApiResponses(value = {
			@ApiResponse(code=200, message = "The person was found."),
			@ApiResponse(code=404, message = "Person not found.")
	})
	public Response getPerson(@ApiParam(value = "Person id") @PathParam("id") String id){
		Model personModel = ontoHelper.getPersonFromModel(id, getModel());
		if(personModel.isEmpty())
			return Response.status(404).build();
		return getModelAsJsonLd(personModel);
	}
	
	@PUT
	@Path("/{id}/kjenner/{kjenner_id}")
	@ApiOperation(value = "Put a knows relationship.", notes = "Puts a knows relationship between two persons. Requires authorization as cx_auth in header (the key returned from post person as cx_secret).")
	@ApiResponses(value = {
			@ApiResponse(code=201, message = "Relationship added."),
			@ApiResponse(code=403, message = "Not authorized with correct key in cx_auth."),
			@ApiResponse(code=404, message = "Person not found.")
	})
	public Response addKjenner(@ApiParam(value = "The id (integer) of the person that adds the relationship.") @PathParam("id") String id, 
							@ApiParam(value = "The id (integer) of the other person.")@PathParam("kjenner_id") String kjennerId) {
		if(!isAuthorized(id)){
			return forbidden(); 
		}
			
		Model data = getModel();
		if(!ontoHelper.isPerson(id, data) || !ontoHelper.isPerson(kjennerId, data)){
			return Response.status(404).build();
		}
		
		ontoHelper.addKjenner(id, kjennerId, data);
		return Response.status(201).build();
	}
	
	@PUT
	@Path("/{id}/harSett")
	@Consumes(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Put a hasSeen movie to person", notes = "Puts a hasSeen movie from person_id and movie. Requires authorization as cx_auth in header (the key returned from post person as cx_secret).")
	@ApiResponses(value = {
			@ApiResponse(code=201, message = "hasSeen movie added."),
			@ApiResponse(code=400, message = "Missing movie uri payload."),
			@ApiResponse(code=403, message = "Not authorized with correct key in cx_auth."),
			@ApiResponse(code=404, message = "Person or movie not found.")
	})
	public Response addHarSett(
			@ApiParam(value="The person that has seen a movie.") @PathParam("id") String id, 
			@ApiParam(required = true, value="The uri of the movie.") String film) {
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
			return Response.status(404).build();
		}
		ontoHelper.addHarSett(id, film, data);
		local.addModel(movieModel, RdfConnection.DATA_GRAPH);
		return Response.status(201).build();
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
