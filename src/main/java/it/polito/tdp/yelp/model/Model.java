package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge> grafo;
	private Map<String, Business> businessIdMap; //crearla sempre
	

	public Model() {
		super();
		this.dao = new YelpDao();
		
		this.businessIdMap = new HashMap<String, Business>();
		
		//Popoliamo l'identity map, in caso ci servisse dopo
		List<Business> businesses = this.dao.getAllBusiness();
		for (Business b : businesses) {
			this.businessIdMap.put(b.getBusinessId(), b);
		}

	}
	
	
	public void creaGrafo(String city) {
		//costruzione di un nuovo grafo
		this.grafo = new SimpleWeightedGraph<Business, DefaultWeightedEdge>(DefaultWeightedEdge.class);
				
		//assegnazione dei vertici
		List<Business> vertici = this.dao.getVertici(city);
		Graphs.addAllVertices(this.grafo, vertici);
				
		//assegnazione degli archi
		List<Arco> archi = this.dao.getArchi(city);
		for (Arco a : archi) {
			Business b1 = this.businessIdMap.get(a.getBusiness_id1());
			Business b2 = this.businessIdMap.get(a.getBusiness_id2());
			double peso = a.getLat1(); //il peso è N ed è un'attributo della classe arco
			Graphs.addEdgeWithVertices(this.grafo, b1, b2, peso);
		}
	}
	
	
	
	
	public List<String> getCitta(){
		return this.dao.getCitta();
	}
	
	
	
	
	public List<Business> getVertici(String city){
		return this.dao.getVertici(city);
	}
	
	
	
	public int getNArchi(){
		return this.grafo.edgeSet().size();
	}
	
	
	
}
