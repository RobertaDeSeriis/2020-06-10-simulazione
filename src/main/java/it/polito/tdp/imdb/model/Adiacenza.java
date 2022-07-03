package it.polito.tdp.imdb.model;

public class Adiacenza {

	Actor id1; 
	Actor id2; 
	int peso;
	
	public Adiacenza(Actor id1, Actor id2, int peso) {
		super();
		this.id1 = id1;
		this.id2 = id2;
		this.peso=peso;
	}

	public Actor getId1() {
		return id1;
	}

	public void setId1(Actor id1) {
		this.id1 = id1;
	}

	public Actor getId2() {
		return id2;
	}

	public void setId2(Actor id2) {
		this.id2 = id2;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	
	
}
