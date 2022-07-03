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

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model{

	private ImdbDAO dao; 
	List <String> genere;
	List <Actor> actors;
	private Graph<Actor, DefaultWeightedEdge> grafo; 
	Map <Integer, Actor> idmap; 
	
	
	public Model() {
		this.dao = new ImdbDAO();
		this.genere = dao.listGeneri();
		
	}
	
	public List<String> getGenere() {
		return this.genere;
	} 
	
	public  String creaGrafo(String g) {
		this.grafo= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		idmap= new HashMap<>(); 
		 
		//Aggiungi vertici 
		this.actors= dao.getVertici(g, idmap);
		Graphs.addAllVertices(this.grafo, this.actors);
		
		for(Adiacenza a : dao.getArchi(g, idmap)) {
			if(grafo.getEdge(a.getId1(), a.getId2()) == null) {
				Graphs.addEdgeWithVertices(grafo, a.getId1(), a.getId2(), a.getPeso());
			}
		}
		
		return "Grafo creato con " +this.grafo.vertexSet().size()+ " vertici e "
				+ this.grafo.edgeSet().size()+ " archi";
	}

	public List<Actor> getActors() {
		Collections.sort(actors);
		return actors;
	}
	
	public List<Actor> getAdiacenti(Actor a) { 
		//COMPONENTE CONNESSA
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<Actor, DefaultWeightedEdge>(grafo);
		List<Actor> actorsConnessi = new ArrayList<>(ci.connectedSetOf(a)); //torna la componente connessa a partire da a
		//actors.remove(actorsConnessi.get(0));
		Collections.sort(actorsConnessi);
		return actorsConnessi;
	}

	public void setActors(List<Actor> actors) {
		this.actors = actors;
	}
	
	
}


