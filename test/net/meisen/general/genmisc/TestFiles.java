package net.meisen.general.genmisc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import net.meisen.general.genmisc.raster.utilities.GeneralUtilities;
import net.meisen.general.genmisc.types.Files;
import net.meisen.general.genmisc.types.Streams;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the <code>Files</code> implementation
 * 
 * @author pmeisen
 * 
 */
public class TestFiles {

	private final static File tmpDir = new File(System.getProperty("java.io.tmpdir"));

	/**
	 * Utilities for the test.
	 * 
	 * @author pmeisen
	 * 
	 */
	public static final class Util {

		/**
		 * Helper to use the unzipping implementation to unzip the specified
		 * {@code resource} of the classpath.
		 * 
		 * @param resource
		 *            the location of the resource to be unzipped on the
		 *            classpath
		 * 
		 * @return the created directory with the unzipped content
		 * 
		 * @throws IOException
		 *             if the file cannot be unzipped
		 */
		public static File testUnzipHelper(final String resource)
				throws IOException {

			// create a temporary directory
			final File tmpTestDir = new File(tmpDir, UUID.randomUUID().toString());
			assertTrue(tmpTestDir.mkdir());

			// get the archive to unzip
			final InputStream stream = Util.class.getClassLoader()
					.getResourceAsStream(resource);
			assertNotNull(stream);

			// test the unzipping
			Files.unzip(stream, tmpTestDir);

			// cleanUp
			assertNull(Streams.closeIO(stream));

			return tmpTestDir;
		}

		/**
		 * Validates the unzipped content of the zip-archiv located at
		 * {@code net/meisen/general/genmisc/zipArchives/filesAndDirs.zip}.
		 * 
		 * @param tmpTestDir
		 *            the directory to be validated
		 * 
		 * @throws IOException
		 *             if the a file cannot be validated
		 */
		public static void validateUnzippedFilesAndDir(final File tmpTestDir)
				throws IOException {

			final File emptyDir = new File(tmpTestDir, "emptyDir");
			final File onlyFiles = new File(tmpTestDir, "onlyFiles");
			final File fullDir = new File(tmpTestDir, "fullDir");
			final File subEmptyDir = new File(tmpTestDir, "fullDir/emptyDir");
			final File subDir = new File(tmpTestDir, "fullDir/anotherDir");

			List<File> files = Files.getCurrentFilelist(tmpTestDir);
			List<File> subs = Files.getCurrentSubDirectories(tmpTestDir);
			assertEquals(5, files.size());
			assertEquals(3, subs.size());
			assertTrue(files.contains(new File(tmpTestDir, "File1.txt")));
			assertTrue(files.contains(new File(tmpTestDir, "File2.txt")));
			assertTrue(files.contains(new File(tmpTestDir, "File3.txt")));
			assertTrue(files.contains(new File(tmpTestDir, "File4.txt")));
			assertTrue(files.contains(new File(tmpTestDir, "File5.txt")));
			assertTrue(subs.contains(emptyDir));
			assertTrue(subs.contains(onlyFiles));
			assertTrue(subs.contains(fullDir));

			// check the content of File3.txt
			final String content = Files.readFromFile(new File(tmpTestDir,
					"File4.txt"), "UTF8");
			assertEquals("sÄÜÖÜÄÖ", content);

			files = Files.getCurrentFilelist(emptyDir);
			subs = Files.getCurrentSubDirectories(emptyDir);
			assertEquals(0, files.size());
			assertEquals(0, subs.size());

			files = Files.getCurrentFilelist(onlyFiles);
			subs = Files.getCurrentSubDirectories(onlyFiles);
			assertEquals(5, files.size());
			assertEquals(0, subs.size());
			assertTrue(files.contains(new File(onlyFiles, "File1.txt")));
			assertTrue(files.contains(new File(onlyFiles, "File2.txt")));
			assertTrue(files.contains(new File(onlyFiles, "File3.txt")));
			assertTrue(files.contains(new File(onlyFiles, "File4.txt")));
			assertTrue(files.contains(new File(onlyFiles, "File5.txt")));

			files = Files.getCurrentFilelist(fullDir);
			subs = Files.getCurrentSubDirectories(fullDir);
			assertEquals(1, files.size());
			assertEquals(2, subs.size());
			assertTrue(files.contains(new File(fullDir, "file.txt")));
			assertTrue(subs.contains(new File(fullDir, "anotherDir")));
			assertTrue(subs.contains(new File(fullDir, "emptDir")));

			files = Files.getCurrentFilelist(subEmptyDir);
			subs = Files.getCurrentSubDirectories(subEmptyDir);
			assertEquals(0, files.size());
			assertEquals(0, subs.size());

			files = Files.getCurrentFilelist(subDir);
			subs = Files.getCurrentSubDirectories(subDir);
			assertEquals(1, files.size());
			assertEquals(0, subs.size());
			assertTrue(files.contains(new File(subDir, "file.txt")));
		}
	}

