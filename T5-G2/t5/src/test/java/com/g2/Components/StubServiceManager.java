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

import com.g2.Interfaces.ServiceInterface;
import com.g2.Interfaces.ServiceManager;

@Profile("test")
@Service
public class StubServiceManager extends ServiceManager {

    private Object returnedObject;
    private Boolean set_exception = false;
    private Boolean return_value;

    public StubServiceManager(RestTemplate restTemplate) {
        super(restTemplate);
        // TODO Auto-generated constructor stub
    }
    
    public void setShouldReturnTrue(Boolean return_value){
        this.return_value = return_value;
    }

    public void setShouldThrowException(){
        this.set_exception = true;
    }


    @Override
    public Object handleRequest(String serviceName, String action, Object... params) {
        
        if(action.equals("executelogic_true") || 
            action.equals("executelogic_false") ||
            action.equals("executelogic_object") ||
            action.equals("executelogic_exception ")){
                switch(action){
                    case "executelogic_true" -> {
                        return true;
                    }
                    case "executeLogic_false" ->{
                        return false;
                    }
                    case "executeLogic_object" ->{
                        return returnedObject;
                    }
                    case "executeLogic_exception" ->{
                        throw new RuntimeException("Simulated exception");
                    }
                }
        }
        
        if(set_exception) throw new RuntimeException("Simulated exception");
        if(returnedObject != null) return returnedObject;
        return return_value;
    }

    public void setReturnedObject(Object returnedObject) {
        this.returnedObject = returnedObject;
    }

}
