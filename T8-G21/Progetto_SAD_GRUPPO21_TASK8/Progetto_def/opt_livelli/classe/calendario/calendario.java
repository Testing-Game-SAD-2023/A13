package calendario;

public class calendario {
	public static String calend(int d, String ms, int a)
	{

		int m=0;
		if (ms.equals("gennaio")) 
    		m=1;
		else if (ms.equals("febbraio")) 
            m=2;
		else if (ms.equals("marzo")) 
    		m=3;
		else if (ms.equals("aprile"))m=4;
		else if (ms.equals("maggio"))m=5;
		else if (ms.equals("giugno"))m=6;
		else if (ms.equals("luglio"))m=7;
		else if (ms.equals("agosto"))m=8;
		else if (ms.equals("settembre"))m=9;
		else if (ms.equals("ottobre")) m=10;
		else if (ms.equals("novembre"))m=11;
		else if (ms.equals("dicembre"))m=12; 

		if (d<1 || d>31 || m==0 || a<1582)
			return "Errore";
		if (m<10 && a==1582)
			return "Errore";
		if (d<15 && m==10 && a==1582)
			return "Errore";
		Boolean bisestile= (a%4==0);
		if (bisestile && a%100==0 && a%400!=0)
			bisestile=false;
		if ((m==2 && d>29)||(m==2 && d==29 && !bisestile))
			return "Errore";
		if ((m==4 || m==6 || m==9 || m==11) && d>30)
			return "Errore";

		
		if (m<=2)
		 {
		  m = m + 12;
		  a--;
		 };
		 int f1 = a / 4;
		 int f2 = a / 100;
		 int f3 = a / 400;
		 int f4 = (int) (2 * m + (.6 * (m + 1)));
		 int f5 = a + d + 1;
		 int x = f1 - f2 + f3 + f4 + f5;
		 int k = x / 7;
		 int n = x - k * 7;
		 
		 if (n==1) return "Lunedi";		 
		 else if (n==2) return "Martedi";
		 else if (n==3) return "Mercoledi";
		 else if (n==4) return "Giovedi";
		 else if (n==5) return "Venerdi";
		 else if (n==6) return "Sabato";
		 else if (n==0) return "Domenica";
		 else return "Errore";
	}

	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int giorno=30;
		String mese="aprile";
		int anno=2020;
		System.out.println(calend(giorno,mese,anno));
		
		
	}
*/


	
}