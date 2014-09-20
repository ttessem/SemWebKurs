package com.computas.sem.uib.provider;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.computas.sem.uib.connection.RdfConnection;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;

import static com.computas.sem.uib.provider.Utils.getModelAsJsonLd;

@Path("/service/sparql")
public class SparqlProvider {
	
	@Inject @Named(RdfConnection.LOCAL) private RdfConnection local;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("text/turtle")
	public Response sparql(@FormParam("query") String queryString) {
		try {
			System.out.println(queryString);
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
//		else if(query.isSelectType()){
//			return getModelAsJsonLd(local.executeSelect(query)); 
//		}
		return Response.status(422).build();
	}
}
