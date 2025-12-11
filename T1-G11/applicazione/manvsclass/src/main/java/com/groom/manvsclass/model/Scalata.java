package com.groom.manvsclass.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/*
 * the @Document annotation is used to indicate that an instance
 * of the class should be stored as a document in a MongoDB collection
 *
 * the @Id annotation is used to specify the primary key field of the entity
 */
@Document(collection = "scalate")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Scalata {

    @Id
    private String name;

    // meta dati creazione
    private String date;
    private String username;

    private String description;
    private int numberOfLevels;
    private List<Level> listOfLevels;

}
