package com.groom.manvsclass.model;



import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "interaction")
public class interaction {
	private int id_i; //Id interazione
	private String email; //Email utente
	private String	name; //Id classe
	private long id; //Id utente
	private int type; //Tipo di interazione
	private String commento;
	private String	date;
	
	
	public interaction(int id_i,String email, String name, long id, int type,String date) {
		this.setId_i(id_i);
		this.setEmail(email);
		this.setName(name);
		this.setId(id);
		this.setType(type);
		this.setDate(date);
    }
	
	public interaction()
	{
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCommento() {
		return commento;
	}

	public void setCommento(String commento) {
		this.commento = commento;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
	    return "interaction{" +
	    		"class_id= "+id_i+'\''+
	    		"class_name='" + name+ '\''+
	            "user_id='" + id + '\'' +
	            "user_email="+email+'\''+
	            ", type='" + type + '\'' +
	            ", commento='" + commento + 
	            "date="+date+'\''+
	            '}';
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId_i() {
		return id_i;
	}

	public void setId_i(int id_i) {
		this.id_i = id_i;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}