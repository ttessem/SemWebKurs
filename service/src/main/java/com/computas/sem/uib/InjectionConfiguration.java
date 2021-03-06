package com.computas.sem.uib;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.computas.sem.uib.connection.RdfConnection;
import com.computas.sem.uib.helpers.AuthHelper;
import com.computas.sem.uib.helpers.MovieHelper;
import com.computas.sem.uib.helpers.OntologyHelper;

class InjectionConfiguration extends AbstractBinder {
	private RdfConnection localConnection;
	private RdfConnection lmdbConnection;
	
	public InjectionConfiguration(RdfConnection local, RdfConnection lmdbConnection) {
		this.localConnection = local;
		this.lmdbConnection = lmdbConnection;
	}
	
	@Override
	protected void configure() {	
		//connections
		bind(localConnection).named(RdfConnection.LOCAL).to(RdfConnection.class);
		bind(lmdbConnection).named(RdfConnection.LMDB).to(RdfConnection.class);
		bind(OntologyHelper.class).to(OntologyHelper.class);
		bind(MovieHelper.class).to(MovieHelper.class);
		bind(AuthHelper.class).to(AuthHelper.class);
	}
}

