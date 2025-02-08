package student.davideMancinelliUno;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ClassUnderTest.StringParser;

class StudentTest {

	static int i = 0;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testStringParser() {
		StringParser Parsero = new StringParser();
	}

	
	@Test
	void testBuildString() {
		String input1 = "Non so cosa scrivere\\'\\'";
		String prova1string = StringParser.buildString(input1, false);
		System.out.println(prova1string);
		assertEquals(prova1string, "Non so cosa scrivere''");
		String prova2string = StringParser.buildString(input1, true);
		System.out.println(prova2string);
		
		
		String input2 = "\nAdesso . \n so cosa scrivere";
		String prova3string = StringParser.buildString(input2, true);
		System.out.println(prova3string);
		
		String input4 = "\nAdesso . \n so cosa scrivere";
		String prova5string = StringParser.buildString(input4, false);
		System.out.println(prova5string);
		
		String input3 = "Adesso . \n so cosa scrivere";
		String prova4string = StringParser.buildString(input3, false);
		System.out.println(prova4string);

	}

	
	@Test
	void testReadString() {
		StringBuilder risultato1 = new StringBuilder();
		String input1 = "Qualcosa da @dire e pensare";
		int offset1 = StringParser.readString(risultato1, input1, 0, '@');
		System.out.println("Offset1:" + offset1);
		assertEquals(offset1, 12);
		
		String input2 = "Uomini forti, destini forti, uomini deboli, destini deboli@";
		int offset2 = StringParser.readString(risultato1, input2, 0, '@');
		System.out.println("Offset2:" +offset2);
	
		String input3 = "Sequenza unicode: \\\\u0041 e altro.";
		int offset3 = StringParser.readString(risultato1, input3, 0, '\\');
		System.out.println("Offset3:" +offset3);
		
		String input4 = "Sequenza unicode: \\n e altro.";
		int offset4 = StringParser.readString(risultato1, input4, 0, '@');
		System.out.println("Offset4:" +offset4);
		
		String input5 = "Qalcosa in latino \\u00a4";
		int offset5 = StringParser.readString(risultato1, input5, 0, 'u');
		System.out.println("Offset5:" +offset5);
		
		String input6 = "Qalcosa in latino \\uA0C4";
		int offset6 = StringParser.readString(risultato1, input6, 0, 'u');
		System.out.println("Offset6:" +offset6);
		
		String input7 = "Qalcosa in latino \\u....";
		int offset7 = StringParser.readString(risultato1, input7, 0, 'u');
		System.out.println("Offset7:" +offset7);
		
		String input8 = "Qalcosa in latino \\u00+4";
		int offset8 = StringParser.readString(risultato1, input8, 0, 'u');
		System.out.println("Offset8:" +offset8);
		assertEquals(offset8, 24);
		
		String input9 = "Qalcosa in latino \\u^^^^";
		int offset9 = StringParser.readString(risultato1, input9, 0, 'u');
		System.out.println("Offset9:" +offset9);
		
		String input10 = "Qalcosa in latino \\uzzzz";
		int offset10 = StringParser.readString(risultato1, input10, 0, 'u');
		System.out.println("Offset10:" +offset10);
		
		String input11 = "\'";
		int offset11 = StringParser.readString(risultato1, input11, 0, 'u');
		System.out.println("Offset11:" +offset11);
		
	}

	
	@ParameterizedTest
	@ValueSource(strings = {"", "\b", "\t", "\n", "\f", "\n", "\f", "\r", "\r", "\"", "\'", 
							"\\", "\b0\n\t", "ééé", "\0napoli", "é23", "→μ", "μ"})	
	void testEscapeString(String candidate) {
		
		String prova1 = StringParser.escapeString(candidate, '|');
		System.out.println(prova1);
		
		// Creato il valore globale i per far si che nul venga stampato una sola volta
		// poichè quando viene messo come parametro in ValueSource viene dato un errore
		if(i==0){						
			String input2 = null;
			String prova2 = StringParser.escapeString(input2, '|');
			System.out.println(prova2);
			i++;
		}
	}
	
	

}
