package com.sgnn7.cpassistant;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class VolumeInfoProvider {
	public boolean targetCanHoldAllFiles(File target, long sourcesSize) {
		long usableSpace = target.getUsableSpace();
		boolean volumeCanHoldFiles = usableSpace >= sourcesSize;
		if (!volumeCanHoldFiles) {
			System.err.println("Volume cannot hold all files!");
			System.err.println("Volume space: " + FileUtils.byteCountToDisplaySize(usableSpace));
			System.err.println("Space needed: " + FileUtils.byteCountToDisplaySize(sourcesSize));
		}

		return volumeCanHoldFiles;
	}
}
