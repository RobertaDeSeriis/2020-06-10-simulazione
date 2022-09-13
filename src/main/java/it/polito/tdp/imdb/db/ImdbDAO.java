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
	
	public List<Actor> listAllActors(){
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
	}
	
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
	
	public List<Actor> listVertici(String g, Map<Integer, Actor> idMap){
		String sql = "SELECT DISTINCT a.* "
				+ "FROM roles r, movies_genres mg, actors a "
				+ "WHERE r.movie_id= mg.movie_id AND a.id=r.actor_id "
				+ "AND mg.genre=? "
				+ "order by a.last_name ";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, g);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				idMap.put(res.getInt("id"), actor);
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> listArchi(String g, Map<Integer, Actor> idMap){
		String sql = "SELECT DISTINCT r.actor_id, r1.actor_id, COUNT(mg.movie_id) AS peso "
				+ "FROM roles r, roles r1, movies_genres mg "
				+ "WHERE r.movie_id= mg.movie_id  "
				+ "AND r1.movie_id=mg.movie_id "
				+ "AND r.actor_id>r1.actor_id "
				+ "AND mg.genre=? "
				+ "GROUP BY r.actor_id, r1.actor_id ";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, g);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Adiacenza a = new Adiacenza(idMap.get(res.getInt("r.actor_id")), idMap.get(res.getInt("r1.actor_id")), res.getInt("peso"));
				result.add(a);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
