package student.simoneMontellaDue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Constructor;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ClassUnderTest.FTPFile;

class StudentTest {

	private static FTPFile ftpFile, ftpFileRawListing;
	private static String rawListingExample;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		ftpFile = new FTPFile();
		rawListingExample = "test listing";

		Constructor<FTPFile> stringConstructor = FTPFile.class.getDeclaredConstructor(String.class);
		stringConstructor.setAccessible(true);

		ftpFileRawListing = stringConstructor.newInstance(rawListingExample);
	}

	@Test
	void testFTPFile() {
		assertNotNull(ftpFile);
	}

	@Test
	void testFTPFileString() {
		assertNotNull(ftpFileRawListing);
	}

	@Test
	void testGetRawListing() {
		ftpFile.setRawListing(rawListingExample);
		assertEquals(rawListingExample, ftpFile.getRawListing());
	}

	@Test
	void testIsDirectory() {
		ftpFile.setType(FTPFile.DIRECTORY_TYPE);
		assertTrue(ftpFile.isDirectory());
	}

	@Test
	void testIsFile() {
		ftpFile.setType(FTPFile.FILE_TYPE);
		assertTrue(ftpFile.isFile());
	}

	@Test
	void testIsSymbolicLink() {
		ftpFile.setType(FTPFile.SYMBOLIC_LINK_TYPE);
		assertTrue(ftpFile.isSymbolicLink());
	}

	@Test
	void testIsUnknown() {
		ftpFile.setType(FTPFile.UNKNOWN_TYPE);
		assertTrue(ftpFile.isUnknown());
	}

	@Test
	void testIsValid() {
		assertTrue(ftpFile.isValid());
	}

	@Test
	void testGetAndSetType() {
		ftpFile.setType(FTPFile.FILE_TYPE);
		assertEquals(FTPFile.FILE_TYPE, ftpFile.getType());
	}

	@Test
	void testGetNameAndSetName() {
		ftpFile.setName("testFile.txt");
		assertEquals("testFile.txt", ftpFile.getName());
	}

	@Test
	void testSetSizeAndGet() {
		ftpFile.setSize(1024);
		assertEquals(1024, ftpFile.getSize());
	}

	@Test
	void testSetHardLinkCountAndGet() {
		ftpFile.setHardLinkCount(3);
		assertEquals(3, ftpFile.getHardLinkCount());
	}

	@Test
	void testSetAndGetGroup() {
		ftpFile.setGroup("admin");
		assertEquals("admin", ftpFile.getGroup());
	}

	@Test
	void testSetAndGetUser() {
		ftpFile.setUser("user123");
		assertEquals("user123", ftpFile.getUser());
	}

	@Test
	void testSetAndGetLink() {
		ftpFile.setLink("linkedFile.txt");
		assertEquals("linkedFile.txt", ftpFile.getLink());
	}

	@Test
	void testSetAndGetPermission() {
		ftpFile.setPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION, true);
		assertTrue(ftpFile.hasPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION));
	}

	@Test
	void testToString() {
		ftpFile.setRawListing(rawListingExample);
		assertEquals(rawListingExample, ftpFile.toString());
	}

	@Test
	void testSetTimestamp() {
		ftpFile.setTimestamp(Calendar.getInstance());
		assertNotNull(ftpFile.getTimestamp());
	}

	@Test
	void testHasNullPermission() {
		assertFalse(ftpFileRawListing.hasPermission(FTPFile.READ_PERMISSION, FTPFile.WRITE_PERMISSION));
	}

	@Test
	void testToFormattedString() {
		assertFalse(ftpFile.toFormattedString().isEmpty());
	}

	@Test
	void testInvalidToFormattedStringFile() {
		ftpFile.setType(FTPFile.FILE_TYPE);
		assertTrue(ftpFileRawListing.toFormattedString().contains("Invalid"));
	}

	@Test
	void testToFormattedStringTimezone() {
		assertFalse(ftpFile.toFormattedString("CET").isEmpty());
	}

	@Test
	void testToFormattedStringSymLink() {
		ftpFile.setType(FTPFile.SYMBOLIC_LINK_TYPE);
		assertFalse(ftpFile.toFormattedString().isEmpty());
	}

	@Test
	void testToFormattedStringDirectory() {
		ftpFile.setType(FTPFile.DIRECTORY_TYPE);
		assertFalse(ftpFile.toFormattedString("CET").isEmpty());
	}

	@Test
	void testToFormattedStringUnknown() {
		ftpFile.setType(FTPFile.UNKNOWN_TYPE);
		assertFalse(ftpFile.toFormattedString("CET").isEmpty());
	}

	@Test
	void testToFormattedStringReadPerm() {
		ftpFile.setPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION, true);
		assertFalse(ftpFile.toFormattedString().isEmpty());
	}
	
	@Test
	void testToFormattedStringWritePerm() {
		ftpFile.setPermission(FTPFile.USER_ACCESS, FTPFile.WRITE_PERMISSION, true);
		assertFalse(ftpFile.toFormattedString().isEmpty());
	}
	
	@Test
	void testToFormattedStringExecPerm() {
		ftpFile.setPermission(FTPFile.USER_ACCESS, FTPFile.EXECUTE_PERMISSION, true);
		assertFalse(ftpFile.toFormattedString().isEmpty());
	}
}
