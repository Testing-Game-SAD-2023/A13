import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestVCardBean {

    private VCardBean vCardBean;

    @Before
    public void setUp() {
        vCardBean = new VCardBean();
    }

    @Test
    public void testSetAndGetFirstName() {
        vCardBean.setFirstName("John");
        assertEquals("John", vCardBean.getFirstName());
    }

    @Test
    public void testSetAndGetLastName() {
        vCardBean.setLastName("Doe");
        assertEquals("Doe", vCardBean.getLastName());
    }

    @Test
    public void testSetAndGetFormattedName() {
        vCardBean.setFormattedName("John Doe");
        assertEquals("John Doe", vCardBean.getFormattedName());
    }

    @Test
    public void testSetAndSetTitle() {
        vCardBean.setTitle("Manager");
        assertEquals("Manager", vCardBean.getTitle());
    }

    @Test
    public void testSetAndGetOrganization() {
        vCardBean.setOrganization("XYZ Company");
        assertEquals("XYZ Company", vCardBean.getOrganization());
    }

    @Test
    public void testSetAndGetEmail() {
        vCardBean.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", vCardBean.getEmail());
    }

    @Test
    public void testSetAndGetPhone() {
        vCardBean.setPhone("123-456-7890");
        assertEquals("123-456-7890", vCardBean.getPhone());
    }

    @Test
    public void testSetAndGetFax() {
        vCardBean.setFax("987-654-3210");
        assertEquals("987-654-3210", vCardBean.getFax());
    }

    @Test
    public void testGetVCardWithoutData() {
        assertNull(vCardBean.getVCard());
    }

    @Test
    public void testGetVCardWithData() {
        vCardBean.setFirstName("John");
        vCardBean.setLastName("Doe");
        vCardBean.setOrganization("XYZ Company");
        vCardBean.setEmail("john.doe@example.com");
        assertNotNull(vCardBean.getVCard());
    }
}