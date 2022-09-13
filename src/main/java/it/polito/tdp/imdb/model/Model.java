package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model{

	ImdbDAO dao; 
	List<String> generi;
	List<Actor> vertici;
	List<Adiacenza> archi;
	Map<Integer, Actor> idMap;
	Graph<Actor, DefaultWeightedEdge> grafo;
	
	
	public Model() {
		this.dao= new ImdbDAO();
	}


	public List<String> getGeneri() {
		return dao.listGeneri();
	}
	
	public String creaGrafo(String g) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap= new HashMap<>();
		vertici= dao.listVertici(g, idMap);
		Graphs.addAllVertices(this.grafo, vertici);
		
		//aggiungo archi
		archi= dao.listArchi(g, idMap);
		
		for(Adiacenza a: archi) {
			if(grafo.containsVertex(a.getId1()) &&  grafo.containsVertex(a.getId2()) && a.getPeso()>0) {
				Graphs.addEdge(this.grafo, a.getId1(), a.getId2(), a.getPeso());
			}
		}
		
		
		return "Grafo creato!\n# Vertici:"+grafo.vertexSet().size()+ "\n# Archi: "+grafo.edgeSet().size();	
	}


	public List<Actor> getVertici() {
		return vertici;
	}


	public List<Adiacenza> getArchi() {
		return archi;
	}
	

	public List<Actor> getAdiacenti(Actor a){
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci= new ConnectivityInspector<>(grafo); 
		List<Actor> attoriConnessi= new ArrayList<>(ci.connectedSetOf(a));
		vertici.remove(attoriConnessi.get(0)); 
		Collections.sort(attoriConnessi); //ordine alfabetico per cognome
		List<Actor> attoriN= new ArrayList<>(attoriConnessi);
		
		return attoriN;
	
	}
}


