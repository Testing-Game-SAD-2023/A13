package com.groom.manvsclass.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document(collection = "ClassUT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassUT {
    @Id
    @Indexed
    private String name;
    private String date;
    private String difficulty;
    private String uri;
    private String description;
    private List<String> category;

    @Override
    public String toString() {
        return "ClassUT{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", code_url='" + uri + '\'' +
                ", category=" + category +
                '}';
    }

}
