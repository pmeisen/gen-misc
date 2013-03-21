package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import net.meisen.general.genmisc.resources.Resource;
import net.meisen.general.genmisc.types.Strings;

/**
 * Tests used to test the implemented functionality of the {@link Resource}
 * class.
 * 
 * @author pmeisen
 * 
 */
public class TestStrings {

	/**
	 * Test the chunking functionality
	 * 
	 * @see Strings#chunk(String, int)
	 */
	@Test
	public void testChunk() {
		String s = null;
		List<String> chunks = null;

		// Test the empty chunking
		s = "";
		assertEquals(0, Strings.chunk(s, 1).size());

		// Test the chunking of the exact size of the input
		s = "abc";
		chunks = Strings.chunk(s, s.length());
		assertEquals(s, chunks.get(0));
		assertEquals(1, chunks.size());

		// Tests chunking if the first is smaller than the size
		s = "abc";
		chunks = Strings.chunk(s, s.length() + 1);
		assertEquals(s, chunks.get(0));
		assertEquals(1, chunks.size());

		// Tests real chunking, i.e. a string will be partitioned
		s = "abc";
		chunks = Strings.chunk(s, 2);
		assertEquals("ab", chunks.get(0));
		assertEquals("c", chunks.get(1));
		assertEquals(2, chunks.size());

		// Tests chunking when two chunks fit completely
		s = "abcd";
		chunks = Strings.chunk(s, 2);
		assertEquals("ab", chunks.get(0));
		assertEquals("cd", chunks.get(1));
	}

	/**
	 * Test the concatenation functionality
	 * 
	 * @see Strings#concate(String, Collection)
	 * @see Strings#concate(String, Object...)
	 */
	@Test
	public void testConcate() {
		String s = null;

		s = Strings.concate("-");
		assertEquals(s, "");
		
		// concatenate some integer
		s = Strings.concate("-", 1, 2, 3, 4);
		assertEquals(s, "1-2-3-4");
		
		// concatenate some Strings
		s = Strings.concate(";", "A", "B");
		assertEquals(s, "A;B");
	}
}
