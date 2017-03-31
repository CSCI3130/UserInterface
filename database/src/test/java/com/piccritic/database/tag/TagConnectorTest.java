/**
 * @author damienr74, ian-dawson
 */

package com.piccritic.database.tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TagConnectorTest {
	String dog_tag = "dog";
	String bad_tag = "123";
	
	TagConnector tc = new JPATagConnector();
	
	@Before
	public void init() {
	
	}
	
	@Test
	public void TestDogTag() throws TagException {
		assertTrue(tc.findTags("").isEmpty());
		assertTrue(tc.findTags("dog").isEmpty());
		Tag t = new Tag();
		t.setTag(dog_tag);
		tc.insertTag(t);
		assertEquals(1, tc.findTags("").size());
		assertEquals(1, tc.findTags("dog").size());
		tc.deleteTag(t);
		assertTrue(tc.findTags("dog").isEmpty());
	}
	
	@Test(expected = TagException.class)
	public void invalidTagTest() throws TagException {
		Tag t = new Tag();
		t.setTag(bad_tag);
		tc.insertTag(t);
	}
	
	@After
	public void teardown() {
		
	}
	
}
