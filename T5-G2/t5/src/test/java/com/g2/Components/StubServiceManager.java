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

package com.g2.Components;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.g2.Interfaces.ServiceManager;

@Profile("test")
@Service
public class StubServiceManager extends ServiceManager {

    private Object returnedObject;
    
    public StubServiceManager(RestTemplate restTemplate) {
        super(restTemplate);
    }
    
    @Override
    public Object handleRequest(String serviceName, String action, Object... params) {
        switch(action){
            case "executelogic_true" -> {
                return true;
            }
            case "executeLogic_false" -> {
                return false;
            }
            case "executeLogic_Object" ->{
                return this.returnedObject;
            }
            case "executelogic_exception" ->{
                throw new RuntimeException("Simulated exception");
            }
        }
        return false;
    }

    public void setReturnedObject(Object returnedObject) {
        this.returnedObject = returnedObject;
    }

}