	/**
	 * Tests if the deletion of a none existing directory returns
	 * <code>true</code>
	 */
	@Test
	public void testDirectoryDeletionOfNotExistingDirectory() {
		final String rndFileName = UUID.randomUUID().toString();
		final File mainDir = new File(tmpDir, rndFileName);

		// make sure the file is deleted
		assertTrue(!mainDir.exists() || mainDir.delete());
		assertEquals(mainDir.exists(), false);
		assertEquals(Files.deleteDir(mainDir), true);

		// create it and delete it
		assertTrue(mainDir.mkdir());
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
		assertTrue(mainDir.mkdir());

		// create some files and sub-directories
		File lastDir = mainDir;
		for (int i = 0; i < 10; i++) {
			final String rndSubFileName = UUID.randomUUID().toString();
			final File subFile = new File(lastDir, rndSubFileName);

			if (i % 2 == 0) {
				// create the directory
				assertTrue(subFile.mkdir());
				lastDir = subFile;
			} else {

				// create a file
				assertTrue(subFile.createNewFile());
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
		assertEquals(Files.removeExtension("noExtensionFile"), "noExtensionFile");

		if (GeneralUtilities.isWindows()) {
			assertEquals(Files.removeExtension("C:\\test\\testFile.file"), "testFile");
			assertEquals(Files.removeExtension("C:\\testFile.file"), "testFile");
			assertEquals(Files.removeExtension("C:\\"), null);
		} else {
			assertEquals(Files.removeExtension("/subdir/testFile.file"), "testFile");
			assertEquals(Files.removeExtension("/testFile.file"), "testFile");
			assertEquals(Files.removeExtension("/"), null);
		}

		// do some stuff with real files
		final String rnd = UUID.randomUUID().toString();

		final File testFile = new File(tmpDir, rnd);
		assertTrue(testFile.mkdirs());

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

	/**
	 * Tests the unzipping of an empty archive.
	 * 
	 * @throws IOException
	 *             if the zip cannot be read or unzipped
	 */
	@Test
	public void testUnzipEmptyArchive() throws IOException {

		// unzip and check the result
		final File tmpTestDir = Util
				.testUnzipHelper("net/meisen/general/genmisc/zipArchives/empty.zip");

		final List<File> files = Files.getCurrentFilelist(tmpTestDir);
		final List<File> subs = Files.getCurrentSubDirectories(tmpTestDir);

		assertEquals(0, files.size());
		assertEquals(0, subs.size());

		// cleanUp
		assertTrue(Files.deleteDir(tmpTestDir));
	}

	/**
	 * Tests the unzipping of an archive with a singel file (in the root).
	 * 
	 * @throws IOException
	 *             if the zip cannot be read or unzipped
	 */
	@Test
	public void testUnzipSingleFile() throws IOException {

		// unzip and check the result
		final File tmpTestDir = Util
				.testUnzipHelper("net/meisen/general/genmisc/zipArchives/singleFile.zip");

		final List<File> files = Files.getCurrentFilelist(tmpTestDir);
		final List<File> subs = Files.getCurrentSubDirectories(tmpTestDir);

		assertEquals(1, files.size());
		assertEquals(0, subs.size());
		assertTrue(files.contains(new File(tmpTestDir, "singleFile.txt")));

		// cleanUp
		assertTrue(Files.deleteDir(tmpTestDir));
	}

	/**
	 * Tests the unzipping of an archive with multiple files (in the root).
	 * 
	 * @throws IOException
	 *             if the zip cannot be read or unzipped
	 */
	@Test
	public void testUnzipMultipleFile() throws IOException {

		// unzip and check the result
		final File tmpTestDir = Util
				.testUnzipHelper("net/meisen/general/genmisc/zipArchives/multipleFiles.zip");

		final List<File> files = Files.getCurrentFilelist(tmpTestDir);
		final List<File> subs = Files.getCurrentSubDirectories(tmpTestDir);

		assertEquals(5, files.size());
		assertEquals(0, subs.size());
		assertTrue(files.contains(new File(tmpTestDir, "File1.txt")));
		assertTrue(files.contains(new File(tmpTestDir, "File2.txt")));
		assertTrue(files.contains(new File(tmpTestDir, "File3.txt")));
		assertTrue(files.contains(new File(tmpTestDir, "File4.txt")));
		assertTrue(files.contains(new File(tmpTestDir, "File5.txt")));

		// cleanUp
		assertTrue(Files.deleteDir(tmpTestDir));
	}

	/**
	 * Tests the unzipping of an archive with files and an empty directory (in
	 * the root).
	 * 
	 * @throws IOException
	 *             if the zip cannot be read or unzipped
	 */
	@Test
	public void testUnzipFilesAndEmptyDir() throws IOException {

		// unzip and check the result
		final File tmpTestDir = Util
				.testUnzipHelper("net/meisen/general/genmisc/zipArchives/filesAndEmptyDir.zip");

		final List<File> files = Files.getCurrentFilelist(tmpTestDir);
		final List<File> subs = Files.getCurrentSubDirectories(tmpTestDir);

		assertEquals(5, files.size());
		assertEquals(1, subs.size());
		assertTrue(files.contains(new File(tmpTestDir, "File1.txt")));
		assertTrue(files.contains(new File(tmpTestDir, "File2.txt")));
		assertTrue(files.contains(new File(tmpTestDir, "File3.txt")));
		assertTrue(files.contains(new File(tmpTestDir, "File4.txt")));
		assertTrue(files.contains(new File(tmpTestDir, "File5.txt")));
		assertTrue(subs.contains(new File(tmpTestDir, "emptyDir")));

		// cleanUp
		assertTrue(Files.deleteDir(tmpTestDir));
	}

	/**
	 * Tests the unzipping of a more comples archive.
	 * 
	 * @throws IOException
	 *             if the zip cannot be read or unzipped
	 */
	@Test
	public void testUnzipFilesAndDir() throws IOException {

		// unzip and check the result
		final File tmpTestDir = Util
				.testUnzipHelper("net/meisen/general/genmisc/zipArchives/filesAndDirs.zip");

		Util.validateUnzippedFilesAndDir(tmpTestDir);

		// cleanUp
		assertTrue(Files.deleteDir(tmpTestDir));
	}

	/**
	 * Tests the zipping of a directory.
	 * 
	 * @throws IOException
	 *             if the directory cannot be accessed
	 */
	@Test
	public void testZip() throws IOException {
		final File tmpTestDir = Util
				.testUnzipHelper("net/meisen/general/genmisc/zipArchives/filesAndDirs.zip");
		final File zippedFile = new File(tmpDir, "archiv.zip");

		// zip everything
		Files.zipDirectory(tmpTestDir, zippedFile);

		// remove the directory and unzip it again
		assertTrue(Files.deleteDir(tmpTestDir));
		Files.unzip(zippedFile, tmpTestDir);

		// validate it
		Util.validateUnzippedFilesAndDir(tmpTestDir);

		// cleanUp
		// assertTrue(Files.deleteDir(tmpTestDir));
		assertTrue(zippedFile.delete());
	}

	/**
	 * Tests the implementation of
	 * {@code Files#writeProperties(File, Properties)}.
	 * 
	 * @throws IOException
	 *             if an IO-error occurred
	 */
	@Test
	public void writePropertyFile() throws IOException {

		final Properties myTestProperties = new Properties();
		myTestProperties.setProperty("propertyKey", "propertyValue");

		// create the files for the test
		final File tmpDir = new File(System.getProperty("java.io.tmpdir"), UUID
				.randomUUID().toString());
		final File tmpFile = new File(tmpDir, "myTest.properties");

		// make sure we don't have a parent currently and not a file
		assertFalse(tmpFile.exists());
		assertFalse(tmpFile.getParentFile().exists());

		try {
			// write the properties to the file
			Files.writeProperties(tmpFile, myTestProperties);

			// the file should have been created
			assertTrue(tmpFile.exists());

			final Properties myLoadedProperties = Files.readProperties(tmpFile);
			assertEquals(myTestProperties, myLoadedProperties);
		} finally {
			// cleanUp
			Files.deleteDir(tmpDir);
		}
	}
}
