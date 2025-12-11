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
/*package com.gateway.apiGateway.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

}
*/
package com.gateway.apiGateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver; // Importa l'interfaccia

//import com.gateway.apiGateway.config.PrincipalNameKeyResolver;(se lo mantengo in config non devo aggiungere l'import)
@Configuration
public class GatewayConfig {

    // Aggiungi questo bean. Il nome del metodo è il nome del bean che userai nell'YAML: "@principalNameKeyResolver"
    @Bean
    public KeyResolver principalNameKeyResolver() {
        return new PrincipalNameKeyResolver(); 
    }

}

