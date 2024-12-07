package com.example.db_setup;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TeamRestController {
    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/team")
    public List<Team.ReducedDTO> getAllTeams(){
        return TeamRepository.mapDto(teamRepository.findAll());
    }

    @PutMapping("/team")
    public Team.ReducedDTO createTeam(@RequestBody Team team){
        teamRepository.save(team);
        return new Team.ReducedDTO(team);
    }

    
    @GetMapping("/team/{ID}")
    public Team getTeam(@PathVariable Integer ID){
        return teamRepository.findById(ID).orElse(null);        
    }

    @DeleteMapping("team/{ID}")
    public void deleteTeam(@PathVariable Integer ID){
        teamRepository.deleteById(ID);
    }
    
    @PatchMapping("/team/{ID}")
    public void modifyTeam(@PathVariable Integer ID, @RequestBody Team.ReducedDTO dto){
        Team team = teamRepository.findById(ID).orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
            )
        );
        if(dto.getName() != null) team.setName(dto.getName());
        if(dto.getDescription() != null) team.setDescription(dto.getDescription());
        teamRepository.save(team);
        
    }
    
    
    @GetMapping("/team/{ID}/students")
    public Set<User> listStudents(@PathVariable("ID") Integer user_id){
        return teamRepository.getById(user_id).getStudentList();
    }

    @PutMapping("/team/{ID}/students")
    public void addStudent(@RequestBody User user, @PathVariable("ID") Integer team_id){
        teamRepository.insertStudent(user.getID(), team_id);    
    }

    @DeleteMapping("/team/{ID}/students")
    public void deleteStudent(@RequestBody User user, @PathVariable("ID") Integer team_id){
        teamRepository.deleteStudent(user.getID(), team_id);
    }



}
