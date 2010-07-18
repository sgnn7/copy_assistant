package com.sgnn7.cpassistant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileFinder {
	// Strings used for performance reasons
	public List<String> getFilesFromSources(String... locations) {
		List<String> fileList = new ArrayList<String>(100);
		for (String source : locations) {
			fileList.addAll(getDirectoryTree(source));
		}
		return fileList;
	}

	private List<String> getDirectoryTree(String source) {
		List<String> fileList = new ArrayList<String>();
		fileList.add(source);

		File sourceFile = new File(source);

		if (!sourceFile.isFile()) {
			for (File currentSource : sourceFile.listFiles()) {
				if (currentSource.isFile()) {
					fileList.add(currentSource.getPath());
				} else if (currentSource.isDirectory()) {
					List<String> directoryTree = getDirectoryTree(currentSource.getPath());
					fileList.addAll(directoryTree);
				}
			}
		}
		return fileList;
	}
}
