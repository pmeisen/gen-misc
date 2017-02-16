package net.meisen.general.genmisc;

import net.meisen.general.genmisc.resources.ResourceInfo;
import net.meisen.general.genmisc.resources.ResourceType;
import net.meisen.general.genmisc.types.Files;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link ResourceInfo} class
 *
 * @author pmeisen
 */
public class TestResourceInfo {
    private static final String RESOURCE_DIR = "testResources";

    // the working directory used for the test
    private static String workingDir;
    private static String resourceDir = Files.getCanonicalPath(RESOURCE_DIR);
    private static FileManager fileManager = new FileManager();

    /**
     * Initializes the resources available
     */
    @BeforeClass
    public static void init() {

        // set the working directory and print it
        workingDir = Files.getCanonicalPath(System.getProperty("user.dir"));

        // print the working directory
        System.out.println("The working directory is: " + workingDir);
        System.out.println("- The following directory must be on the "
                + "class-path: " + resourceDir);
        System.out.println("- The working directory cannot be write protected");
    }

    /**
     * Checks files of the file-system
     *
     * @throws IOException if a test-file cannot be created
     */
    @Test
    public void testFiles() throws IOException {
        ResourceInfo info;

        // check a file-system directory
        info = new ResourceInfo(workingDir, false);
        assertEquals(info.getInJarPath(), null);
        assertEquals(info.getJarPath(), null);
        assertEquals(info.getType(), ResourceType.FILE_SYSTEM_PATH);
        assertEquals(info.getFullPath(), Files.getCanonicalPath(workingDir));

        // check a file-system file
        final File file = fileManager.createFile(workingDir);
        info = new ResourceInfo(file.getAbsolutePath(), true);
        assertEquals(info.getInJarPath(), null);
        assertEquals(info.getJarPath(), null);
        assertEquals(info.getType(), ResourceType.FILE_SYSTEM_FILE);
        assertEquals(info.getFullPath(), Files.getCanonicalPath(file));

        // lets create a file as resource, which can be found via class-path but
        // is still a file
        final File resFile = new File(resourceDir, "file.txt");
        System.out.println(resFile);
        info = new ResourceInfo(resFile.getName(), true);
        assertEquals(info.getInJarPath(), null);
        assertEquals(info.getJarPath(), null);
        assertEquals(info.getType(), ResourceType.FILE_SYSTEM_FILE);
        assertTrue(info.getFullPath(), info.getFullPath().endsWith("file.txt"));
    }

