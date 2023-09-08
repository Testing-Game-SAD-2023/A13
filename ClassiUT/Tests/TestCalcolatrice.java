import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NomeClasseTest {
	private static Calcolatrice cut;

	@BeforeAll
	static public void setup() {
		cut = new Calcolatrice();
	}

	@Test
	public void testDivide() {
		int result = cut.divide(10, 2);
		assertEquals(5, result);
	}

	@Test
	public void testDivideByZero() {
		assertThrows(ArithmeticException.class, () -> {
			cut.divide(10, 0);
		});
	}
}