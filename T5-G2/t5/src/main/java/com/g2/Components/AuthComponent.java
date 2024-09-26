package com.g2.Components;

import com.g2.Interfaces.ServiceManager;

public class AuthComponent extends ServiceLogicComponent {

    //Questo è a tutti gli effetti un wrapper per avere velocemente il check del authentication.
    public AuthComponent(ServiceManager serviceManager, String jwt) {
        super(serviceManager, "T23", "GetAuthenticated", jwt);
        SetErrorCode("Auth_error");
    }

}