    /**
     * Checks files within a jar
     *
     * @throws IOException if the test-file cannot be created
     */
    @Test
    public void testFilesInJar() throws IOException {
        ResourceInfo info;

        // check a relative class-path file
        info = new ResourceInfo("META-INF\\MANIFEST.MF", true);
        assertEquals(info.getInJarPath(), "META-INF/MANIFEST.MF");
        assertEquals(info.getJarPath() != null, true);
        assertEquals(info.getType(), ResourceType.IN_JAR_FILE);
        assertEquals(info.getFullPath().endsWith(".jar!/META-INF/MANIFEST.MF"),
                true);

        // check a relative class-path file
        info = new ResourceInfo("net\\meisen\\general\\gendummy\\dummy.txt", true);
        assertEquals(info.getInJarPath(), "net/meisen/general/gendummy/dummy.txt");
        assertEquals(info.getJarPath().matches(".*net-meisen-general-gen-dummy.*\\.jar"), true);
        assertEquals(info.getType(), ResourceType.IN_JAR_FILE);
        assertEquals(info.getFullPath().endsWith(".jar!/net/meisen/general/gendummy/dummy.txt"), true);

        // check an absolute class-path file
        info = new ResourceInfo("net\\meisen\\general\\gendummy\\dummy.txt",
                true);
        final String jarFile = info.getJarPath();
        final String fullPathInJar = new File(jarFile).toURI().toURL()
                + "!/net/meisen/general/gendummy/dummy.txt";
        info = new ResourceInfo(fullPathInJar, true);
        assertEquals(info.getInJarPath(),
                "net/meisen/general/gendummy/dummy.txt");
        assertEquals(info.getJarPath(), jarFile);
        assertEquals(info.getType(), ResourceType.IN_JAR_FILE);
        assertEquals(
                info.getFullPath().endsWith(
                        ".jar!/net/meisen/general/gendummy/dummy.txt"), true);

        // check an absolute class-path file which does not exist
        info = new ResourceInfo(fullPathInJar + ".notexist", true);
        assertEquals(info.getInJarPath(), null);
        assertEquals(info.getJarPath(), null);
        assertEquals(info.getType(), null);
        assertEquals(info.getFullPath(), null);

        // check an absolute class-path file with "invalid" syntax
        info = new ResourceInfo(fullPathInJar.replace('/', '\\'), true);
        assertEquals(info.getInJarPath(), "net/meisen/general/gendummy/dummy.txt");
        assertEquals(info.getJarPath(), jarFile);
        assertEquals(info.getType(), ResourceType.IN_JAR_FILE);
        assertEquals(info.getFullPath().endsWith(".jar!/net/meisen/general/gendummy/dummy.txt"), true);

        // check an absolute class-path file with "invalid" syntax
        String path = fullPathInJar.replace("file:/", "").replace('/', '\\');
        if (!path.startsWith(File.separator)) {
            path = File.separator + path;
        }
        info = new ResourceInfo(path, true);
        assertEquals(info.getInJarPath(), "net/meisen/general/gendummy/dummy.txt");
        assertEquals(info.getJarPath(), jarFile);
        assertEquals(info.getType(), ResourceType.IN_JAR_FILE);
        assertEquals(info.getFullPath().endsWith(".jar!/net/meisen/general/gendummy/dummy.txt"), true);

        // check an absolute class-path file with a root
        info = new ResourceInfo("net\\meisen\\general\\gendummy\\dummy.txt", jarFile, true);
        assertEquals(info.getInJarPath(), "net/meisen/general/gendummy/dummy.txt");
        assertEquals(info.getJarPath(), jarFile);
        assertEquals(info.getType(), ResourceType.IN_JAR_FILE);
        assertEquals(info.getFullPath().endsWith(".jar!/net/meisen/general/gendummy/dummy.txt"), true);

        // check a none existing file
        info = new ResourceInfo("neverevershouldexists/unexistentFile.bad", true);
        assertEquals(info.getInJarPath(), null);
        assertEquals(info.getJarPath(), null);
        assertEquals(info.getType(), null);
        assertEquals(info.getFullPath(), null);

        // check a class-path file which is a directory
        info = new ResourceInfo("META-INF", true);
        assertEquals(info.getInJarPath(), null);
        assertEquals(info.getJarPath(), null);
        assertEquals(info.getType(), null);
        assertEquals(info.getFullPath(), null);

        // check a class-path file which is a directory (with marker)
        info = new ResourceInfo("META-INF/", true);
        assertEquals(info.getInJarPath(), null);
        assertEquals(info.getJarPath(), null);
        assertEquals(info.getType(), null);
        assertEquals(info.getFullPath(), null);
    }

    /**
     * Test the <code>ResourceInfo</code> of directories on the file-system
     *
     * @throws IOException if a test-directory cannot be created
     */
    @Test
    public void testDirectory() throws IOException {
        ResourceInfo info;

        // check a not existing file
        info = new ResourceInfo("neverevershouldexists", false);
        assertEquals(info.getInJarPath(), null);
        assertEquals(info.getJarPath(), null);
        assertEquals(info.getType(), null);
        assertEquals(info.getFullPath(), null);

        // check a relative class-path directory
        final File file = new File(resourceDir, "dir.sample");
        info = new ResourceInfo(file.getName(), false);
        assertEquals(info.getInJarPath(), null);
        assertEquals(info.getJarPath(), null);
        assertEquals(info.getType(), ResourceType.FILE_SYSTEM_PATH);
        assertTrue(info.getFullPath(), info.getFullPath().endsWith("dir.sample/"));
    }

    /**
     * Test the <code>ResourceInfo</code> of directories within a jar
     */
    @Test
    public void testDirectoryInJar() {
        ResourceInfo info;

        // check a relative class-path directory
        info = new ResourceInfo("net\\meisen\\general\\gendummy", false);
        assertEquals(info.getInJarPath(), "net/meisen/general/gendummy/");
        assertEquals(info.getJarPath() != null, true);
        assertEquals(info.getType(), ResourceType.IN_JAR_PATH);
        assertEquals(
                info.getFullPath().endsWith(
                        ".jar!/net/meisen/general/gendummy/"), true);
    }

    /**
     * Tests some general errors which could be made and should be thrown/result
     * in <code>null</code>
     *
     * @throws IOException if a test-file or directory cannot be created
     */
    @Test
    public void testGeneralErrors() throws IOException {
        ResourceInfo info;
        File file;

        // check a file and mark it as path
        file = new File(resourceDir, "file.txt");
        info = new ResourceInfo(file.getName(), false);
        assertEquals(info.getInJarPath(), null);
        assertEquals(info.getJarPath(), null);
        assertEquals(info.getType(), null);
        assertEquals(info.getFullPath(), null);

        // check a directory and mark it as file
        file = new File(resourceDir, "dir.sample");
        info = new ResourceInfo(file.getName(), true);
        assertEquals(info.getInJarPath(), null);
        assertEquals(info.getJarPath(), null);
        assertEquals(info.getType(), null);
        assertEquals(info.getFullPath(), null);
    }

