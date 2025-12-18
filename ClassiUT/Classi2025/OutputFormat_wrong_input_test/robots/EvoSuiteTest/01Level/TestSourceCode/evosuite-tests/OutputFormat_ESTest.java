import org.junit.Test;
import static org.junit.Assert.*;

public class OutputFormat_ESTest {

    @Test
    public void testCreatePrettyPrint() {
        OutputFormat f = OutputFormat.createPrettyPrint();
        assertEquals(2, f.getIndent().length());
        assertTrue(f.isNewlines());
        assertTrue(f.isTrimText());
    }

    @Test
    public void testSetEncoding() {
        OutputFormat f = new OutputFormat();
        f.setEncoding("ISO-8859-1");
        assertEquals("ISO-8859-1", f.getEncoding());
    }

}
