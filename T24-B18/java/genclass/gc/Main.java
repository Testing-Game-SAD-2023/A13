package genclass.gc;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Main {

    public static void main(String[] args) throws Exception {
    	  
    
    	
    	
        HttpServer server = HttpServer.create(new InetSocketAddress(8002), 0);  //Avvia server HTTP 
        server.createContext("/generaClasse", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException { //handler
                System.out.println("Request Accepted");
                if ("GET".equals(exchange.getRequestMethod())) {

                boolean esito = false;
                int ntry=0;
                Generator ic = new Generator();

                try {
                    while (!esito) {  // se una delle funzioni genera eccezioni si riavvia la procedura (per un max di 50 volte)
                    	ntry+=1;
  
                    	if(ntry==50)
                          {
                    	   throw new Exception("Fatal error.Program Shutdown NOW");

                          }
                    	
                        try {
                         
                            ic.computeJavaClass();
                            ic.computeJunitTest();
                            esito = true;
                        } catch (Exception e) {
                        	e.printStackTrace();
                            System.out.println("Error.  \nSmart Reboot activacted, computing will be restarted NOW ");

                            esito = false;
                        }
                    }
                  
                    //preparazione model e file
                    byte[] model = ic.getJmodel().toString().getBytes();
                    byte[] file1 = Files.readAllBytes(Paths.get(ic.getGeneratedclassfile().getCanonicalPath()));
                    byte[] file2 = Files.readAllBytes(Paths.get(ic.getGeneratedclassfile().getParentFile().getCanonicalPath()+"/GenRandoopTests.zip"));
                    byte[] file3 = Files.readAllBytes(Paths.get(ic.getGeneratedclassfile().getParentFile().getCanonicalPath()+"/GenEvosuiteTests.zip"));

                    exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
                    exchange.sendResponseHeaders(200, file1.length + file2.length + file3.length + model.length);
                    
                    
                   //invio
                    OutputStream os = exchange.getResponseBody();
                    os.write(model);
                    os.write(file1);
                    os.write(file2);
                    os.write(file3);



                    os.close();

                } catch (Exception e) {
                    exchange.sendResponseHeaders(500, -1); // Internal Server Error 
                    e.printStackTrace();
                }
            }
            }});

        server.setExecutor(null);
        server.start(); 
        System.out.println("Start server on 8002");

    }
}
