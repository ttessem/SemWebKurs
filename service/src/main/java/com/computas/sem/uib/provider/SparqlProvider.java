package com.computas.sem.uib.provider;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.computas.sem.uib.connection.RdfConnection;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.sparql.util.ResultSetUtils;

import static com.computas.sem.uib.helpers.Utils.getModelAsJsonLd;

@Path("/service/sparql")
public class SparqlProvider {
	
	@Inject @Named(RdfConnection.LOCAL) private RdfConnection local;
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("text/turtle")
	public Response sparqlConstruct(@FormParam("query") String queryString) {
		try {
			Query query = QueryFactory.create(queryString);
			if (query.isDescribeType() || query.isConstructType()) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				local.executeConstruct(query).write(out, "TURTLE");
				return Response.ok(out.toString()).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		return Response.status(422).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/sparql-results+xml")
	public Response postSparqlSelect(@FormParam("query") String queryString) {
		try {
			Query query = QueryFactory.create(queryString);
			System.out.println(query.toString());
			if(query.isSelectType()){
				return Response.ok(ResultSetFormatter.asXMLString(local.executeSelect(query))).build();
			}
			else if(query.isAskType()){
				return Response.ok(ResultSetFormatter.asXMLString(local.executeAsk(query))).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
		return Response.status(422).build();
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("application/sparql-results+xml")
	public Response getSparqlSelect(@QueryParam("query") String queryString) {
		return postSparqlSelect(queryString);
	}
}
