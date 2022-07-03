package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Movie> listAllMovies(){
		  String sql = "SELECT * FROM movies";
		  List<Movie> result = new ArrayList<Movie>();
		  Connection conn = DBConnect.getConnection();

		  try {
		   PreparedStatement st = conn.prepareStatement(sql);
		   ResultSet res = st.executeQuery();
		   while (res.next()) {

		    Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
		      res.getInt("year"), res.getDouble("rank"));
		    
		    result.add(movie);
		   }
		   conn.close();
		   return result;
		   
		  } catch (SQLException e) {
		   e.printStackTrace();
		   return null;
		  }
		 }
	
	/*public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	public List<String> listGeneri(){
		String sql = "select distinct genre from movies_genres ORDER BY genre";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				String movie =res.getString("genre");
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Actor> getVertici(String g, Map<Integer, Actor> idMap) {
		String sql= "SELECT DISTINCT a.* "
				+ "FROM  roles r, movies_genres md, actors a "
				+ "WHERE r.actor_id=a.id AND md.movie_id=r.movie_id AND "
				+ "md.genre=? ";
		List<Actor> result= new ArrayList<>(); 
		
		try {
			Connection conn= DBConnect.getConnection(); 
			PreparedStatement st= conn.prepareStatement(sql);
			st.setString(1, g);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				idMap.put(res.getInt("id"), actor);
				result.add(actor);
			}
			conn.close();
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error"); //la runniamo
		}
		return result; 
	}
	
	public List<Adiacenza> getArchi(String g, Map<Integer, Actor> idmap){
		String sql="SELECT a1.id, a2.id, COUNT(mg.movie_id) AS peso "
				+ "FROM actors a1, actors a2, roles r1, roles r2, movies_genres mg "
				+ "WHERE  mg.genre=? AND a1.id>a2.id "
				+ "AND a1.id=r1.actor_id AND a2.id=r2.actor_id "
				+ "AND mg.movie_id=r1.movie_id AND mg.movie_id=r2.movie_id "
				+ "GROUP BY a1.id, a2.id ";
		List<Adiacenza> result=  new ArrayList<>(); 
		
		try {
			Connection conn= DBConnect.getConnection(); 
			PreparedStatement st= conn.prepareStatement(sql);
			st.setString(1, g);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				result.add(new Adiacenza(idmap.get(res.getInt("a1.id")), idmap.get(res.getInt("a2.id")), res.getInt("peso"))); 
			}
			conn.close();
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error"); //la runniamo
		}
		return result; 
	}
	
	
	
	
}
