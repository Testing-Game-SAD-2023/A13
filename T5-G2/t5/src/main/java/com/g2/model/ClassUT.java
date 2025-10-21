/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.g2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ClassUT {

    @JsonProperty("name")
    private String name;
    @JsonProperty("date")
    private String date;
    @JsonProperty("difficulty")
    private String difficulty;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("description")
    private String description;
    @JsonProperty("category")
    private List<String> category;

    public ClassUT(String name, String date, String description, String difficulty, String uri, List<String> category) {
        this.name = name;
        this.date = date;
        this.difficulty = difficulty;
        this.uri = uri;
        this.description = description;
        this.category = category;
    }

    // Costruttore senza argomenti
    public ClassUT() {
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

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
                ", code_url='" + uri + '\'' +
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
