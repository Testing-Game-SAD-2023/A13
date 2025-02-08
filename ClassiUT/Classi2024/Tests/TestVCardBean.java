import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestVCardBean {
    private static VCardBean cut;

    @BeforeClass
    public static void setup() {
        cut = new VCardBean();
    }

    @Test
    public void testSetVCard() {
        cut.setVCard("Test");
        assertEquals("Test", cut.getVCard());
    }
}