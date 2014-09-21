package com.computas.sem.uib.helpers;

import javax.inject.Inject;
import javax.inject.Named;

import com.computas.sem.uib.connection.RdfConnection;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;

import static com.computas.sem.uib.connection.RdfConnection.*;

public class AuthHelper {
//	private static final Property USER_ID = ResourceFactory.createProperty("http://semweb.computas.com/cx_auth/hasId");
	private static final Property SECRET = ResourceFactory.createProperty("http://semweb.computas.com/cx_auth/hasSecret");
	
	@Inject @Named(RdfConnection.LOCAL) private RdfConnection local;

	public void setAuth(Resource resource, String uuid) {
		Model authGraph = getModel();
		authGraph.removeAll(resource, SECRET, (RDFNode) null);
		authGraph.add(resource, SECRET, ResourceFactory.createPlainLiteral(uuid));
		local.setModel(authGraph, AUTH_GRAPH);
	}
	
	public boolean checkAuth(Resource resource, String secret) {
		if(secret == null)
			return false;
		return getModel().contains(resource, SECRET, secret);
	}	
	
	private Model getModel() {
		return local.getModel(AUTH_GRAPH);
	}
}
