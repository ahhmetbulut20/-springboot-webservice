package com.spring.movieSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Component
public class MovieService implements IMovieService{
	
	@Override
	public List<Movie> search(String movieName) {
		
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		headers.add("authorization", "apikey 3gRwwiyHvrD6Wm4N1gyBN1:6apbrXKSgFKxvUedGBZYZL");
		String url= "https://api.collectapi.com/imdb/imdbSearchByName?query="+movieName;
		HttpEntity<?>requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String>response=client.exchange(url, HttpMethod.GET, requestEntity,String.class);
		String r=response.getBody();
		System.out.println(r);
		ObjectMapper objectMapper = new ObjectMapper();
		List<Movie>movies=new ArrayList<Movie>();
		try {
			JsonNode node= objectMapper.readTree(r);
			JsonNode resultNode=node.get("result");
			if(resultNode.isArray()) {
				ArrayNode moviesNode = (ArrayNode) resultNode;
				for(int i=0; i<moviesNode.size();i++) {
					JsonNode singleMovie = moviesNode.get(i);
					String title= singleMovie.get("Title").toString();
					String year= singleMovie.get("Year").toString();
					String imdbId= singleMovie.get("imdbID").toString();
					String type= singleMovie.get("Type").toString();
					Movie m=new Movie();
					m.setImdbId(imdbId);
					m.setTitle(title);
					m.setType(type);
					m.setYear(year);
					movies.add(m);
				}
			}
		}
		catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return movies;
	}

	
	
	
	
	@Override
	public Movie findById(String id) {
		
		RestTemplate client = new RestTemplate();
		HttpHeaders headers= new HttpHeaders();
		headers.add("content-type", "application/json");
		headers.add("authorization", "apikey 3gRwwiyHvrD6Wm4N1gyBN1:6apbrXKSgFKxvUedGBZYZL");
		String url= "https://api.collectapi.com/imdb/imdbSearchById?movieId="+id;
		HttpEntity<?>requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String>response=client.exchange(url, HttpMethod.GET, requestEntity,String.class);
		String r=response.getBody();
		System.out.println(r);
		ObjectMapper objectMapper = new ObjectMapper();
		Movie m=new Movie();
		
		try {
					JsonNode node= objectMapper.readTree(r);
					JsonNode resultNode=node.get("result");
			
					String title= resultNode.get("Title").toString();
					String year= resultNode.get("Year").toString();
					String imdbId= resultNode.get("imdbID").toString();
					String type= resultNode.get("Type").toString();
					m.setImdbId(imdbId);
					m.setTitle(title);
					m.setType(type);
					m.setYear(year);
					
		}		
		catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return m;
	}
	
	
	
	
	
	@Override
	public Movie detail(String id) {
		List<Movie>movies=new ArrayList<Movie>();
		Movie m=new Movie();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("list.txt"));
			String line = reader.readLine();
			while (line != null) {
				System.out.println(line);
				line = reader.readLine();
				if(line != null) {
					if(line.contains(id)) {
					String [] movieDetail=line.split(",");
					m.setImdbId(movieDetail[0]);
					m.setTitle(movieDetail[1]);
					m.setType(movieDetail[2]);
					m.setYear(movieDetail[3]);
					return m;
				}
			}
			}
				reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return this.findById(id);
	}
}
