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
	private Graph<Business, DefaultWeightedEdge> grafo ;
	private List<Business> allNodes; //tutti gli oggetti
	private Map<String, Business> idMap; //come String considero l'id del business
	
	
	public Model() {
		this.dao = new YelpDao();
		this.allNodes = new ArrayList<>();
		this.idMap = new HashMap<>();
	}
	
	
	
	//metodo per creare il grafo
	public void creaGrafo(String citta) {
		loadNodes();
		this.grafo = new SimpleWeightedGraph(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, this.dao.getVertici(citta));
	
		List<Arco> allEdges = this.dao.getArchi(citta, idMap);
		
		for (Arco edge : allEdges) {
			Graphs.addEdgeWithVertices(this.grafo, edge.getB1(), edge.getB2(), edge.getPeso());
		}
		
		System.out.println("Grafo creato");
		System.out.println("ci sono " + this.grafo.vertexSet().size() + " vertici e " + this.grafo.edgeSet().size()+ " archi.");
	}
	
	
	
	
	
	//metodo per riempire la mappa con le coppie id-business
	private void loadNodes(){
		if(this.allNodes.isEmpty()) { //se la lista dei business è vuota
			this.allNodes = this.dao.getAllBusiness(); //riempi la lista business
		}
			
		if(this.idMap.isEmpty()) {
			for(Business b : this.allNodes) { //per ciascun business nella lista business
				this.idMap.put(b.getBusinessId(), b);  //registralo nella mappa con il suo id
			}
		}
	}
	
	
	
	//metodo per trovare l'altro nodo a estremo dell'arco di peso massimo
	public List<Business> businessPiuLontani(Business b) {
		int max = 0 ;
		for(DefaultWeightedEdge e: this.grafo.edgesOf(b)) {
			if(this.grafo.getEdgeWeight(e)>max) {
				max = (int)this.grafo.getEdgeWeight(e);				
			}
		}
			
		List<Business> result = new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgesOf(b)) {
			if((int)this.grafo.getEdgeWeight(e) == max) {
				Business b2 = Graphs.getOppositeVertex(this.grafo, e, b);
				result.add(b2);
			}
		}
		return result ;
	}
	
	
	
	public List<Business> getVertici(String citta){
		return this.dao.getVertici(citta);
	}
	
	
	public List<String> getVerticiNomi(String citta){
		return this.dao.getVerticiNomi(citta);
	}
	
	
	public List<String> getAllCitta(){
		return this.dao.getAllCitta();
	}
	
	
	//metodo che mi restituisce il numero di vertici
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
		
	//metodo che mi restituisce il numero di archi
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
	public List<Business> getAllBusinesses(){
		return this.dao.getAllBusiness();
	}
	
}