package com.sgnn7.cpassistant;

import java.io.File;
import java.io.IOException;

public class FolderOperationHandler {
	public void makeDirectoryIfNotExists(File destinationDirectory) throws IOException {
		if (!destinationDirectory.exists()) {
			destinationDirectory.mkdirs();
		}
	}
}
