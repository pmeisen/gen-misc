package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import net.meisen.general.genmisc.resources.Manifest;

/**
 * Tests the retrieval of the manifest information from a jar
 * 
 * @author pmeisen
 */
public class TestManifest {

	/**
	 * Tests the retrieval of <code>null</code> class
	 * 
	 * @throws IOException
	 *             if the manifest cannot be read
	 */
	@Test
	public void testNullClass() throws IOException {
		assertEquals(Manifest.getManifestInfo(null), null);
	}
}
