package com.robotchallenge.codecompile.jacoco;

import java.io.IOException;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class MockCompilationService extends CompilationService {

    public MockCompilationService(String testingClassName, String testingClassCode,
            String underTestClassName, String underTestClassCode,
            String mvn_path) {
        super(testingClassName, testingClassCode,
                underTestClassName, underTestClassCode,
                mvn_path);
        //this.config.setTimestamp("20241104112530123");
        creaCartella();
    }

    public void creaCartella() {
        try {
            createDirectoriesAndCopyPom();
        } catch (IOException e) {
            logger.error("[Compilation Service] [I/O ERROR] ", e);
        }
    }

}
