package com.sgnn7.cpassistant;

import static org.junit.Assert.*;

import java.io.File;
import java.util.UUID;

import org.junit.Test;

public class FolderOperationHandlerTest {
	@Test
	public void foldersAreCreatedAsRequested() throws Exception {
		CopierTestUtils.cleanTempDirectories();

		FolderOperationHandler testObject = new FolderOperationHandler();

		String folderName = CopierTestUtils.joinPath(CopierTestConstants.TARGET_STAGING_DIR, UUID.randomUUID()
				.toString());
		File folder = new File(folderName);
		testObject.makeDirectoryIfNotExists(folder);
		assertTrue(folder.exists());
		assertTrue(folder.isDirectory());

		CopierTestUtils.deleteDirectory(folder);
	}
}
