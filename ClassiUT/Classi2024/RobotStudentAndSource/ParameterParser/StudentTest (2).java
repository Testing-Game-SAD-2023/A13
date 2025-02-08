package student.lorenzoDestriereDue;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.ParameterParser;

/**
 * Questa classe rappresenta una suite di test per verificare il corretto funzionamento della classe ParameterParser.
 * <p>
 * <br>
 * La classe ParameterParser offre metodi per analizzare e estrarre coppie di nome/valore da una stringa o un array di caratteri,
 * fornendo opzioni come la conversione dei nomi a lettere minuscole e la gestione di token quotati.
 * <br>
 * Questa suite di test si propone di esplorare e validare le funzionalità della ParameterParser attraverso
 * una copertura del codice completa e approfondita, assicurando la corretta gestione di casi come token quotati,
 * separatori multipli e conversione dei nomi a lettere minuscole.
 * <p>
 * Alcuni dettagli della classe, come le implementazioni interne di parseToken e getToken,
 * sono coperti indirettamente attraverso i test dei metodi pubblici che ne fanno uso.
 * <p>
 * <br>
 *
 * @see ClassUnderTest.ParameterParser
 * @author Destriere Lorenzo (N97000448)
 */

class StudentTest {
	
	private ParameterParser parser;
	private Map<String, String> expectedMap;
	private Map parsedMap;

	/**
	 * Questo metodo viene eseguito prima di ciascun test (@Test) nella classe, preparando l'ambiente
	 * per l'esecuzione del test corrente. Inizializza una nuova istanza di ParameterParser
	 * e altre variabili di supporto necessarie per il test.
	 *
	 * @see ClassUnderTest.ParameterParser#ParameterParser()
	 * @see Test
	 */
	@BeforeEach
	void setUp() throws Exception {
		parser = new ParameterParser();
		expectedMap = new HashMap();
		expectedMap.put("key1", "value1");
		expectedMap.put("key2", "value2");
	}

	/**
	 * Questo metodo viene eseguito dopo ciascun test (@Test) nella classe, svolgendo le operazioni
	 * necessarie per ripristinare lo stato dell'ambiente dopo l'esecuzione del test corrente.
	 * In questo caso, imposta le variabili di istanza `parser`, `expectedMap` e `parsedMap` a null,
	 * liberando così eventuali risorse e consentendo la loro raccolta da parte del garbage collector.
	 * Questa operazione è fondamentale per garantire l'isolamento e la pulizia dell'ambiente
	 * tra i diversi test, evitando possibili interferenze tra esecuzioni di test consecutivi.
	 *
	 * @see Test
	 */
	@AfterEach
	void tearDown() throws Exception {
		parser = null;
		expectedMap = null;
		parsedMap = null;
	}

