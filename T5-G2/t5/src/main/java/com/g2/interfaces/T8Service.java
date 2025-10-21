package com.g2.interfaces;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import testrobotchallenge.commons.models.dto.score.EvosuiteCoverageDTO;

@Service
public class T8Service extends BaseService {

    /*
     * Per il test locale senza container docker usare:
     * BASE_URL = "http://127.0.0.1:8090"
     */
    private static final String BASE_URL = "http://api_gateway-controller:8090";
    private static final String SERVICE_PREFIX = "compile/evosuite";

    protected T8Service(RestTemplate restTemplate) {
        super(restTemplate, BASE_URL + "/" + SERVICE_PREFIX);

        registerAction("evosuiteUserCoverage", new ServiceActionDefinition(
                params -> evosuiteUserCoverage((String) params[0], (String) params[1], (String) params[2],
                        (String) params[3], (String) params[4]),
                String.class, String.class, String.class, String.class, String.class
        ));

    }

    private EvosuiteCoverageDTO evosuiteUserCoverage(String testClassName, String testClassCode, String classUTName,
                                                     String classUTCode, String classUTPackage) {
        final String endpoint = "/coverage/player";

        JSONObject requestBody = new JSONObject();
        requestBody.put("testClassName", testClassName);
        requestBody.put("testClassCode", testClassCode);
        requestBody.put("classUTName", classUTName);
        requestBody.put("classUTCode", classUTCode);
        requestBody.put("classUTPackage", classUTPackage);

        return callRestPost(endpoint, requestBody, null, null, EvosuiteCoverageDTO.class);
    }
}
