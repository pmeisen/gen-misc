package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import org.junit.Test;

import net.meisen.general.genmisc.types.Files;

/**
 * Tests the <code>Files</code> implementation
 * 
 * @author pmeisen
 * 
 */
public class TestFiles {

	private final static File tmpDir = new File(
			System.getProperty("java.io.tmpdir"));

	/**
	 * Tests if the deletion of a none existing directory returns
	 * <code>true</code>
	 */
	@Test
	public void testDirectoryDeletionOfNotExistingDirectory() {
		final String rndFileName = UUID.randomUUID().toString();
		final File mainDir = new File(tmpDir, rndFileName);

		// make sure the file is deleted
		mainDir.delete();
		assertEquals(mainDir.exists(), false);
		assertEquals(Files.deleteDir(mainDir), true);

		// create it and delete it
		mainDir.mkdir();
		assertEquals(mainDir.exists(), true);
		assertEquals(Files.deleteDir(mainDir), true);
	}

	/**
	 * Tests the deletion implementation of a directory with content
	 * 
	 * @throws IOException
	 *             if a <code>File</code> for testing cannot be created
	 */
	@Test
	public void testDirectoryDeletionOfDirWithContent() throws IOException {
		final String rndFileName = UUID.randomUUID().toString();
		final File mainDir = new File(tmpDir, rndFileName);
		mainDir.mkdir();

		// create some files and sub-directories
		File lastDir = mainDir;
		for (int i = 0; i < 10; i++) {
			final String rndSubFileName = UUID.randomUUID().toString();
			final File subFile = new File(lastDir, rndSubFileName);

			if (i % 2 == 0) {
				// create the directory
				subFile.mkdir();
				lastDir = subFile;
			} else {

				// create a file
				subFile.createNewFile();
			}
		}

		// delete the mainDir
		assertEquals(mainDir.exists(), true);
		assertEquals(Files.deleteDir(mainDir), true);
		assertEquals(mainDir.exists(), false);
	}

	/**
	 * Tests the removal of a file extension
	 * 
	 * @throws IOException
	 *             if the test-files could not be created
	 */
	@Test
	public void testRemoveExtension() throws IOException {

		// just some Strings
		assertEquals(Files.removeExtension((File) null), null);
		assertEquals(Files.removeExtension((String) null), null);
		assertEquals(Files.removeExtension("myFile.test"), "myFile");
		assertEquals(Files.removeExtension("myFile.test.backup"), "myFile.test");
		assertEquals(Files.removeExtension("noExtensionFile"),
				"noExtensionFile");
		assertEquals(Files.removeExtension("C:\\test\\testFile.file"),
				"testFile");
		assertEquals(Files.removeExtension("C:\\testFile.file"), "testFile");
		assertEquals(Files.removeExtension("C:\\"), null);

		// do some stuff with real files
		final String rnd = UUID.randomUUID().toString();

		final File testFile = new File(tmpDir, rnd);
		testFile.mkdirs();

		assertEquals(Files.removeExtension(testFile), testFile.getName());
		assertTrue(Files.deleteDir(testFile));
	}

	/**
	 * Tests the implementation of the retrieval of the file extension
	 */
	@Test
	public void testGetFileExtension() {
		assertEquals(Files.getExtension((File) null), "");
		assertEquals(Files.getExtension((String) null), "");

		assertEquals(Files.getExtension("myFile"), "");
		assertEquals(Files.getExtension("myFile"), "");
		assertEquals(Files.getExtension("C:\\testFile.file"), "file");
		assertEquals(Files.getExtension("C:\\test\\testFile.file"), "file");
		assertEquals(Files.getExtension("theFile.jpg"), "jpg");
	}

	/**
	 * Tests the implementation of the mimeType determination, which is only
	 * based on the extension of the file (and not on the content)
	 */
	@Test
	public void testGetMimeType() {
		assertEquals(Files.getMimeType((File) null), Files.DEFAULT_MIMETYPE);
		assertEquals(Files.getMimeType((String) null), Files.DEFAULT_MIMETYPE);

		assertEquals(Files.getMimeType("myFile"), Files.DEFAULT_MIMETYPE);
		assertEquals(Files.getMimeType(""), Files.DEFAULT_MIMETYPE);
		assertEquals(Files.getMimeType("theFile.jpg"), "image/jpeg");
		assertEquals(Files.getMimeType("theFile.css"), "text/css");
		assertEquals(Files.getMimeType("C:\\test\\testFile.jpg"), "image/jpeg");
	}

	/**
	 * Tests the reading of properties from a <code>File</code>
	 * 
	 * @throws IOException
	 *             if the specified test file cannot be read
	 */
	@Test
	public void testReadProperties() throws IOException {

		// get the stream and copy it
		final String fileName = UUID.randomUUID().toString();
		final File file = new File(System.getProperty("java.io.tmpdir"),
				fileName);
		assertEquals(Files.copyResourceToFile("test.properties", file), true);

		// read the properties
		final Properties p = Files.readProperties(file);
		assertEquals(p.size(), 3);
		assertEquals(p.get("prop1"), "1");
		assertEquals(p.get("prop2"), "2");
		assertEquals(p.get("prop3"), "Whatever");

		// remove the file
		assertTrue(file.delete());
	}

	/**
	 * Tests the implementation of writing and reading from a <code>File</code>
	 * 
	 * @throws IOException
	 *             if the test-files cannot be created or read
	 * 
	 * @see Files#writeToFile(String, String, String)
	 * @see Files#readFromFile(String)
	 */
	@Test
	public void testWritingAndReading() throws IOException {
		final File file = new File(tmpDir, UUID.randomUUID().toString() + "."
				+ "test");
		final String fileName = Files.getCanonicalPath(file);

		// write something to the file
		final String content = "This is a Test";
		Files.writeToFile(fileName, content, "UTF-8");

		// check the content
		final String readContent = Files.readFromFile(fileName);
		assertEquals(readContent, content);

		assertTrue(file.delete());
	}

	/**
	 * Tests the implementation of <code>File.isInDirectory(File, File)</code>,
	 * <code>File.isInDirectory(File, String)</code>,
	 * <code>File.isInDirectory(String, String)</code>.
	 * 
	 * @throws IOException
	 *             if the test-file could not be created
	 */
	@Test
	public void testIsInDirectory() throws IOException {
		final File file = new File(tmpDir, UUID.randomUUID().toString() + "."
				+ "test");

		assertFalse(Files.isInDirectory(file, file));
		assertFalse(Files.isInDirectory(file.getAbsolutePath(),
				tmpDir.getParent()));
		assertFalse(Files.isInDirectory(file.getAbsolutePath(),
				tmpDir.getParentFile()));

		// create a new file
		assertTrue(file.createNewFile());

		// check the file
		assertFalse(Files.isInDirectory(file, file));
		assertTrue(Files.isInDirectory(file, tmpDir));
		assertTrue(Files.isInDirectory(file.getAbsolutePath(),
				tmpDir.getParent()));
		assertTrue(Files.isInDirectory(file.getAbsolutePath(),
				tmpDir.getParentFile()));

		// delete the test-file
		assertTrue(file.delete());
	}
}
