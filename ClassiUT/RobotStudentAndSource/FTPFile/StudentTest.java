package student.pasqualeCriscuoloDue;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ClassUnderTest.FTPFile;

class StudentTest {
	
	public static FTPFile f;
	public static FTPFile f2;
	public static FTPFile f3;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		f = new FTPFile();
		f2 = new FTPFile();
		f3 = new FTPFile();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetRawListing() {
		String s = "Test";
		f.setRawListing(s);
		assertEquals(s,f.getRawListing());
	}

	@Test
	void testIsDirectory() {
		boolean[] res = new boolean[4];
		boolean[] pred = {false, true, false, false};
		for(int i=0; i<4; i++) {
			f.setType(i);
			res[i] = f.isDirectory();
		}
		assertArrayEquals(pred,res);
		f.setType(-1);
		assertFalse(f.isDirectory());
		f.setType(4);
		assertFalse(f.isDirectory());
		f.setType(10);
		assertFalse(f.isDirectory());
		f.setType(FTPFile.DIRECTORY_TYPE);
		assertTrue(f.isDirectory());
	}

	@Test
	void testIsFile() {
		boolean[] res = new boolean[4];
		boolean[] pred = {true, false, false, false};
		for(int i=0; i<4; i++) {
			f.setType(i);
			res[i] = f.isFile();
		}
		assertArrayEquals(pred,res);
		f.setType(-1);
		assertFalse(f.isFile());
		f.setType(4);
		assertFalse(f.isFile());
		f.setType(10);
		assertFalse(f.isFile());
		f.setType(FTPFile.FILE_TYPE);
		assertTrue(f.isFile());
	}

	@Test
	void testIsSymbolicLink() {
		boolean[] res = new boolean[4];
		boolean[] pred = {false, false, true, false};
		for(int i=0; i<4; i++) {
			f.setType(i);
			res[i] = f.isSymbolicLink();
		}
		assertArrayEquals(pred,res);
		f.setType(-1);
		assertFalse(f.isSymbolicLink());
		f.setType(4);
		assertFalse(f.isSymbolicLink());
		f.setType(10);
		assertFalse(f.isSymbolicLink());
		f.setType(FTPFile.SYMBOLIC_LINK_TYPE);
		assertTrue(f.isSymbolicLink());
	}

	@Test
	void testIsUnknown() {
		boolean[] res = new boolean[4];
		boolean[] pred = {false, false, false, true};
		for(int i=0; i<4; i++) {
			f.setType(i);
			res[i] = f.isUnknown();
		}
		assertArrayEquals(pred,res);
		f.setType(-1);
		assertFalse(f.isUnknown());
		f.setType(4);
		assertFalse(f.isUnknown());
		f.setType(10);
		assertFalse(f.isUnknown());
		f.setType(FTPFile.UNKNOWN_TYPE);
		assertTrue(f.isUnknown());
	}

	@Test
	void testSetType() {
		assertEquals(f.getType(),3);
		f.setType(0);
		assertEquals(f.getType(),0);
	}

	@Test
	void testSetName() {
		assertEquals(f.getName(),null);
		f.setName("Test");
		assertEquals(f.getName(),"Test");
	}

	@Test
	void testSetSize() {
		assertEquals(f.getSize(),-1);
		f.setSize(4);
		assertEquals(f.getSize(),4);
	}


	@Test
	void testSetHardLinkCount() {
		assertEquals(f.getHardLinkCount(),0);
		f.setHardLinkCount(3);
		assertEquals(f.getHardLinkCount(),3);
	}


	@Test
	void testSetGroup() {
		assertEquals(f.getGroup(), "");
		f.setGroup("GroupTest");
		assertEquals(f.getGroup(), "GroupTest");
	}

	@Test
	void testSetUser() {
		assertEquals(f.getUser(), "");
		f.setUser("UserTest");
		assertEquals(f.getUser(), "UserTest");
	}

	@Test
	void testSetLink() {
		assertEquals(f.getLink(), null);
		f.setLink("LinkTest");
		assertEquals(f.getLink(), "LinkTest");	}

	@Test
	void testSetTimestamp() {
		assertEquals(f.getTimestamp(), null);
		Calendar c = Calendar.getInstance();
		f.setTimestamp(c);
		assertEquals(f.getTimestamp(), c);
	}

	@Test
	void testSetPermission() {
		assertFalse(f.hasPermission(1,1));
		f.setPermission(1, 1, false);
		assertFalse(f.hasPermission(1,1));
		f.setPermission(1, 1, true);
		assertTrue(f.hasPermission(1,1));
		assertThrowsExactly(ArrayIndexOutOfBoundsException.class, () -> f.setPermission(-1,2,false));
		assertThrowsExactly(ArrayIndexOutOfBoundsException.class, () -> f.setPermission(3,2,false));
		assertThrowsExactly(ArrayIndexOutOfBoundsException.class, () -> f.setPermission(2,4,false));
		assertThrowsExactly(ArrayIndexOutOfBoundsException.class, () -> f.setPermission(4,4,false));
		assertThrowsExactly(ArrayIndexOutOfBoundsException.class, () -> f.setPermission(-1,2,false));
		assertThrowsExactly(ArrayIndexOutOfBoundsException.class, () -> f.setPermission(-1,-1,false));
		assertThrowsExactly(ArrayIndexOutOfBoundsException.class, () -> f.setPermission(-1,4,false));
		assertThrowsExactly(ArrayIndexOutOfBoundsException.class, () -> f.setPermission(4,-1,false));
	}
	
	@Test
	void testToFormattedStringString() {
		
		Calendar c = Calendar.getInstance();
		f.toFormattedString(null);
		String t = null;
		f.toFormattedString(t);
		f.setTimestamp(c);
		t  = new String("America/Los_Angeles");
		f.toFormattedString(t);
		f.toFormattedString(null);
		/*
		f2.file.toFormattedString(t);
		f3.test();
		f3.file.toFormattedString(t);
		f3.ts1();
		f3.file.toFormattedString(t);
		f3.ts2();
		f3.file.toFormattedString(t);
		f3.ts3();
		f3.file.toFormattedString(t);
		f3.ts4();
		f3.file.toFormattedString(t);
		f3.ts5();
		f3.file.toFormattedString(t);
		*/
	}
	
	@Test
	void testPermissionToString() {
		/*
		assertNull(f2.perm);
		f2.test1();
		assertEquals("---------",f2.perm);
		f2.test2();
		assertEquals("rwx------",f2.perm);
		*/
	}

	
}
