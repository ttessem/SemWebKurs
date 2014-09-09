package com.computas.sem.uib;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.computas.sem.uib.connection.RdfConnection;

class InjectionConfiguration extends AbstractBinder {
	private RdfConnection localConnection;
	private RdfConnection festConnection;
	
	public InjectionConfiguration(RdfConnection local, RdfConnection fest) {
		this.localConnection = local;
		this.festConnection = fest;
	}
	
	@Override
	protected void configure() {	
		//connections
		bind(localConnection).named(RdfConnection.LOCAL).to(RdfConnection.class);
		bind(festConnection).named(RdfConnection.FEST).to(RdfConnection.class);
		
	}
}

