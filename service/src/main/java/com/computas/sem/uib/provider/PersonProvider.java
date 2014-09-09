package com.computas.sem.uib.provider;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("uib/person")
public class PersonProvider {

	@GET
	public String getName() {
		return "Alex" ;
	}
}
