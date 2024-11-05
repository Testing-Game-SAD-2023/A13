package com.g2.Components;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.g2.Interfaces.ServiceInterface;
import com.g2.Interfaces.ServiceManager;

@Profile("test")
@Service
public class StubServiceManager extends ServiceManager {

    private boolean shouldReturnTrue;
    private boolean shouldThrowException;
    private Object returnedObject;

    public StubServiceManager(RestTemplate restTemplate) {
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

    //Aggiunti per simulare handlerequest per i test del package components
    public void setShouldReturnTrue(boolean shouldReturnTrue) {
        this.shouldReturnTrue = shouldReturnTrue;
    }

    public void setShouldThrowException(boolean shouldThrowException) {
        this.shouldThrowException = shouldThrowException;
    }
    
    @Override
    public Object handleRequest(String serviceName, String action, Object... params) {
        if (shouldThrowException) {
            throw new RuntimeException("Simulated exception");
        }
        if ("executelogic".equals(action)) {
            return shouldReturnTrue;
        } else {
            return returnedObject;
        }
    }

    public void setReturnedObject(Object returnedObject) {
        this.returnedObject = returnedObject;
    }

}
