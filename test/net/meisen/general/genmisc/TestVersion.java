package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.meisen.general.genmisc.resources.Version;

/**
 * Tests the <code>Version</code> implementation
 * 
 * @author pmeisen
 * 
 */
public class TestVersion {

	/**
	 * Tests the parsing of an empty String (i.e. "")
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEmptyString() {
		Version.parse("");
	}

	/**
	 * Tests the parsing of a <code>null</code> String
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullString() {
		Version.parse(null);
	}

	/**
	 * Tests a <code>Version</code> which has only a MAJOR value
	 */
	@Test
	public void testOnlyMajor() {
		Version v = Version.parse("1");
		assertEquals(v.getMajorAsInt(), 1);
		assertEquals(v.getMinorAsInt(), 0);
		assertEquals(v.getBuildAsInt(), 0);
		assertEquals(v.getRevisionAsInt(), 0);
		assertEquals(v.getPrefix(), "");
		assertEquals(v.getSuffix(), "");
		assertEquals(v.toString(), "1.0.0.0");

		v = Version.parse("2.0.0.0");
		assertEquals(v.getMajorAsInt(), 2);
		assertEquals(v.getMinorAsInt(), 0);
		assertEquals(v.getBuildAsInt(), 0);
		assertEquals(v.getRevisionAsInt(), 0);
		assertEquals(v.getPrefix(), "");
		assertEquals(v.getSuffix(), "");
		assertEquals(v.toString(), "2.0.0.0");

		v = Version.parse("3.0");
		assertEquals(v.getMajorAsInt(), 3);
		assertEquals(v.getMinorAsInt(), 0);
		assertEquals(v.getBuildAsInt(), 0);
		assertEquals(v.getRevisionAsInt(), 0);
		assertEquals(v.getPrefix(), "");
		assertEquals(v.getSuffix(), "");
		assertEquals(v.toString(), "3.0.0.0");
	}

	/**
	 * Tests a <code>Version</code> which has only a MAJOR value
	 */
	@Test
	public void testOnlyMinor() {
		Version v = Version.parse("0.1");
		assertEquals(v.getMajorAsInt(), 0);
		assertEquals(v.getMinorAsInt(), 1);
		assertEquals(v.getBuildAsInt(), 0);
		assertEquals(v.getRevisionAsInt(), 0);
		assertEquals(v.getPrefix(), "");
		assertEquals(v.getSuffix(), "");
		assertEquals(v.toString(), "0.1.0.0");

		v = Version.parse("0.2.0");
		assertEquals(v.getMajorAsInt(), 0);
		assertEquals(v.getMinorAsInt(), 2);
		assertEquals(v.getBuildAsInt(), 0);
		assertEquals(v.getRevisionAsInt(), 0);
		assertEquals(v.getPrefix(), "");
		assertEquals(v.getSuffix(), "");
		assertEquals(v.toString(), "0.2.0.0");

		v = Version.parse("0.3.0.0");
		assertEquals(v.getMajorAsInt(), 0);
		assertEquals(v.getMinorAsInt(), 3);
		assertEquals(v.getBuildAsInt(), 0);
		assertEquals(v.getRevisionAsInt(), 0);
		assertEquals(v.getPrefix(), "");
		assertEquals(v.getSuffix(), "");
		assertEquals(v.toString(), "0.3.0.0");
	}

	/**
	 * Tests only the BUILD component
	 */
	@Test
	public void testOnlyBuild() {
		Version v = Version.parse("0.0.1");
		assertEquals(v.getMajorAsInt(), 0);
		assertEquals(v.getMinorAsInt(), 0);
		assertEquals(v.getBuildAsInt(), 1);
		assertEquals(v.getRevisionAsInt(), 0);
		assertEquals(v.getPrefix(), "");
		assertEquals(v.getSuffix(), "");
		assertEquals(v.toString(), "0.0.1.0");

		v = Version.parse("0.0.2.0");
		assertEquals(v.getMajorAsInt(), 0);
		assertEquals(v.getMinorAsInt(), 0);
		assertEquals(v.getBuildAsInt(), 2);
		assertEquals(v.getRevisionAsInt(), 0);
		assertEquals(v.getPrefix(), "");
		assertEquals(v.getSuffix(), "");
		assertEquals(v.toString(), "0.0.2.0");
	}

	/**
	 * Tests only the REVISION component
	 */
	@Test
	public void testOnlyRevision() {
		Version v = Version.parse("0.0.0.1");
		assertEquals(v.getMajorAsInt(), 0);
		assertEquals(v.getMinorAsInt(), 0);
		assertEquals(v.getBuildAsInt(), 0);
		assertEquals(v.getRevisionAsInt(), 1);
		assertEquals(v.getPrefix(), "");
		assertEquals(v.getSuffix(), "");
		assertEquals(v.toString(), "0.0.0.1");
	}

	/**
	 * Tests the SNAPSHOT identifier
	 */
	@Test
	public void testSnapshot() {
		Version v = Version.parse("1-SNAPSHOT");
		assertEquals(v.isSnapshot(), true);
		assertEquals(v.toString(), "1.0.0.0-SNAPSHOT");

		v = Version.parse("1.0.1-SNAPSHOT");
		assertEquals(v.isSnapshot(), true);
		assertEquals(v.toString(), "1.0.1.0-SNAPSHOT");

		v = Version.parse("1.0.1-NOSNAPSHOT");
		assertEquals(v.isSnapshot(), false);
		assertEquals(v.toString(), "1.0.1.0");
	}

	/**
	 * Tests the comparison of two <code>Versions</code>
	 */
	@Test
	public void testComparison() {

		// equal
		Version v1 = Version.parse("1");
		Version v2 = Version.parse("1");
		assertEquals(v1.compareTo(v2), 0);
		assertEquals(v2.compareTo(v1), 0);

		// equal different String representation
		v1 = Version.parse("1.0");
		v2 = Version.parse("1");
		assertEquals(v1.compareTo(v2), 0);
		assertEquals(v2.compareTo(v1), 0);

		// v2 > v1
		v1 = Version.parse("1");
		v2 = Version.parse("1.0.1");
		assertEquals(v1.compareTo(v2), -1);
		assertEquals(v2.compareTo(v1), 1);

		// SNAPSHOT, equal
		v1 = Version.parse("1.0-SNAPSHOT");
		v2 = Version.parse("1-SNAPSHOT");
		assertEquals(v1.compareTo(v2), 0);
		assertEquals(v2.compareTo(v1), 0);

		// SNAPSHOT, whereby v2 > v1
		v1 = Version.parse("1.0-SNAPSHOT");
		v2 = Version.parse("1.0.0.1-SNAPSHOT");
		assertEquals(v1.compareTo(v2), -1);
		assertEquals(v2.compareTo(v1), 1);

		// v2 SNAPSHOT, whereby v2 > v1
		v1 = Version.parse("1.0");
		v2 = Version.parse("1.0.0.1-SNAPSHOT");
		assertEquals(v1.compareTo(v2), -1);
		assertEquals(v2.compareTo(v1), 1);

		// v2 SNAPSHOT, whereby v2 = v1 => v2 < v1 (because v2 is SNAPSHOT)
		v1 = Version.parse("1.0");
		v2 = Version.parse("1-SNAPSHOT");
		assertEquals(v1.compareTo(v2), 1);
		assertEquals(v2.compareTo(v1), -1);
	}
}