	/**
	 * Questo metodo di test verifica il corretto comportamento del metodo parse() della classe ParameterParser
	 * quando l'input è un array di caratteri nullo. Viene inizializzato un array di caratteri (input) a null
	 * e successivamente chiamato il metodo parse() con i parametri specificati (input, offset, length, separator).
	 * Infine, viene eseguito quanto segue:
	 * 
	 * <ul>
	 * 	<li>Utilizza assertTrue per verificare che la mappa risultante (parsedMap) sia vuota, poiché non è possibile
	 *   effettuare il parsing di un array di caratteri nullo.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#parse(char[], int, int, char)
	 * @see assertTrue
	 */
	@Test
	void testParseNullCharArrayIntIntChar() {
		char[] input = null;
		int offset = 0;
		int length = 0;
		char separator = ';';
		parsedMap = parser.parse(input, offset, length, separator);
		
		assertTrue(parsedMap.isEmpty());
	}

	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con un array di caratteri, un offset e una lunghezza entrambi impostati a zero. Viene
	 * inizializzato un array di caratteri (input) con coppie chiave-valore ("key1=value1", "key2=value2") e
	 * successivamente chiamato il metodo parse() con i parametri specificati (input, offset, length, separator).
	 * Infine, viene eseguito quanto segue:
	 * 
	 * <ul>
	 * 	<li>Utilizza assertTrue per verificare che la mappa risultante (parsedMap) sia vuota, poiché la lunghezza
	 *   zero indica che non ci sono elementi da analizzare nell'input.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#parse(char[], int, int, char)
	 * @see assertTrue
	 */
	@Test
	void testParseCharArrayZeroOffsetZeroLengthChar() {
		char[] input = {'k', 'e', 'y', '1', '=', 'v', 'a', 'l', 'u', 'e', '1', 'k', 'e', 'y', '2', '=', 'v', 'a', 'l', 'u', 'e', '2'};
		int offset = 0;
		int length = 0;
		char separator = ';';
		parsedMap = parser.parse(input, offset, length, separator);
		
		assertTrue(parsedMap.isEmpty());
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con un array di caratteri, un offset pari a zero e una lunghezza pari alla lunghezza
	 * completa dell'array. Viene inizializzato un array di caratteri (input) con coppie chiave-valore ("key1value1",
	 * "key2value2") in cui il separatore è rappresentato dal carattere ';'. Successivamente, viene chiamato il
	 * metodo parse() con i parametri specificati (input, offset, length, separator). Infine, viene eseguito quanto segue:
	 * 
	 * <ul>
	 * 	<li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) sia uguale alla mappa attesa
	 *   (mapExpected). In questo caso, la mappa attesa contiene le coppie ("key1value1", null) e ("key2value2", null),
	 *   poiché l'uguake è assente tra le coppie chiave-valore nell'input.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#parse(char[], int, int, char)
	 * @see assertTrue
	 */
	@Test
	void testParseNotEqualCharArrayZeroOffsetFullLengthChar() {
		char[] input = {'k', 'e', 'y', '1', 'v', 'a', 'l', 'u', 'e', '1', ';', 'k', 'e', 'y', '2', 'v', 'a', 'l', 'u', 'e', '2'};
		int offset = 0;
		int length = input.length;
		char separator = ';';
		parsedMap = parser.parse(input, offset, length, separator);
		
		Map<String, String> mapExpected = new HashMap<>();
		mapExpected.put("key1value1", null);
		mapExpected.put("key2value2", null);
		assertEquals(mapExpected, parsedMap);
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con un array di caratteri contenente coppie chiave-valore e un separatore che non è presente
	 * tra le coppie chiave-valore. Viene inizializzato un array di caratteri (input) con coppie chiave-valore
	 * ("key1=value1", "key2=value2") in cui il separatore è rappresentato dal carattere ';'. Successivamente,
	 * viene chiamato il metodo parse() con i parametri specificati (input, offset, length, separator). Infine,
	 * viene eseguito quanto segue:
 	 *
	 * <ul>
	 * 	<li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) sia uguale alla mappa attesa
 	 *   (mapExpected). In questo caso, la mappa attesa contiene una singola coppia ("key1", "value1key2=value2"),
 	 *   poiché il separatore ';' è assente tra le coppie chiave-valore nell'input.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#parse(char[], int, int, char)
	 * @see assertTrue
	 */
	@Test
	void testParseNotSeparatorCharArrayZeroOffsetFullLengthChar() {
		char[] input = {'k', 'e', 'y', '1', '=', 'v', 'a', 'l', 'u', 'e', '1', 'k', 'e', 'y', '2', '=', 'v', 'a', 'l', 'u', 'e', '2'};
		int offset = 0;
		int length = input.length;
		char separator = ';';
		parsedMap = parser.parse(input, offset, length, separator);
		
		Map<String, String> mapExpected = new HashMap<>();
		mapExpected.put("key1", "value1key2=value2");
		assertEquals(mapExpected, parsedMap);
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con un array di caratteri contenente coppie chiave-valore e un separatore tra le coppie.
	 * Viene inizializzato un array di caratteri (input) con coppie chiave-valore ("key1=value1", "key2=value2")
	 * in cui il separatore è rappresentato dal carattere ';'. Successivamente, viene chiamato il metodo parse() con
	 * i parametri specificati (input, offset, length, separator). Infine, viene eseguito quanto segue:
	 *
	 * <ul>
	 * 	<li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) abbia la stessa dimensione della
	 *   mappa attesa (expectedMap). In questo caso, la mappa attesa contiene due coppie chiave-valore.</li>
	 *   <li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) sia uguale alla mappa attesa
	 *   (expectedMap). In questo modo, si verifica che la classe ParameterParser analizzi correttamente le coppie
	 *   chiave-valore e le inserisca nella mappa risultante.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#parse(char[], int, int, char)
	 * @see assertTrue
	 */
	@Test
	void testParseCharArrayZeroOffsetFullLengthChar() {
		char[] input = {'k', 'e', 'y', '1', '=', 'v', 'a', 'l', 'u', 'e', '1', ';', 'k', 'e', 'y', '2', '=', 'v', 'a', 'l', 'u', 'e', '2'};
		int offset = 0;
		int length = input.length;
		char separator = ';';
		parsedMap = parser.parse(input, offset, length, separator);
		
		assertEquals(expectedMap.size(), parsedMap.size());
		assertEquals(expectedMap, parsedMap);
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando la modalità di
	 * lettere minuscole è abilitata utilizzando il metodo setLowerCaseNames(true). Viene inizializzato un array
	 * di caratteri (input) con una coppia chiave-valore ("KEY1=value1") in cui il separatore è rappresentato
	 * dal carattere ';'. Successivamente, la modalità lettere minuscole viene abilitata utilizzando il metodo
	 * setLowerCaseNames(true). Viene poi chiamato il metodo isLowerCaseNames() per assicurarsi che la modalità
	 * sia stata attivata con successo. Infine, viene chiamato il metodo parse() con i parametri specificati
	 * (input, offset, length, separator) e viene eseguito quanto segue:
	 *
	 * <ul>
	 * 	<li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) abbia la stessa dimensione della
	 *   mappa attesa (mapExpected). In questo caso, la mappa attesa contiene una singola coppia ("key1", "value1"),
	 *   poiché la modalità lettere minuscole è attiva e le chiavi sono state convertite di conseguenza.</li>
	 *   <li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) sia uguale alla mappa attesa
	 *   (mapExpected). In questo modo, si verifica che la classe ParameterParser converta correttamente le chiavi
	 *   in minuscolo quando la modalità lettere minuscole è attivata.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#setLowerCaseNames(boolean)
	 * @see ClassUnderTest.ParameterParser#isLowerCaseNames()
	 * @see ClassUnderTest.ParameterParser#parse(char[], int, int, char)
	 * @see assertTrue
	 */
	@Test
	void testIsLowerCaseNames() {
		char[] input = {'K', 'E', 'Y', '1', '=', 'v', 'a', 'l', 'u', 'e', '1'};
		int offset = 0;
		int length = input.length;
		char separator = ';';
		parser.setLowerCaseNames(true);
		assertTrue(parser.isLowerCaseNames());
		
		parsedMap = parser.parse(input, offset, length, separator);
		
		Map<String, String> mapExpected = new HashMap<>();
		mapExpected.put("key1", "value1");
		assertEquals(mapExpected.size(), parsedMap.size());
		assertEquals(mapExpected, parsedMap);
	}
	
	//@Test
	//void testParseOneCharArrayZeroOffsetFullLengthChar() {
	//	//l'ultimo char == separator
	//	char[] input = {';'};
	//	int offset = 0;
	//	int length = input.length;
	//	char separator = ';';
	//	parsedMap = parser.parse(input, offset, length, separator);
		
	//	assertEquals(expectedMap.size(), parsedMap.size());
	//	assertEquals(expectedMap, parsedMap);
	//}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con un array di caratteri nullo e un carattere separatore. Viene inizializzato un array
	 * di caratteri (input) come nullo e un carattere separatore rappresentato dal carattere ';'. Successivamente,
	 * viene chiamato il metodo parse() con i parametri specificati (input, separator) e viene eseguito quanto segue:
	 *
	 * <ul>
	 * 	<li>Utilizza assertTrue per verificare che la mappa risultante (parsedMap) sia vuota. In questo caso, la
	 *   mappa dovrebbe essere vuota poiché l'input è nullo.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#parse(char[], char)
	 * @see assertTrue
	 */
	@Test
	void testParseNullCharArrayChar() {
		char[] input = null;
		char separator = ';';
		parsedMap = parser.parse(input, separator);
		assertTrue(parsedMap.isEmpty());
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con un array di caratteri contenente coppie chiave-valore e un carattere separatore tra
	 * le coppie. Viene inizializzato un array di caratteri (input) con coppie chiave-valore ("key1=value1", "key2=value2")
	 * in cui il separatore è rappresentato dal carattere ';'. Successivamente, viene chiamato il metodo parse() con
	 * i parametri specificati (input, separator) e viene eseguito quanto segue:
	 *
	 * <ul>
	 * 	<li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) abbia la stessa dimensione della
	 *   mappa attesa (expectedMap). In questo caso, la mappa attesa contiene due coppie chiave-valore.</li>
	 *  <li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) sia uguale alla mappa attesa
	 *   (expectedMap). In questo modo, si verifica che la classe ParameterParser analizzi correttamente le coppie
	 *   chiave-valore e le inserisca nella mappa risultante.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#parse(char[], char)
	 * @see assertTrue
	 */
	@Test
	void testParseCharArrayChar() {
		char[] input = {'k', 'e', 'y', '1', '=', 'v', 'a', 'l', 'u', 'e', '1', ';', 'k', 'e', 'y', '2', '=', 'v', 'a', 'l', 'u', 'e', '2'};
		char separator = ';';
		parsedMap = parser.parse(input, separator);
		assertEquals(expectedMap.size(), parsedMap.size());
		assertEquals(expectedMap, parsedMap);
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con una stringa nulla e un carattere separatore. Viene inizializzata una stringa (input) come
	 * nullo e un carattere separatore rappresentato dal carattere ';'. Successivamente, viene chiamato il metodo parse()
	 * con i parametri specificati (input, separator) e viene eseguito quanto segue:
	 *
	 * <ul>
	 * 	<li>Utilizza assertTrue per verificare che la mappa risultante (parsedMap) sia vuota. In questo caso, la mappa
	 *   dovrebbe essere vuota poiché l'input è nullo.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#parse(String, char)
	 * @see assertTrue
	 */
	@Test
	void testParseNullStringChar() {
		String input = null;
		char separator = ';';
		parsedMap = parser.parse(input, separator);
		assertTrue(parsedMap.isEmpty());
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con una stringa contenente coppie chiave-valore e un carattere separatore tra le coppie.
	 * Viene inizializzata una stringa (input) con coppie chiave-valore ("key1=value1", "key2=value2") in cui
	 * il separatore è rappresentato dal carattere ';'. Successivamente, viene chiamato il metodo parse() con i
	 * parametri specificati (input, separator) e viene eseguito quanto segue:
	 *   
	 * <ul>
	 * 	<li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) abbia la stessa dimensione della
	 *   mappa attesa (expectedMap). In questo caso, la mappa attesa contiene due coppie chiave-valore.</li>
	 *  <li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) sia uguale alla mappa attesa
	 *   (expectedMap). In questo modo, si verifica che la classe ParameterParser analizzi correttamente le coppie
	 *   chiave-valore dalla stringa e le inserisca nella mappa risultante.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#parse(String, char)
	 * @see assertEquals
	 */
	@Test
	void testParseStringChar() {
		String input = "key1=value1;key2=value2";
		char separator = ';';
		parsedMap = parser.parse(input, separator);
		assertEquals(expectedMap.size(), parsedMap.size());
		assertEquals(expectedMap, parsedMap);
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con una stringa e un array di caratteri entrambi nulli. Viene chiamato il metodo parse()
	 * con i parametri specificati (null, null) e viene eseguito quanto segue:
	 *
	 * <ul>
	 * 	<li>Utilizza assertTrue per verificare che la mappa risultante (parsedMap) sia vuota. In questo caso, la mappa
	 *   dovrebbe essere vuota poiché sia la stringa che l'array di caratteri sono nulli.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#parse(String, char[])
	 * @see assertTrue
	 */
	@Test
	void testParseNullStringNullCharArray() {
		parsedMap = parser.parse(null, null);
        assertTrue(parsedMap.isEmpty());
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con una stringa nulla e un array di caratteri separatore vuoto. Viene chiamato il metodo
	 * parse() con i parametri specificati (null, array di caratteri vuoto) e viene eseguito quanto segue:
	 *
	 * <ul>
	 * 	<li>Utilizza assertTrue per verificare che la mappa risultante (parsedMap) sia vuota. In questo caso, la mappa
	 *   dovrebbe essere vuota poiché la stringa di input è nulla e l'array di caratteri separatore è vuoto.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#parse(String, char[])
	 * @see assertTrue
	 */
	@Test
	void testParseNullStringEmptyCharArray() {
		char[] separators = {};
		parsedMap = parser.parse(null, separators);
        assertTrue(parsedMap.isEmpty());
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con una stringa nulla e un array di caratteri separatore. Viene chiamato il metodo parse()
	 * con i parametri specificati (null, array di caratteri separatore) e viene eseguito quanto segue:
	 *
	 * <ul>
	 * 	<li>Utilizza assertTrue per verificare che la mappa risultante (parsedMap) sia vuota. In questo caso, la mappa
	 *   dovrebbe essere vuota poiché la stringa di input è nulla.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#parse(String, char[])
	 * @see assertTrue
	 */
	@Test
	void testParseNullStringCharArray() {
		char[] separators = {';'};
		parsedMap = parser.parse(null, separators);
		
		assertTrue(parsedMap.isEmpty());
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con una stringa contenente coppie chiave-valore e un array di caratteri separatore.
	 * Viene inizializzata una stringa (input) con coppie chiave-valore ("key1=value1", "key2=value2") in cui
	 * il separatore è rappresentato dal carattere ';'. Successivamente, viene chiamato il metodo parse() con i
	 * parametri specificati (input, separators) e viene eseguito quanto segue:
	 * 
	 * <ul>
	 * 	<li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) abbia la stessa dimensione della
	 *   mappa attesa (expectedMap). In questo caso, la mappa attesa contiene due coppie chiave-valore.</li>
	 *  <li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) sia uguale alla mappa attesa
	 *   (expectedMap). In questo modo, si verifica che la classe ParameterParser analizzi correttamente le coppie
	 *   chiave-valore dalla stringa e le inserisca nella mappa risultante.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser#parse(String, char[])
	 * @see assertEquals
	 */
	@Test
	void testParseStringPresentCharArray() {
		String input = "key1=value1;key2=value2";
		char[] separators = {';'};
		parsedMap = parser.parse(input, separators);
		
		assertEquals(expectedMap.size(), parsedMap.size());
        assertEquals(expectedMap, parsedMap);
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con una stringa contenente coppie chiave-valore e un array di caratteri separatore contenente
	 * più di un carattere. Viene inizializzata una stringa (input) con coppie chiave-valore ("key1=value1", "key2=value2")
	 * in cui i separatori sono rappresentati dai caratteri ';', ','. Successivamente, viene chiamato il metodo parse() con
	 * i parametri specificati (input, separators) e viene eseguito quanto segue:
	 *
	 * <ul>
	 * 	<li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) abbia la stessa dimensione della
	 *   mappa attesa (expectedMap). In questo caso, la mappa attesa contiene due coppie chiave-valore.</li>
	 *  <li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) sia uguale alla mappa attesa
	 *   (expectedMap). In questo modo, si verifica che la classe ParameterParser analizzi correttamente le coppie
	 *   chiave-valore dalla stringa e le inserisca nella mappa risultante.</li>
	 *  <li>Creazione di un secondo input (input2) con lo stesso formato di coppie chiave-valore e separatori diversi,
	 *   poi esegue il parsing di questo secondo input e confronta la mappa risultante (parsedMap2) con la prima
	 *   mappa risultante (parsedMap). L'obiettivo è verificare che la classe ParameterParser gestisca correttamente
	 *   input con diversi formati di separatori.</li>
	 * </ul>
	 * 
	 * @see ClassUnderTest.ParameterParser#parse(String, char[])
	 * @see assertEquals
	 */
	@Test
	void testParseStringMultiCharArray() {
		String input = "key1=value1;key2=value2";
		char[] separators = {';', ','};
		parsedMap = parser.parse(input, separators);
		
		String input2 = "key1=value1,key2=value2";
		Map parsedMap2 = parser.parse(input2, separators);
		
		assertEquals(parsedMap2.size(), parsedMap.size());
        assertEquals(parsedMap2, parsedMap);
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con una stringa contenente coppie chiave-valore e spazi bianchi tra i caratteri. Viene
	 * inizializzata una stringa (input) con coppie chiave-valore ("key1=value1", "key2=value2") in cui sono
	 * presenti spazi bianchi tra i caratteri. Successivamente, viene chiamato il metodo parse() con i parametri
	 * specificati (input, offset, length, separator) e viene eseguito quanto segue:
	 *
	 * <ul>
	 * 	<li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) abbia la stessa dimensione della
	 *   mappa attesa (expectedMap). In questo caso, la mappa attesa contiene due coppie chiave-valore.</li>
	 *  <li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) sia uguale alla mappa attesa
	 *   (expectedMap). In questo modo, si verifica che la classe ParameterParser analizzi correttamente le coppie
	 *   chiave-valore dalla stringa e le inserisca nella mappa risultante, nonostante la presenza di spazi bianchi.</li>
	 * </ul>
	 * 
	 * @see ClassUnderTest.ParameterParser
	 * @see assertEquals
	 */
	@Test
	void testWhiteSpaceOfGetToken() {
		char[] input = {'k', 'e', 'y', '1', ' ', '=', ' ','v', 'a', 'l', 'u', 'e', '1', ';', 'k', 'e', 'y', '2', '=', 'v', 'a', 'l', 'u', 'e', '2'};
		int offset = 0;
		int length = input.length;
		char separator = ';';
		parsedMap = parser.parse(input, offset, length, separator);
		
		assertEquals(expectedMap.size(), parsedMap.size());
		assertEquals(expectedMap, parsedMap);
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con una stringa contenente coppie chiave-valore, alcune delle quali sono racchiuse tra virgolette.
	 * Viene inizializzata una stringa (inputString) con coppie chiave-valore ("key1=\"value1\";key2=value2") in cui
	 * "value1" è racchiuso tra virgolette. Successivamente, viene chiamato il metodo parse() con i parametri specificati
	 * (input, offset, length, separator) e viene eseguito quanto segue:
	 *
	 * <ul>
	 * 	<li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) abbia la stessa dimensione della
	 *   mappa attesa (expectedMap). In questo caso, la mappa attesa contiene due coppie chiave-valore.</li>
	 *  <li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) sia uguale alla mappa attesa
	 *   (expectedMap). In questo modo, si verifica che la classe ParameterParser analizzi correttamente le coppie
	 *   chiave-valore dalla stringa, gestendo correttamente i valori racchiusi tra virgolette.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser
	 * @see assertEquals
	 */
	@Test
	void testQuoteOfParseQuotedToken() {
		String inputString = "key1=\"value1\";key2=value2";
		
		char[] input = inputString.toCharArray();
		int offset = 0;
		int length = input.length;
		char separator = ';';
		parsedMap = parser.parse(input, offset, length, separator);
		
		assertEquals(expectedMap.size(), parsedMap.size());
		assertEquals(expectedMap, parsedMap);
	}
	
	/**
	 * Questo metodo di test verifica il comportamento della classe ParameterParser quando viene chiamato il
	 * metodo parse() con una stringa contenente coppie chiave-valore, alcune delle quali sono racchiuse tra virgolette
	 * e contengono doppi backslash. Viene inizializzata una stringa (inputString) con coppie chiave-valore
	 * ("key1=\\\"value1\\\";key2=value2") in cui "value1" è racchiuso tra virgolette e contiene doppi backslash.
	 * Successivamente, viene chiamato il metodo parse() con i parametri specificati (input, offset, length, separator)
	 * e viene eseguito quanto segue:
	 * 
	 * <ul>
	 * 	<li>Utilizza assertEquals per verificare che la mappa risultante (parsedMap) abbia la stessa dimensione della
	 *   mappa attesa (expectedMap). In questo caso, la mappa attesa contiene due coppie chiave-valore.</li>
	 *  <li>Utilizza assertNotEquals per verificare che la mappa risultante (parsedMap) non sia uguale alla mappa attesa.
	 *   In questo modo, si verifica che la classe ParameterParser gestisca correttamente i doppi backslash all'interno
	 *   dei valori racchiusi tra virgolette, evitando la creazione di un valore con virgolette in eccesso.</li>
	 * </ul>
	 *
	 * @see ClassUnderTest.ParameterParser
	 * @see assertEquals
	 * @see assertNotEquals
	 */
	@Test
	void testDoubleBackSlashOfParseQuotedToken() {
		String inputString = "key1=\\\"value1\\\";key2=value2";
		
		char[] input = inputString.toCharArray();
		int offset = 0;
		int length = input.length;
		char separator = ';';
		parsedMap = parser.parse(input, offset, length, separator);
		
		assertEquals(expectedMap.size(), parsedMap.size());
		assertNotEquals(expectedMap, parsedMap);
	}
	
}
