package com.sgnn7.cpassistant;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class VolumeInfoProviderTest {
	VolumeInfoProvider testObject = new VolumeInfoProvider();

	@Test
	public void ifFileSizesAreSmallerThanVolumeAvailableSizeThenReturnTrue() {
		File file = new File(".");
		long usableSpace = file.getUsableSpace();
		assertTrue(testObject.targetCanHoldAllFiles(file, usableSpace - 2));
	}

	@Test
	public void ifFileSizesAreLargerThanVolumeAvailableSizeThenReturnFalse() {
		File file = new File(".");
		long usableSpace = file.getUsableSpace();
		assertFalse(testObject.targetCanHoldAllFiles(file, usableSpace + 2));
	}
}
