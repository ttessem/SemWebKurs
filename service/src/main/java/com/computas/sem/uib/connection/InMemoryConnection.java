package com.computas.sem.uib.connection;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.GraphUtil;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.core.DatasetGraph;
import com.hp.hpl.jena.sparql.core.DatasetGraphFactory;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateRequest;

/**
 * Created with IntelliJ IDEA.
 * User: jkm
 * Date: 11/4/13
 * Time: 9:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class InMemoryConnection implements RdfConnection {

    private static final String DATA_GRAPH = "data";
	private DatasetGraph datasetGraph;
    private Map<String, Node> grapNodeMap;
	private Model baseOntology;

    public InMemoryConnection(Model baseOntology){
		this.baseOntology = baseOntology;
    }
    
    @PostConstruct
    public void init() {
        datasetGraph = DatasetGraphFactory.createMem();
        grapNodeMap = new HashMap();
    }

    @PreDestroy
    public void destroy() {
        datasetGraph.close();
    }

    private QueryExecution createQueryExecution(Query query) {
//        return QueryExecutionFactory.create(query, DatasetFactory.create(datasetGraph));
    	/**
    	 * TODO:SERIØS HACK!!! DefaultGraph er ikke union av alle named graphs :/ HVORDAN!?!?!?
    	 */
    	Model m = ModelFactory.createUnion(getModel(DATA_GRAPH), baseOntology);
    	return QueryExecutionFactory.create(query, m);
    }


    @Override
    public Model executeConstruct(Query constructQuery) {
        QueryExecution queryExecution = createQueryExecution(constructQuery);

        try {
        	if(constructQuery.isDescribeType()){
        		return queryExecution.execDescribe();
        	}
            return queryExecution.execConstruct();
        } finally {
            queryExecution.close();
        }
    }

    @Override
    public ResultSet executeSelect(Query selectQuery) {
        QueryExecution queryExecution = createQueryExecution(selectQuery);

        try {
            return ResultSetFactory.copyResults(queryExecution.execSelect());
        } finally {
            queryExecution.close();
        }
    }

    @Override
    public Model getModel(String graphName) {
        return ModelFactory.createModelForGraph(datasetGraph.getGraph(getGraphNameNode(graphName)));
    }

    private Node getGraphNameNode(String graphName) {
        if (!grapNodeMap.containsKey(graphName)) {
            grapNodeMap.put(graphName, NodeFactory.createURI(graphName));
        }
        return grapNodeMap.get(graphName);
    }

    @Override
    public void addModel(Model model, String graphName) {
        Node graphNameNode = getGraphNameNode(graphName);
        Graph graph = datasetGraph.getGraph(graphNameNode);

        if (graph == null) {    //add new graph
            datasetGraph.addGraph(graphNameNode, model.getGraph());
        }
        //add to existing graph
        GraphUtil.addInto(graph, model.getGraph());
    }

    @Override
    public void executeUpdate(UpdateRequest updateRequest) {
        UpdateAction.execute(updateRequest, datasetGraph);
    }
}
