import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestCalcolatrice {
    private static Calcolatrice cut;

    @BeforeClass
    public static void setup() {
        cut = new Calcolatrice();
    }

    @Test
    public void testDivide() {
        int result = cut.divide(10, 2);
        assertEquals(5, result);
    }

   /* @Test(expected = ArithmeticException.class)
    public void testDivideByZero() {
        cut.divide(10, 0);
    }*/
}