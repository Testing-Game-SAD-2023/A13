package com.example.db_setup;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class TeamRestController {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired 
    RestTemplate restTemplate;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

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

        //MODIFICA FEDERICA
        Team team = teamRepository.findById(ID).orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Team not found"
            )
        );
        /*
        Set<User> users = team.getStudentList();
        for (User user : users) {
            try {
                emailService.sendTeamDeletedEmail(user.getEmail(), team.getName());
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send email notification", e);
            }
        }
        */
        //FINE MODIFICA FEDERICA

        for(String missionId : team.getExerciseList()){
            ResponseEntity<String> response = relayRequest(missionId, HttpMethod.DELETE, null);
            if(response.getStatusCode()!=HttpStatus.OK){
                System.out.println(response.getBody());        
            }
        }
        
        team = null;
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


    @SuppressWarnings("deprecation")
    @PutMapping("/team/{ID}/students")
    public void addStudent(@RequestBody User user, @PathVariable("ID") Integer team_id){
        //MODIFICA FEDERICA
        teamRepository.findById(team_id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")
        );

        Integer user_id = user.getID();
        teamRepository.insertStudent(user_id, team_id);
        /*/
        try {
            emailService.sendStudentAddedToTeamEmail(userRepository.getById(user_id).getEmail(), team.getName());
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email notification", e);
        }
        */
        //FINE MODIFICA FEDERICA
        updateAllExercises(team_id);
    }

        

    @SuppressWarnings("deprecation")
    @DeleteMapping("/team/{ID}/students")
    public void deleteStudent(@RequestBody User user, @PathVariable("ID") Integer team_id){
        int affected_rows = teamRepository.deleteStudent(user.getID(), team_id); //mettiamo prima questa perché altrimenti il vettore in team rimane obsoleto in memoria
        
        
        //MODIFICA FEDERICA
        Team team = teamRepository.findById(team_id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")
        );
            
        if(affected_rows==0){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"
            );
        }

        /*
        try {
            emailService.sendStudentRemovedFromTeamEmail(userRepository.getById(user.getID()).getEmail(), team.getName());
        } catch (MessagingException e) {
                throw new RuntimeException("Failed to send email notification", e);
        }
        */
        //FINE MODIFICA FEDERICA
        updateAllExercises(team);
    }




    


    /**
     * 
     * <pre>
     * Ricevuta una descrizione di team, il controller:
     *      aggiunge/aggiorna il campo teamId
     *      aggiunge le email degli studenti del team
     *      invia la richiesta al t5
     *      se riceve l'oggetto missione, ne prende l'id
     *      salva l'id nel team
     * </pre>     
     * 
     * @param json
     * @param team_id
     * @return
     * 
     */
    @PutMapping("/team/{tid}/exercise")
    public ResponseEntity<String> addTeamExercise(@RequestBody String json, @PathVariable("tid") Integer team_id){
        Team team = teamRepository.findById(team_id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")
        );

        final ObjectMapper mapper = new ObjectMapper();
        json = addStudentsToExerciseJson(json, team, mapper);
        
        //richiesta di aggiunta di un esercizio
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        ResponseEntity<String> response = restTemplate.exchange(
            "http://t5-app-1:8080/exercise",
            HttpMethod.PUT,
            new HttpEntity<String>(
                json,
                headers
            ),
            String.class
        );

        //add exercise to team
        if(response.getStatusCode()==HttpStatus.OK){
            try{
                final JsonNode node = mapper.readTree(response.getBody());
                String exercise = node.get("id").asText(null);
                if(exercise==null) throw new IllegalArgumentException("Missing id");
                team.getExerciseList().add(exercise);
                teamRepository.save(team); //<-
            }catch(Exception e){
                System.out.println(response.getBody());
                e.printStackTrace();
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "serialization error on "+response.getBody()+"\n"+e.getMessage()
                );
            }

        }
        return response; //mando pari pari quello che ho ricevuto
        
    }
    

    @DeleteMapping("/team/{tid}/exercise/{mid}")
    public ResponseEntity<String> deleteTeamExercise(@PathVariable("mid") String mission_id, @PathVariable("tid") Integer team_id){
        Team team = teamRepository.findById(team_id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")
        );

        if(!team.getExerciseList().contains(mission_id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mission not found in team");

        ResponseEntity<String> response = relayRequest(mission_id, HttpMethod.DELETE, null);

        //rimozione missione da team
        if(response.getStatusCode()==HttpStatus.OK){
            try{
                final ObjectMapper mapper = new ObjectMapper();
                final JsonNode node = mapper.readTree(response.getBody());
                String exercise = node.get("id").asText(null);
                if(exercise==null) throw new IllegalArgumentException("Missing id");
                team.getExerciseList().remove(exercise);//<-
                teamRepository.save(team);
            }catch(Exception e){
                System.out.println(response.getBody());
                e.printStackTrace();
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "serialization error on "+response.getBody()+"\n"+e.getMessage()
                );
            }

        }

        return response; //mando pari pari quello che ho ricevuto 
    } 


    

    @GetMapping("/team/{tid}/exercise/{mid}")
    public ResponseEntity<String> getTeamExercise(@PathVariable("mid") String mission_id, @PathVariable("tid") Integer team_id){
        Team team = teamRepository.findById(team_id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")
        );
        
        if(!team.getExerciseList().contains(mission_id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mission not found");


        return relayRequest(mission_id, HttpMethod.GET, null);
    }

    @PostMapping("/team/{tid}/exercise/{mid}")
    public ResponseEntity<String> updateTeamExercise(@PathVariable("mid") String mission_id, @PathVariable("tid") Integer team_id, @RequestBody String json){
        Team team = teamRepository.findById(team_id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")
        );
        
        if(!team.getExerciseList().contains(mission_id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mission not found");

        
        final ObjectMapper mapper = new ObjectMapper();
        json = addStudentsToExerciseJson(json, team, mapper);


        
        
        return relayRequest(mission_id, HttpMethod.POST, json);
    }


    private static String addStudentsToExerciseJson(String json, Team team, ObjectMapper mapper){
       
        try{
            Map<String, Object> nodeMap = mapper.readValue(json, new TypeReference<HashMap<String, Object>>(){});
        
            List<String> students = new ArrayList<String>(team.getStudentList().size());
            team.getStudentList().forEach((student)->{students.add(student.email);});
            nodeMap.put("teamId", team.getID());
            nodeMap.put("students",students);
            json = mapper.convertValue(nodeMap, JsonNode.class).toString();

        }catch(Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "serialization error: "+e.getMessage()
            );
        }
        
        return json;
    }

    private ResponseEntity<String> relayRequest(String missionId, HttpMethod method, String body){
        HttpHeaders headers = new HttpHeaders();        
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.exchange(
            "http://t5-app-1:8080/exercise/"+missionId,
            method,
            new HttpEntity<String>(
                body,
                headers
            ),
            String.class
        );

        return response;
    }

    private void updateAllExercises(int team_id){
        Team team = teamRepository.getById(team_id);
        updateAllExercises(team);
    }

    private void updateAllExercises(Team team){    
        //build students array
        String json = addStudentsToExerciseJson("{}", team, new ObjectMapper());
        System.out.println(json);
        for(String mission_id : team.getExerciseList()){
            relayRequest(mission_id, HttpMethod.POST, json);
        }
    }


    // MODIFICA FEDERICA

    @GetMapping("/student_teams/{ID}")
    public List<Team> listStudentTeams(@PathVariable("ID") Integer user_id){
        return teamRepository.getStudentTeams(user_id);
    }
    // FINE MODIFICA FEDERICA

}
