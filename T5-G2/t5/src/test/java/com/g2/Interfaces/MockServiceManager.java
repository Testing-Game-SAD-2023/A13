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

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Profile("test")
@Service
public class MockServiceManager extends ServiceManager {

    private boolean shouldReturnTrue;
    private boolean shouldThrowException;
    private Object returnedObject;

    public MockServiceManager(RestTemplate restTemplate) {
        super(restTemplate);
        // TODO Auto-generated constructor stub
    }

    @Override
    public <T extends ServiceInterface> void registerService(String serviceName, Class<T> serviceClass,
            RestTemplate restTemplate) {
        super.registerService(serviceName, serviceClass, restTemplate); // Esposto come pubblico solo per i test
    }

    @Override
    public <T extends ServiceInterface> T createService(Class<T> serviceClass, RestTemplate restTemplate) {
        return super.createService(serviceClass, restTemplate);
    }

    public boolean hasService(String key) {
        return services.containsKey(key);
    }
}
