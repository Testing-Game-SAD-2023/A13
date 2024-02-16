import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.*;
import java.io.InputStreamReader;

public class generazione_livelli {

	public static void main(String[] args) {
		 Vector<String> Risultato = new Vector<String> ();
		 Vector<Integer> Return = new Vector<Integer> ();
		 Object[] chiavi = new Object[13];
		 int i,n;
		 n=1;
		  
		 String path = new File("livelli.txt").getAbsolutePath(); 
			        File file = new File(path);

			        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			            String line = reader.readLine();
			            n = Integer.parseInt(line);
			            System.out.println("Number read from file: " + n);
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
	    
		 HashMap<Integer,Double> coperture = new HashMap<Integer,Double>();
		 for(i=0;i<13;i++) {
		 String filePath = new File("I"+i+"/evosuite-report/statistics.csv").getAbsolutePath();
	     boolean isFirstLine = true;
	    

	


		System.out.println("funziona1" );
	     try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	            System.out.println("funziona2" );
		    String line;
	            while ((line = br.readLine()) != null) {
	                if (isFirstLine) {
	                    isFirstLine = false;
	                    continue;
	                }

	                String[] columns = line.split(",");
	                String copertura = columns[2];
	                Risultato.add(copertura);
	                
	                System.out.println("COPERTURA: " + copertura );
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	     int dim=Risultato.size();
	     coperture.put(i,Double.parseDouble(Risultato.elementAt(dim-1).toString()));
	     Risultato.clear();
		 }
		 for(Integer key : coperture.keySet()) {
		 		System.out.println(key+" "+coperture.get(key));
		   }
		 HashMap<Integer,Double> ordinata = ordinaHashMapPerValore(coperture);
		 System.out.println(" map ordinata"+ordinata);
		 chiavi= ordinata.keySet().toArray();
		 System.out.println(" elemento minore "+chiavi[0]);
		 //n=3;
		 if(n>1)
		 Return.add(1);
		 double spiaz=13/n;
		 System.out.println(" spiazzamento: "+spiaz);
		 if(n%2==0) {
			for(int r=(n-2)/2;r>0;r--) {
				Return.add(7-(int)(spiaz*r));
			}
			for(int r=1;r<(n-2)/2+1;r++) {
				Return.add(7+(int)(spiaz*r));
			}
		 }
		 else{
			 for(int r=(n-2)/2;r>0;r--) {
					Return.add(7-(int)(spiaz*r));
				}
			 Return.add(7);
				for(int r=1;r<(n-1)/2;r++) {
					Return.add(7+(int)(spiaz*r));
				}
			 
		 }
		 if(n>1)
		 Return.add(12);
		 for(int r=n;r<13;r++) {
				Return.add(13);
			}
		 System.out.println(" elemento da salvare "+Return);
		 ArrayList<Integer> vet_key = new ArrayList<>(ordinata.keySet());
		 		vet_key.add(13, -1);
		 		for (Integer k : vet_key) {
		            System.out.println("Chiave: " + k);
		        }
	            
	       
		 
	        try {
	        	String scriptPath = new File("misurazione_livelli.sh").getAbsolutePath();
	        	System.out.println(" percorso "+scriptPath);
	            ProcessBuilder processBuilder = new ProcessBuilder("bash", scriptPath, vet_key.get(Return.elementAt(0)).toString(), vet_key.get(Return.elementAt(1)).toString(),
	            vet_key.get(Return.elementAt(2)).toString(),vet_key.get(Return.elementAt(3)).toString(),vet_key.get(Return.elementAt(4)).toString(),vet_key.get(Return.elementAt(5)).toString(),vet_key.get(Return.elementAt(6)).toString(),
	            vet_key.get(Return.elementAt(7)).toString(),vet_key.get(Return.elementAt(8)).toString(),vet_key.get(Return.elementAt(9)).toString(),vet_key.get(Return.elementAt(10)).toString(),
	            vet_key.get(Return.elementAt(11)).toString(),vet_key.get(Return.elementAt(12)).toString());
			System.out.println(" invio: "+Return);
			Process process = processBuilder.start();

	    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            };
		// Leggi gli errori generati dal comando bash
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println(errorLine);
            };
	            int exitCode = process.waitFor();

	            if (exitCode == 0) {
	                System.out.println("Lo script e' stato eseguito correttamente.");
	            } else {
	                System.out.println("Si e' verificato un errore durante l'esecuzione dello script.");
	                System.out.println(" exit code: "+exitCode);
	                
	            }
	        } catch (IOException | InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	
	public static <K, V extends Comparable<? super V>> HashMap<K, V> ordinaHashMapPerValore(HashMap<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<K, V> risultato = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            risultato.put(entry.getKey(), entry.getValue());
        }

        return risultato;
	}}