    /**
     * Tests the retrieval of some files with special characters
     *
     * @throws IOException if a test-file or directory cannot be created
     */
    @Test
    public void testUrlEncoding() throws IOException {

        // create a folder with special characters
        final File dir = new File(resourceDir, File.separator + "Test with äüöäüö");
        assertTrue(dir.exists());

        String relPath;
        ResourceInfo info;
        File file;

        // first create a simple file within the directory
        file = new File(dir, "sample.file");
        assertTrue(file.exists());

        // the relative path
        relPath = dir.getName() + "/" + file.getName();

        // check the result
        info = new ResourceInfo(relPath, true);
        assertTrue(info.getFullPath(), info.getFullPath().endsWith(relPath));
        assertEquals(ResourceType.FILE_SYSTEM_FILE, info.getType());
        assertNull(info.getInJarPath());
        assertNull(info.getJarPath());

        // create a file with special chars
        file = new File(dir, "änöther sämple.file tüpe");
        assertTrue(file.exists());

        // the relative path
        relPath = dir.getName() + "/" + file.getName();

        // check the result
        info = new ResourceInfo(relPath, true);
        assertTrue(info.getFullPath(), info.getFullPath().endsWith(relPath));
        assertEquals(ResourceType.FILE_SYSTEM_FILE, info.getType());
        assertNull(info.getInJarPath());
        assertNull(info.getJarPath());

        // files from another jar-file
        relPath = "net/meisen/general/gendummy/göt me ä töst/a föle with special chäräcters.txt";
        info = new ResourceInfo(relPath, true);
        assertEquals(ResourceType.IN_JAR_FILE, info.getType());

        assertTrue(info
                .getFullPath()
                .endsWith(
                        ".jar!/net/meisen/general/gendummy/g%c3%b6t%20me%20%c3%a4%20t%c3%b6st/a%20f%c3%b6le%20with%20special%20ch%c3%a4r%c3%a4cters.txt"));
        assertEquals(
                "net/meisen/general/gendummy/göt me ä töst/a föle with special chäräcters.txt",
                info.getInJarPath());
        assertTrue(new File(info.getJarPath()).exists());
        assertTrue(new File(info.getJarPath()).isFile());

        // create a resource from the retrieved data
        info = new ResourceInfo(info.getFullPath(), true);
        assertEquals(ResourceType.IN_JAR_FILE, info.getType());
        assertTrue(info
                .getFullPath()
                .endsWith(
                        ".jar!/net/meisen/general/gendummy/g%c3%b6t%20me%20%c3%a4%20t%c3%b6st/a%20f%c3%b6le%20with%20special%20ch%c3%a4r%c3%a4cters.txt"));
        assertEquals(
                "net/meisen/general/gendummy/göt me ä töst/a föle with special chäräcters.txt",
                info.getInJarPath());
        assertTrue(new File(info.getJarPath()).exists());
        assertTrue(new File(info.getJarPath()).isFile());
    }

    /**
     * Test the implementation of a
     * {@link ResourceInfo#transformFromFileCollection(Collection)}.
     *
     * @throws IOException if a file could not be found
     */
    @Test
    public void testTransformFromFileCollection() throws IOException {
        final File tmpDir = fileManager.createDir(resourceDir);

        final File dir = new File(tmpDir, "anotherDir");
        assertTrue(dir.mkdirs());

        // create some files
        for (int i = 0; i < 10; i++) {
            final File file1 = new File(tmpDir, "file" + i);
            final File file2 = new File(dir, "file" + i);

            assertTrue(file1.createNewFile());
            assertTrue(file2.createNewFile());
        }

        // get all the files in the directory
        final List<File> files = Files.getDirectoryContent(tmpDir);

        // next check the root
        final Collection<ResourceInfo> resInfosNoRoot = ResourceInfo.transformFromFileCollection(files);
        assertEquals(21, resInfosNoRoot.size());

        final Collection<ResourceInfo> resInfosTmpRoot = ResourceInfo.transformFromFileCollection(tmpDir, files);
        assertEquals(21, resInfosTmpRoot.size());
        assertEquals(resInfosNoRoot, resInfosTmpRoot);

        final Collection<ResourceInfo> resInfosDirRoot = ResourceInfo.transformFromFileCollection(dir, files);
        assertEquals(11, resInfosDirRoot.size());

        // cleanUp
        for (int i = 0; i < 10; i++) {
            final File file1 = new File(tmpDir, "file" + i);
            final File file2 = new File(dir, "file" + i);

            assertTrue(file1.delete());
            assertTrue(file2.delete());
        }

        assertTrue(Files.deleteDir(dir));
    }

    /**
     * Cleans up after the tests
     */
    @AfterClass
    public static void cleanUp() {

        // cleanup
        fileManager.cleanUp();
    }
}
