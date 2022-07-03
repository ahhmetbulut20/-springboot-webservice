package com.spring.movieSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@RestController
public class MovieController {
	
	@Autowired
	private IMovieService service; 
	
	@RequestMapping(path="/movies/search", method=RequestMethod.GET)
	public List<Movie> search(@RequestParam(name="movie_name") String name) {
		return this.service.search(name);	
	}
	
	@RequestMapping(path="/addToList", method=RequestMethod.GET)
	public boolean addToList(@RequestParam(name="id") String name) throws IOException{
		Movie m=this.service.findById(name);
		String fileLine = m.getImdbId()+","+m.getTitle()+","+m.getType()+","+m.getYear();
		BufferedWriter wr= new BufferedWriter(new FileWriter(new File("list.txt"),true));
		wr.write(fileLine+"\n"+" ");
		//wr.newLine();
		wr.close();
		return true;
	}
	
	@RequestMapping(path="/movies/detail", method=RequestMethod.GET)
    public String detail(@RequestParam(name = "movie_id") String name){
	    Movie m=this.service.detail(name);
	    String message=null;
	    if(m.getTitle()!=null)
	    	message="ImdbID = "+m.getImdbId()+"\nTitle = "+m.getTitle()+"\nType = "+m.getType()+"\nYear = "+m.getYear();
	    	
	    return message;
	}
	
	
}
