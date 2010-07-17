package com.sgnn7.cpassistant;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class FileFinderTest {
	private FileFinder testObject;

	@Test
	public void folderThatIsAddedGetsExpandedToIncludeAllFiles() {
		testObject = new FileFinder();
		List<String> fileObjects = testObject.getFilesFromSources(CopierTestConstants.SOURCE_STAGING_DIR);
		assertEquals(8, fileObjects.size());
	}
}
