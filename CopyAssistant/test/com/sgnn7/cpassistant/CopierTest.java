package com.sgnn7.cpassistant;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CopierTest {
	private static final String TARGET_STAGING_DIR = "test-data";
	private static final String SOURCE_STAGING_DIR = "data";
	private Copier testObject;
	private MockCopier mockTestObject;

	@Before
	public void setup() throws IOException {
		cleanTempDirectories();

		testObject = new Copier();
		testObject.setSources(Arrays.asList(SOURCE_STAGING_DIR));
		testObject.setOverwriteFilesEnabled(true);

		mockTestObject = new MockCopier();
		mockTestObject.setSources(Arrays.asList(SOURCE_STAGING_DIR));
		mockTestObject.setOverwriteFilesEnabled(true);
	}

	@After
	public void clean() throws IOException {
		cleanTempDirectories();
	}

	@Test
	public void folderThatIsAddedGetsExpandedToIncludeAllFiles() {
		List<String> fileObjects = testObject.getFilesFromSources();
		assertEquals(8, fileObjects.size());
	}

	@Test
	public void invokingCopyCopiesAllFilesToTarget() throws Exception {
		testObject.setDestination(TARGET_STAGING_DIR);
		List<String> result = testObject.beginCopying();
		assertEquals(0, result.size());

		testObject.setSources(Arrays.asList(TARGET_STAGING_DIR));
		assertEquals(9, getFileItemsInTargetDirectory());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void allFilesAreCopiedToTargetExactly() throws Exception {
		testObject.setDestination(TARGET_STAGING_DIR);
		List<String> result = testObject.beginCopying();
		assertEquals(0, result.size());

		File[] sourceFiles = (File[]) FileUtils.listFiles(new File(SOURCE_STAGING_DIR), null, true)
				.toArray(new File[0]);
		File[] targetFiles = (File[]) FileUtils.listFiles(new File(TARGET_STAGING_DIR), null, true)
				.toArray(new File[0]);
		for (int i = 0; i < sourceFiles.length; i++) {
			assertTrue(FileUtils.contentEquals(sourceFiles[i], targetFiles[i]));
		}
	}

	@Test
	public void invokingCopyWithABadFileCopiesAllFilesToTargetAndReturnsFailedOnes() throws Exception {
		mockTestObject.setDestination(TARGET_STAGING_DIR);

		IOException exception = new IOException("stuffs");
		mockTestObject.failOnFile(Arrays.asList("data\\blah\\abc.txt", "data\\test"), exception);

		List<String> result = mockTestObject.beginCopying();
		assertEquals(2, result.size());

		assertEquals(7, getFileItemsInTargetDirectory());
	}

	@Test
	public void invokingCopyWithABadFolderDoesNotTryToCopyFilesIntoThatFolder() throws Exception {
		mockTestObject.setDestination(TARGET_STAGING_DIR);

		IOException exception = new IOException("stuffs");
		mockTestObject.failOnFile(Arrays.asList("data\\blah"), exception);
		mockTestObject.assertCopyIsNotInvokedOnPaths(Arrays.asList("data\\blah\\abc.txt"));

		List<String> result = mockTestObject.beginCopying();
		assertEquals(2, result.size());

		assertEquals(7, getFileItemsInTargetDirectory());
	}

	@Test
	public void invokingCopyWithTooSmallDestinationThrowsRuntimeException() throws Exception {
		mockTestObject.setDestination(TARGET_STAGING_DIR);
		mockTestObject.setCanHoldAllFiles(false);

		try {
			mockTestObject.beginCopying();
		} catch (RuntimeException e) {
			// Pass
		} catch (Exception e) {
			fail("Should have thrown runtime exception");
		}
	}

	private int getFileItemsInTargetDirectory() {
		testObject.setSources(Arrays.asList(TARGET_STAGING_DIR));
		return testObject.getFilesFromSources().size();
	}

	// @Test
	// public void copyMyFiles() throws Exception {
	// testObject.setSources(Arrays.asList("D:\\mp3"));
	// String testOutputDirectory = "Z:\\";
	// testObject.setDestination(testOutputDirectory);
	// boolean result = testObject.beginCopying(true);
	// try {
	// assertTrue(result);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	private class MockCopier extends Copier {
		private List<String> failureFilenames;
		private IOException failureException;
		private List<String> assertionFiles;
		private boolean canHoldAllFiles = true;

		void failOnFile(List<String> filenames, IOException exception) {
			this.failureFilenames = filenames;
			this.failureException = exception;
		}

		void assertCopyIsNotInvokedOnPaths(List<String> assertionFiles) {
			this.assertionFiles = assertionFiles;
		}

		@Override
		void copyFile(File in, File out) throws IOException {
			if (assertionFiles != null) {
				for (String assertionFilename : assertionFiles) {
					if (in.getPath().endsWith(assertionFilename)) {
						fail("File '" + assertionFilename + "' should not have been copied");
					}
				}
			}

			if (failureFilenames.contains(in.getPath())) {
				throw failureException;
			}
			super.copyFile(in, out);
		}

		@Override
		void makeDirectoryIfNotExists(String destinationDirectory) throws IOException {
			for (String failureFilename : failureFilenames) {
				if (destinationDirectory.endsWith(failureFilename)) {
					throw failureException;
				}
			}
			super.makeDirectoryIfNotExists(destinationDirectory);
		}

		@Override
		boolean targetCanHoldAllFiles(String destinationPath, long sourcesSize) {
			return canHoldAllFiles;
		}

		public void setCanHoldAllFiles(boolean canHoldAllFiles) {
			this.canHoldAllFiles = canHoldAllFiles;
		}
	}

	private void cleanTempDirectories() throws IOException {
		File stagingDir = new File(TARGET_STAGING_DIR);
		FileUtils.deleteDirectory(stagingDir);
	}
}
