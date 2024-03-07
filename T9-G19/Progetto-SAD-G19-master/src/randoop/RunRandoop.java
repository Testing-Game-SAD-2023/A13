package randoop;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.lang.ProcessBuilder;



public class RunRandoop {
	static String zip_ritorno;
    static String xml_ritorno;
    static String cartella_ritorno;
    static Random r;
    static int low = 0;
    static int high = 230000;
    
	public static String[] Run( File class_file,int timelimit, int vecchioiter, int iter) {
		String name = (String) class_file.getName().subSequence(0,class_file.getName().length()-5 );

        zip_ritorno = null;
        xml_ritorno = null;
        cartella_ritorno = null;

        r = new Random();

        int seed = r.nextInt(high-low) + low;
        
        
		try {

			zip_ritorno = ("./FolderTree/"+name+"/RobotTest/RandoopTest/"+name+"-"+iter+"-dati_di_copertura/"+name+"_Test.zip");
			cartella_ritorno = ("./FolderTree/"+name+"/RobotTest/RandoopTest/"+name+"-"+iter+"-dati_di_copertura/"+name+"_Test");
			
			xml_ritorno = ("./FolderTree/"+name+"/RobotTest/RandoopTest/"+name+"-"+iter+"-dati_di_copertura/coveragetot.xml");
			
			File f = new File(zip_ritorno);

			ProcessBuilder processBuilder = new ProcessBuilder();

			processBuilder.command("bash", "robot.sh", name, String.valueOf(timelimit), String.valueOf(iter), String.valueOf(seed), String.valueOf(vecchioiter));

			Process p= processBuilder.start();
			
			while(!f.exists())
				Thread.sleep(100);
			
			Thread.sleep(3000);

			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	
		}

		String[] ritorno = {zip_ritorno, xml_ritorno, cartella_ritorno};

		return ritorno;

	}
}
