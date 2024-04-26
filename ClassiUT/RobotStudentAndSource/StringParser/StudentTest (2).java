package student.lucaBiancoUno;
import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ClassUnderTest.StringParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
/**
 * Test class with the aim of cover and test the line of codes and the behavior of the StringParser class
 * @author Luca Bianco
 * */
class StudentTest {
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
    /**
    * <p> This method trivially tests the default constructor of the class StringParser</p>
    * */
    @Test
    void testStringParser() {
        StringParser stringParser = new StringParser();
    }
    /**
    * <p>This method tests the method "buildString" using a black box methodology. In
    * particular, I used a Pair Wise Choice strategy but since this method has only two params
    * it becomes an All Combination Coverage.<br>
    * The equivalence classes are the following: <br>
    * str   ->  null    "prova \\u0021 "    "prova \'uno\'"     "prova \'uno\' \\u0021"     EMPTY_STRING    <br>
    * eatsep      ->  true    false <br>
    *
    * I used a Parametrized test with csv sources to automate the tests and avoid duplicated code.
    * </p>
    *
    * @param    proof  the string to test
    * @param    expected  the string that represents the expected output
    * @param    eatsep  a boolean value that indicates if the separator is to be taken in consideration
    * */
    @ParameterizedTest
    @CsvSource({"'','', false", "\"prova\\u0021\", prova!, true",
            "\"prova\\u0021\", \"prova!\", false",
            "'un\\'ora', 'un'ora', false",
            "'un\\'ora', un'ora, true",
            "'prova di un\\'ora\\u0021', 'prova di un'ora!', false",
            "'prova di un\\'ora\\u0021', prova di un'ora!, true"})
    void testBuildStringBlackBox(String proof, String expected, boolean eatsep) {
        assertEquals(expected, StringParser.buildString(proof, eatsep));
    }
    /**
     * <p>This test method tests the same method with the same methodology of the testBuildStringBlackBox method but it tests
     * the invalid inputs causing Exception.</p>
     * @param   str  the string to test
     * @param   eatsep  a boolean value that indicates if the separator is to be taken in consideration
     * */
    @ParameterizedTest
    @CsvSource({",true", ",false", "'',true"})
    void testBuildStringBlackBoxWithException(String  str, boolean eatsep) {
        assertThrows(Exception.class, () -> StringParser.buildString(str, eatsep));
    }
    /**
     * <p>This method tests the method "buildString" using a white box methodology. In particular, I used a Condition
     * Combination Coverage Criteria. The idea is a Bottom Up strategy, so we start from the last called method in the chain:
     * <i>readUnicodeChar.</i>  <br>
     *
     *
     * The following inputs cover all the condition combiantions for <i>readUnicodeChar</i> method and some trivially
     *condition combinations of read method (LoCs: 75 89 94): <br>
     * <ol>
     *     <li>prova\\uBb2n false</li>
     *     <li>prova\\uBb2/ false</li>
     * </ol>
     *
     * The following inputs cover all the remaining condition combinations for <i>read</i> method (LoCs: 76 80): <br>
     *
     * <ol>
     *     <li>prova \\u2 false</li>
     *     <li>prova\\u0021 false</li>
     *     <li>prova\\\\ false</li>
     *     <li>prova\\\ false</li>
     *     <li>pro\\ va true</li>
     * </ol>
     *
     * </p>
     * @param str
     * @param eatsep
     */
    @ParameterizedTest
    @CsvSource({"prova\\uBb2n, false", "prova\\uBb2/, false",
                "prova\\u2, false", "prova\\u0021, false", "prova\\\\, false", "prova\\\", false", " pro\\ va, true"})
    void testBuildStringWhiteBox(String str, boolean eatsep) {
        StringParser.buildString(str, eatsep);
    }
    private static Stream<Arguments> provideParamsForTestReadString() {
        return Stream.of(
                Arguments.of(new StringBuilder("\'This is a \"TEST\\u0021\", goodbye.\'"),
                             new StringBuilder(), 1, '\'', 33, "This is a \"TEST!\", goodbye.'"),       //1st
                Arguments.of(new StringBuilder("\'This is a \"TEST\\u0021\", goodbye.\'"),
                        new StringBuilder("pre-test "), 1, '\'', 33, "pre-test This is a \"TEST!\", goodbye.'"),    //2nd
                Arguments.of(new StringBuilder(), new StringBuilder(), 1, '\'', 1, ""), //4th
                Arguments.of(new StringBuilder("\'This is a \"TEST\\u0021\", goodbye.\'"),
                        new StringBuilder(), 35, '\'', 35, ""), //7th
                Arguments.of(new StringBuilder("\"This is a \'TEST\\u0021\', goodbye.\""),
                        new StringBuilder(), 1, '\"', 33, "This is a 'TEST!', goodbye.\"") //8th
        );
    }
    /**
     * <p>
     *     This test method use a Black Box strategy, in particular it uses a Base Choice Coverage criteria.<br>
     *     The equivalence classes are the following: <br>
     *     <ol>
     *         <il>DST ->  STRING_EMPTY    STRING_NOT_EMPTY    NULL</il>
     *         <il>SRC ->  STRING_NOT_EMPTY    STRING_EMPTY    NULL</il>
     *         <il>INDEX   ->  0<=INDEX<SRC.LENGTH     INDEX<0    INDEX>SRC.LENGTH</il>
     *         <il>SEP     ->  "   '   VUOTO</il>
     *     </ol>
     * </p>
     * @param src   the string to read
     * @param dst   the string where the result will be put
     * @param index the offset into the origin
     * @param sep   the separator chosen for the dst string
     * @param expectedOffset    expected output of the readString method
     * @param expectedDst   expected status of the dst string
     */
    @ParameterizedTest
    @MethodSource("provideParamsForTestReadString")
    void testReadString(StringBuilder src, StringBuilder dst, int index, char sep, int expectedOffset, String expectedDst) {
        int result = StringParser.readString(dst, src, index, sep);
        assertEquals(result, expectedOffset);
        assertEquals(dst.toString(), expectedDst);
    }
    private static Stream<Arguments> provideParamsForTestReadStringExceptions() {
        return Stream.of(
                Arguments.of(new StringBuilder("\'This is a \"TEST\\u0021\", goodbye.\'"),
                        null, 1, '\''),       //3rd
                Arguments.of(null, new StringBuilder(), 1, '\''),    //5th
                Arguments.of(new StringBuilder("\'This is a \"TEST\\u0021\", goodbye.\'"),
                            new StringBuilder(), -1, '\'') //6th
        );
    }
    /**
     * <p>
     *     Is the same of the testReadString method but this tests the invalid inputs causing Exception.
     * </p>
     * @param src   the string to read
     * @param dst   the string where the result will be put
     * @param index the offset into the origin
     * @param sep   the separator chosen for the dst string
     */
    @ParameterizedTest
    @MethodSource("provideParamsForTestReadStringExceptions")
    void testReadStringExceptions(final StringBuilder src, final StringBuilder dst, int index, char sep) {
        assertThrows(Exception.class, () -> StringParser.readString(dst, src, index, sep));
    }
    private static Stream<Arguments> provideStringsForEscapeStringBlackBoxFirstCombinationGroup() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", "  "),
                Arguments.of("This is a TEST", " This is a TEST "),
                Arguments.of("This is a TEST £", " This is a TEST \\u00a3 ")
        );
    }
    /**
     * This test method has the aim of test the method <i>escapeString</i> using a Black Box strategy,
     * in particular using an All Combinations Coverage, the equivalence classes are the following: <br>
     * <ol>
     *     <il>str     ->      NULL    STRING_EMPTY    STRING_WITHOUT_UCHARS   STRING_WITH_UCHARS</il>
     *     <il>delim   ->      EMPTY_CHAR  '   "</il>
     * </ol>
     * In particular this method tests the first combination group, where delim param is fixed at ' ' (space char)
     * while str and expected params varies
     *
     * @param str   string to test
     * @param expected  the expected output of <i>escapeString</i> method
     */
    @ParameterizedTest
    @MethodSource("provideStringsForEscapeStringBlackBoxFirstCombinationGroup")
    void testEscapeStringBlackBoxFirstCombinationGroup(String str, String expected) {
        char delim = ' ';
        String result;
        result = StringParser.escapeString(str, delim);
        assertEquals(result, expected);
    }

    private static Stream<Arguments> provideStringsForEscapeStringBlackBoxSecondCombinationGroup() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", "''"),
                Arguments.of("This is a TEST", "'This is a TEST'"),
                Arguments.of("This is a TEST £", "'This is a TEST \\u00a3'")
        );
    }
    /**
     * This test method has the aim of test the method <i>escapeString</i> using a Black Box strategy,
     * in particular using an All Combinations Coverage, the equivalence classes are the following: <br>
     * <ol>
     *     <il>str     ->      NULL    STRING_EMPTY    STRING_WITHOUT_UCHARS   STRING_WITH_UCHARS</il>
     *     <il>delim   ->      EMPTY_CHAR  '   "</il>
     * </ol>
     * In particular this method tests the second combination group, where delim param is fixed at ' (single apex char)
     * while str and expected params varies
     *
     * @param str   string to test
     * @param expected  the expected output of <i>escapeString</i> method
     */
    @ParameterizedTest
    @MethodSource("provideStringsForEscapeStringBlackBoxSecondCombinationGroup")
    void testEscapeStringBlackBoxSecondCombination(String str, String expected) {
        char delim = '\'';
        String result;
        result = StringParser.escapeString(str, delim);
        assertEquals(result, expected);
    }
    private static Stream<Arguments> provideStringsForEscapeStringBlackBoxThirdCombinationGroup() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", "\"\""),
                Arguments.of("This is a TEST", "\"This is a TEST\""),
                Arguments.of("This is a TEST £", "\"This is a TEST \\u00a3\"")
        );
    }
    /**
     * This test method has the aim of test the method <i>escapeString</i> using a Black Box strategy,
     * in particular using an All Combinations Coverage, the equivalence classes are the following: <br>
     * <ol>
     *     <il>str     ->      NULL    STRING_EMPTY    STRING_WITHOUT_UCHARS   STRING_WITH_UCHARS</il>
     *     <il>delim   ->      EMPTY_CHAR  '   "</il>
     * </ol>
     * In particular this method tests the third combination group, where delim param is fixed at " (double quote char)
     * while str and expected params varies
     *
     * @param str   string to test
     * @param expected  the expected output of <i>escapeString</i> method
     */
    @ParameterizedTest
    @MethodSource("provideStringsForEscapeStringBlackBoxThirdCombinationGroup")
    void testEscapeStringBlackBoxThirdCombination(String str, String expected) {
        char delim = '"';
        String result;
        result = StringParser.escapeString(str, delim);
        assertEquals(result, expected);
    }
    /**
     * This test method has the aim of test the escapeString method using a WhiteBox strategy, in particular
     * using a Node Coverage criteria.
     * With just one string it's possible to cover all the nodes, the string is built using all the characters that
     * the method uses creating different nodes.
     */
    @Test
    void testEscapeStringWhiteBox() {
        /*  Node Coverage  */
        char nullCharacter = Character.MIN_VALUE;
        char lessThenFirsrtASCII = 30;
        String str = "" + '\b' + '\b' + '\t' + '\n' + '\f' + '\r' + '\"' + '\'' + '\\' + lessThenFirsrtASCII + nullCharacter;
        String result = StringParser.escapeString(str, ' ');
        String expected = " \\b\\b\\t\\n\\f\\r\\\"\\'\\\\\\u001e ";
        assertEquals(result, expected);
    }

}
 