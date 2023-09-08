package com.groom.manvsclass.model.repository;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import com.groom.manvsclass.model.Admin;
import com.groom.manvsclass.model.ClassUT;
import com.groom.manvsclass.model.interaction;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;


@Component
public class SearchRepositoryImpl {

	@Autowired
    MongoClient client;
 
    @Autowired
    MongoConverter converter;

    public long getLikes(String name) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("interaction");

        Bson textSearch = Filters.text(name);
        Bson typeFilter = Filters.eq("type", 1);

        long count = collection.countDocuments(Filters.and(textSearch, typeFilter));

        return count;
    }
    public List<interaction> findReport() {

        final List<interaction> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("interaction");

        AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(
    		 new Document("$match",
     	            new Document("type", 0)
     	        )
            )
        );

        result.forEach(doc -> posts.add(converter.read(interaction.class,doc)));

        return posts;
    }
    
    public List<ClassUT> findByText(String text) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("ClassUT");

        Bson search = Filters.regex("name", ".*" + text + ".*", "i");

        List<ClassUT> posts = new ArrayList<>();
        collection.find(search).forEach(doc -> posts.add(converter.read(ClassUT.class, doc)));

        return posts;
    }
    
  
    
    
    public Admin findAdminByUsername(String username) {
    	MongoDatabase database = client.getDatabase("manvsclass");
    	MongoCollection<Document> collection = database.getCollection("Admin");
        Bson filter = Filters.eq("username", username);
        Document result = collection.find(filter).first();
        if (result == null) {
            return null;
        }
        Admin admin = new Admin("gg","ff","gg","hh");
        admin.setUsername(result.getString("username"));
        admin.setPassword(result.getString("password"));
        return admin;
    }
    
    public List<ClassUT> searchAndFilter(String text, String category) {

        final List<ClassUT> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("ClassUT");

        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(
                        new Document("$search",
                                new Document("index", "default")
                                        .append("text",
                                                new Document("query", text)
                                                        .append("path", Arrays.asList("name", "description")))),
                        new Document("$match",
                                new Document("category", category))
                )
        );

        result.forEach(doc -> posts.add(converter.read(ClassUT.class,doc)));

        return posts;
    }
    
    public List<ClassUT> filterByCategory(String category) {

        final List<ClassUT> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("ClassUT");

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search", 
        	    new Document("index", "default")
                .append("text", 
        new Document("query", category)
                    .append("path", "category")))));

        result.forEach(doc -> posts.add(converter.read(ClassUT.class,doc)));

        return posts;
    }
    
    
    public List<ClassUT> orderByDate() {

        final List<ClassUT> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("ClassUT");
		
		AggregateIterable<Document> result = collection.aggregate(
	    Arrays.asList(
	        new Document("$sort",
	            new Document("date", 1)
	        	)
	    	)
		);

        result.forEach(doc -> posts.add(converter.read(ClassUT.class,doc)));

        return posts;
    }
    
    public List<ClassUT> orderByName() {

        final List<ClassUT> posts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("ClassUT");
		
		AggregateIterable<Document> result = collection.aggregate(
	    Arrays.asList(
	        new Document("$sort",
	            new Document("name", 1)
	        	)
	    	)
		);

        result.forEach(doc -> posts.add(converter.read(ClassUT.class,doc)));

        return posts;
    }
    
    public List<ClassUT> filterByDifficulty(String difficulty) {
        MongoDatabase database = client.getDatabase("manvsclass");
        MongoCollection<Document> collection = database.getCollection("ClassUT");

        Bson filter = Filters.eq("difficulty", difficulty);

        List<ClassUT> posts = new ArrayList<>();
        collection.find(filter).forEach(doc -> posts.add(converter.read(ClassUT.class, doc)));

        return posts;
    }
        
        public List<ClassUT> searchAndDFilter(String text, String difficulty) {

            final List<ClassUT> posts = new ArrayList<>();

            MongoDatabase database = client.getDatabase("manvsclass");
            MongoCollection<Document> collection = database.getCollection("ClassUT");

            AggregateIterable<Document> result = collection.aggregate(
                    Arrays.asList(
                            new Document("$search",
                                    new Document("index", "default")
                                            .append("text",
                                                    new Document("query", text)
                                                            .append("path", Arrays.asList("name", "description")))),
                            new Document("$match",
                                    new Document("difficulty", difficulty))
                    )
            );

            result.forEach(doc -> posts.add(converter.read(ClassUT.class,doc)));

            return posts;
        }    
    
}
