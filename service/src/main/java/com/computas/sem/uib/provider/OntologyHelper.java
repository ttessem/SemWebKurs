package com.computas.sem.uib.provider;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.computas.sem.uib.connection.RdfConnection;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

public class OntologyHelper {
	public static final Property RDF_TYPE = ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
	public static final Property RDFS_LABEL = ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
	public static final Property RDFS_DOMAIN = ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#domain");
	
	@Inject @Named(RdfConnection.LOCAL) private RdfConnection local;
	private static int personID = 1;
	
	public Model getOntologyModel() {
		return local.getModel(RdfConnection.ONTOLOGY_GRAPH);	
	}
	
	public void setOntologyModel(Model m) {
		local.setModel(m, RdfConnection.ONTOLOGY_GRAPH);
	}
	
	public String addPersonToModel(String fornavn, String etternavn, int alder, String studieretning, Model data){
		String id = getNewPersonId();
		Resource person = getPersonURI(id);
		
		data.add(person, RDF_TYPE, getPersonClass());
		data.add(person, getFornavnPredicate(), fornavn);
		data.add(person, getEtternavnPredicate(), etternavn);
		data.addLiteral(person, getAlderPredicate(), alder);
		data.add(person, getStudieretningPredicate(), studieretning);
		return id;
	}
	
	public void addKjenner(String id, String kjennerId, Model data) {
		data.add(getPersonURI(id), getKjennerPredicate(), getPersonURI(kjennerId));
	}

	public Model getPersonFromModel(String id, Model data) {
		Query q = QueryFactory.create("DESCRIBE <"+getPersonURI(id).getURI()+">");
		QueryExecution queryExec = QueryExecutionFactory.create(q, data);
		try {
			return queryExec.execDescribe();
		}
		finally {
			queryExec.close();
		}
	}
	
	public Model getAllPersonsFromModel(Model data) {
		Query q = QueryFactory.create("DESCRIBE ?s WHERE { ?s a <"+getPersonClass()+"> }");
		QueryExecution queryExec = QueryExecutionFactory.create(q, data);
		try {
			return queryExec.execDescribe();
		}
		finally {
			queryExec.close();
		}
	}
	
	public List<String> getPersonPredicates() {
		List<String> predicates = new LinkedList<>();
		ResIterator predicatesIt = getOntologyModel().listResourcesWithProperty(RDFS_DOMAIN, getPersonClass());
		while(predicatesIt.hasNext()) {
			predicates.add(predicatesIt.next().getURI());
		}
		
		return predicates;
	}
	
	public boolean isPerson(String id, Model data) {
		return data.contains(getPersonURI(id), RDF_TYPE, getPersonClass());
	}

	private Resource getPersonClass() {
		return getSubjectWithLabel("Person");
	}
	
	private Property getAlderPredicate() {		
		return getPropertyWithLabel("alder");
	}
	
	private Property getFornavnPredicate() {
		return getPropertyWithLabel("fornavn");
	}	
	
	private Property getEtternavnPredicate() {
		return getPropertyWithLabel("etternavn");
	}
	
	private Property getStudieretningPredicate() {
		return getPropertyWithLabel("studieretning");
	}
	
	private Property getKjennerPredicate() {
		return getPropertyWithLabel("kjenner");
	}

	private Property getPropertyWithLabel(String label) {
		return getSubjectWithLabel(label).as(Property.class);
	}
	
	private Resource getSubjectWithLabel(String label) {
		try {
			return getOntologyModel().listSubjectsWithProperty(RDFS_LABEL, label).next();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	private String getNewPersonId(){
		return Integer.toString(personID++);
	}
	
	private Resource getPersonURI(String id) {
		return ResourceFactory.createResource("http://semweb.computas.com/uib/person/"+id);
	}
}
