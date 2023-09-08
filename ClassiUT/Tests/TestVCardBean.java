import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NomeClasseTest {
	private static VCardBean cut;

	@BeforeAll
	static public void setup() {
		cut = new VCardBean();
	}

	@Test
	public void testSetVCard() {
		cut.setVCard("Test");;
		assertEquals(cut.getVCard(), "Test");
	}
}