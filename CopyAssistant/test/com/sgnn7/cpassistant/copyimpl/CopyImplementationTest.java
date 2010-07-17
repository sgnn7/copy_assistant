package com.sgnn7.cpassistant.copyimpl;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.sgnn7.cpassistant.CopierTestConstants;
import com.sgnn7.cpassistant.CopierTestUtils;

// Generic copy implementation test

public class CopyImplementationTest {
	ICopyImplementation testObject = new FileChannelCopyImplementation();

	@Test
	public void invokingCopyCopiesFileToTragetExactly() throws Exception {
		CopierTestUtils.cleanTempDirectories();

		File sourceFile = new File(CopierTestUtils.joinPath(CopierTestConstants.SOURCE_STAGING_DIR, "stuff.xml"));
		File destinationFolder = new File(CopierTestUtils.joinPath(CopierTestConstants.TARGET_STAGING_DIR, "data"));
		File destinationFile = new File(destinationFolder, "stuff.xml");

		assertTrue(sourceFile.exists());
		assertFalse(destinationFile.exists());
		destinationFolder.mkdirs();
		destinationFile.createNewFile();
		assertTrue(destinationFile.exists());

		testObject.copyFile(sourceFile, destinationFile);
		assertTrue(FileUtils.contentEquals(sourceFile, destinationFile));
	}

	// This test is meant to be use sparingly to test if the implementation can move large files. Using this
	// test for all test attempts is un-feasible due to long run times.
	@Test
	@Ignore
	public void largeFileCopiesFileToTragetExactly() throws Exception {
		CopierTestUtils.cleanTempDirectories();

		File sourceFile = new File(CopierTestConstants.BIG_FILE_PATH);
		File destinationFolder = new File(CopierTestConstants.TARGET_STAGING_DIR);
		File destinationFile = new File(destinationFolder, CopierTestConstants.BIG_FILE_NAME);

		assertTrue(sourceFile.exists());
		assertFalse(destinationFile.exists());
		destinationFolder.mkdirs();
		destinationFile.createNewFile();
		assertTrue(destinationFile.exists());

		testObject.copyFile(sourceFile, destinationFile);
		assertTrue(FileUtils.contentEquals(sourceFile, destinationFile));
	}
}
