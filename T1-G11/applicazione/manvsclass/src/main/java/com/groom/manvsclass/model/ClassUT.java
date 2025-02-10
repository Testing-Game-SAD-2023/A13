package com.groom.manvsclass.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ClassUT")
public class ClassUT {
	private String	name;
	private String	date;
	private	String	difficulty;
	private String code_Uri;
	private	String	description;
	private List<String> category;
	
	
	
	public ClassUT(String name, String date,String description, String difficulty, String code_Uri,List<String> category) {
        this.name = name;
        this.date = date;
        this.difficulty = difficulty;
        this.code_Uri = code_Uri;
        this.description=description;
        this.category = category;
    }
	
	public ClassUT()
	{
		
	}
	
	public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setcode_Uri(String code_Uri) {
		this.code_Uri = code_Uri;
	}
	
	public String getcode_Uri() {
		return this.code_Uri;
	}
	public void setUri(String code_Uri) {
		this.code_Uri = code_Uri;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	
	
	
	
	@Override
	public String toString() {
	    return "ClassUT{" +
	            "name='" + name + '\'' +
	            ", date='" + date + '\'' +
	            ", difficulty='" + difficulty + '\'' +
	            ", code_url='" + code_Uri + '\'' +
	            ", category=" + category +
	            '}';
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	    
}
