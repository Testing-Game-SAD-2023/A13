/*
 *   Copyright (c) 2025 Stefano Marano https://github.com/StefanoMarano80017
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

 package com.g2.Model;

 import com.fasterxml.jackson.annotation.JsonProperty;
 
 public class StatisticProgress {
     @JsonProperty("playerId")
     private Integer PlayerID;
     @JsonProperty("statistic")
     private String StatisticID;
     @JsonProperty("progress")
     private float Progress;
     private String Name;  // Aggiunto il campo per il nome
 
     // Costruttore che include il campo Name
     public StatisticProgress(Integer playerID, String statistic, float progress) {
         PlayerID = playerID;
         StatisticID = statistic;
         Progress = progress;
     }
 
     // Costruttore con Name
     public StatisticProgress(Integer playerID, String statistic, float progress, String name) {
         PlayerID = playerID;
         StatisticID = statistic;
         Progress = progress;
         Name = name;  // Inizializza il nome
     }
 
     // Costruttore di default
     public StatisticProgress() {
     }
 
     public Integer getPlayerID() {
         return PlayerID;
     }
 
     public void setPlayerID(Integer playerID) {
         PlayerID = playerID;
     }
 
     public String getStatisticID() {
         return StatisticID;
     }
 
     public void setStatisticID(String statisticID) {
         StatisticID = statisticID;
     }
 
     public float getProgress() {
         return Progress;
     }
 
     public void setProgress(float progress) {
         Progress = progress;
     }
 
     public String getName() {
         return Name;
     }
 
     public void setName(String name) {
         Name = name;
     }
 
     @Override
     public String toString() {
         return "StatisticProgress{" +
                 "PlayerID=" + PlayerID +
                 ", StatisticID='" + StatisticID + '\'' +
                 ", Progress=" + Progress +
                 ", Name='" + Name + '\'' +  // Aggiunto il nome nella rappresentazione
                 '}';
     }
 }
 