package com.sgnn7.cpassistant;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sgnn7.cpassistant.copyimpl.ICopyImplementation;

public class CopierTest {
	private Copier testObject;

	@Mock
	private ICopyImplementation mockCopyImpl;

	@Mock
	private VolumeInfoProvider mockVolumeInfoProvider;
	@Mock
	private FolderOperationHandler mockFolderOperationHandler;

	@Before
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);

		CopierTestUtils.cleanTempDirectories();

		testObject = new Copier(mockCopyImpl, mockFolderOperationHandler, mockVolumeInfoProvider);
		testObject.setSources(Arrays.asList(CopierTestConstants.SOURCE_STAGING_DIR));
		testObject.setOverwriteFilesEnabled(true);

		doReturn(true).when(mockVolumeInfoProvider).targetCanHoldAllFiles(any(File.class), anyLong());
	}

	@After
	public void clean() throws IOException {
		CopierTestUtils.cleanTempDirectories();
	}

	@Test
	public void invokingCopyWithABadFileCopiesAllFilesToTargetAndReturnsFailedOnes() throws Exception {
		testObject.setDestination(CopierTestConstants.TARGET_STAGING_DIR);

		List<String> exceptionThrowingList = Arrays.asList(CopierTestUtils.joinPath("data", "blah", "abc.txt"),
				CopierTestUtils.joinPath(CopierTestConstants.TARGET_STAGING_DIR, "data", "test"));
		doThrow(new IOException()).when(mockCopyImpl).copyFile(argThat(new FileMatcher(exceptionThrowingList)),
				any(File.class));
		doThrow(new IOException()).when(mockFolderOperationHandler).makeDirectoryIfNotExists(
				argThat(new FileMatcher(exceptionThrowingList)));

		List<String> result = testObject.beginCopying();
		assertEquals(2, result.size());
	}

	@Test
	public void invokingCopyWithABadFolderDoesNotTryToCopyFilesIntoThatFolder() throws Exception {
		testObject.setDestination(CopierTestConstants.TARGET_STAGING_DIR);

		List<String> exceptionThrowingList = Arrays.asList(CopierTestUtils.joinPath(
				CopierTestConstants.TARGET_STAGING_DIR, "data", "blah"));
		List<String> assertionList = Arrays.asList(CopierTestUtils.joinPath("data", "blah", "abc.txt"));

		doThrow(new IOException()).when(mockFolderOperationHandler).makeDirectoryIfNotExists(
				argThat(new FileMatcher(exceptionThrowingList)));
		verify(mockCopyImpl, never()).copyFile(argThat(new FileMatcher(assertionList)), any(File.class));

		List<String> result = testObject.beginCopying();
		assertEquals(2, result.size());
	}

	@Test
	public void invokingCopyWithTooSmallDestinationThrowsRuntimeException() throws Exception {
		testObject.setDestination(CopierTestConstants.TARGET_STAGING_DIR);

		doReturn(false).when(mockVolumeInfoProvider).targetCanHoldAllFiles(any(File.class), anyLong());

		try {
			testObject.beginCopying();
		} catch (RuntimeException e) {
			// Pass
		} catch (Exception e) {
			fail("Should have thrown runtime exception");
		}
	}

	// @Test
	// public void copyMyFiles() throws Exception {
	// testObject.setSources(Arrays.asList("D:\\"));
	// String testOutputDirectory = "Z:\\";
	// testObject.setDestination(testOutputDirectory);
	// List<String> result = testObject.beginCopying();
	//
	// for (String file : result) {
	// System.err.println("Failed: " + file);
	// }
	// }

	private class FileMatcher extends BaseMatcher<File> {
		private final List<String> exceptions;

		public FileMatcher(List<String> exceptions) {
			this.exceptions = exceptions;
		}

		@Override
		public boolean matches(Object object) {
			if (!(object instanceof File))
				return false;
			File testFile = (File) object;

			// System.err.println("Path: " + testFile.getPath());
			for (String filename : exceptions) {
				// System.err.println(" Checking: " + filename);
				if (filename.equals(testFile.getPath())) {
					return true;
				}
			}

			return false;
		}

		@Override
		public void describeTo(Description arg0) {
		}
	}
}
