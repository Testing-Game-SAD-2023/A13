package RemoteCCC.App;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class CompilationServiceTest {

    //private CompilationService compilationService;
    private MockCompilationService mockCompilationService;
    private static final Logger logger = LoggerFactory.getLogger(CompilationServiceTest.class);
    //private AutoCloseable closeable;

    @Mock
    private Config mockConfig;

    @Value("${variabile.mvn}")
    private String mvn_path;

    private static final String BASE_PATH = ".\\src\\test\\resources\\classi_da_testare\\";

    private JSONObject loadTestFiles(String folderName, String testingClassName, String underTestClassName)
            throws Exception {
        // Carica i contenuti dei file e gestisci errori, restituendo un oggetto JSON
        // per la richiesta
        String folderNameT = Paths.get(folderName, testingClassName).toString();
        System.out.println("PATH: " + Paths.get(BASE_PATH, folderNameT, testingClassName + ".java").toString());
        String testingClassCode = readFileContent(Paths.get(BASE_PATH, folderNameT, testingClassName + ".java"));
        String underTestClassCode = readFileContent(Paths.get(BASE_PATH, folderNameT, underTestClassName + ".java"));
        JSONObject requestJson = new JSONObject();
        requestJson.put("testingClassName", testingClassName + ".java");
        requestJson.put("testingClassCode", testingClassCode);
        requestJson.put("underTestClassName", underTestClassName + ".java");
        requestJson.put("underTestClassCode", underTestClassCode);
        System.out.println("[TESTING] RIchiesta generata");
        return requestJson;
    }

    private String readFileContent(Path path) throws IOException {
        // Lettura del file in una stringa, lancia un'eccezione in caso di errore
        if (!Files.exists(path)) {
            throw new IOException("Il file non esiste: " + path);
        }
        return Files.readString(path);
    }

    @BeforeEach
    public void setUp() {

        JSONObject requestJson;
        try {
            requestJson = loadTestFiles("t11", "SubjectParser", "TestSubjectParser");

            // Crea un'istanza di CompilationService con il Config simulato
            mockCompilationService = new MockCompilationService(requestJson.getString("testingClassName"),
                    requestJson.getString("testingClassCode"),
                    requestJson.getString("underTestClassName"),
                    requestJson.getString("underTestClassCode"),
                    mvn_path);

        } catch (IOException e) {
            logger.error("[IOException] [ERROR] ", e);
        } catch (Exception e) {
            logger.error("[Exception] [ERROR] ", e);
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        mockCompilationService.deleteCartelleTest();
    }


    /*
    *     test 11  - cartella con lo stesso nome
     */
    @Test
    public void CaseTest_11() throws IOException {
        CompilationService compilationServiceSpy = spy(mockCompilationService);
        //eccezione quando la directory esiste già
        IOException exception = assertThrows(IOException.class, () -> {
            compilationServiceSpy.createDirectoriesAndCopyPom();
        });
        logger.debug(exception.getMessage());
    }
}
