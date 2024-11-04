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

 package com.g2.Interfaces;
 import org.junit.jupiter.api.BeforeEach;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.context.SpringBootTest;
 import org.springframework.test.web.client.MockRestServiceServer;
 import org.springframework.web.client.RestTemplate;

 import com.g2.t5.T5Application;
 
 
 @SpringBootTest(classes = T5Application.class)
 public class T7ServiceTest {
     @Autowired
     private RestTemplate restTemplate;
     private T7Service T7Service;
     private MockRestServiceServer mockServer;
     private final String Base_URL = "http://manvsclass-controller-1:8080";
 
     @BeforeEach
     public void setUp() {
         // Precondizione: Creare un server mock e inizializzare il T1Service
         mockServer = MockRestServiceServer.createServer(restTemplate);
         T7Service = new T7Service(restTemplate);
     }


}