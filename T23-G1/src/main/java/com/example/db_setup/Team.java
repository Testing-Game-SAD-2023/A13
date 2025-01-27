package com.example.db_setup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lombok.Data;

@Table(name="Teams", schema="studentsrepo")
@Data
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer ID;

    private String name;
    private String description;

    @JoinTable(
        name="Student_Team_Association",
        joinColumns = @JoinColumn(name="team_id"),
        inverseJoinColumns = @JoinColumn(name="user_id")
    )
    @ManyToMany(fetch=FetchType.LAZY)
    private Set<User> studentList = new HashSet<User>();


    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "Student_Exercise_Association", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "exercise_id", nullable = false)
    private Set<String> exerciseList = new HashSet<String>();

    public Integer getID(){ return ID;}
    public void setID(Integer ID){this.ID = ID;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}

    public Set<User> getStudentList(){return studentList;}
    public Set<String> getExerciseList(){return exerciseList;}

    public static class ReducedDTO{
        private String name;
        private String description;
        private Integer ID;

        public String getName(){
            return name;
        }
        public String getDescription(){
            return description;
        }
        public Integer getID(){
            return ID;
        }
        public ReducedDTO(Team team){
            super();
            this.name = team.name;
            this.description = team.description;
            this.ID = team.ID;
        }

        public ReducedDTO(String name, String description){
            this.name = name;
            this.description = description;
        }

    }
}