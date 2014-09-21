package com.computas.sem.uib.helpers;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hp.hpl.jena.rdf.model.Model;

public class Utils {
	public static final String RDF_FORMAT = "JSON-LD";
	public static final String MEDIA_TYPE = MediaType.APPLICATION_JSON;

	public static Response getModelAsJsonLd(Model m) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		m.write(out, RDF_FORMAT);
		return Response.ok(out.toString()).build();
	}

}
