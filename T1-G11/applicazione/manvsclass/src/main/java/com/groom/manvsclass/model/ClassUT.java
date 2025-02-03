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
	private List<String>robotList;
	private List<String>robotDifficulty;
	private List<String>coverage;
	
	
	//Modifica: aggiunta della lista di Robot, delle difficoltà dei Robot e delle coverage
	public ClassUT(String name, String date,String description, String difficulty, String code_Uri,List<String> category, List<String>robotList, List<String> robotDifficulty, List<String> coverage) {
        this.name = name;
        this.date = date;
        this.difficulty = difficulty;
        this.code_Uri = code_Uri;
        this.description=description;
        this.category = category;
		this.robotList=robotList;
		this.robotDifficulty=robotDifficulty;
		this.coverage = coverage;
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
	public void setdifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	
	//Modifica: Aggiunta dei get e set della lista dei robot
	
	public List<String> getRobotList() {
        return robotList;
    }

    public void setRobotList(List<String> robotList) {
        this.robotList = robotList;
    }

	public List<String> getRobotDifficulty() {
        return robotDifficulty;
    }
	

    public void setRobotDifficulty(List<String> robotDifficulty) {
        this.robotDifficulty = robotDifficulty;
    }

	public List<String> getcoverage() {
        return coverage;
    }

    public void setCoverage(List<String> coverage) {
        this.coverage = coverage;
    }

	
	@Override
	public String toString() {
	    return "ClassUT{" +
	            "name='" + name + '\'' +
	            ", date='" + date + '\'' +
	            ", difficulty='" + difficulty + '\'' +
	            ", code_url='" + code_Uri + '\'' +
	            ", category=" + category + '\'' + 
				", coverage=" + coverage + '\'' + 
				", robot=" + robotList + '\'' + 
				", robotDifficulty "+ robotDifficulty +
	            '}';
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	    
}
