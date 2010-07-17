package com.sgnn7.cpassistant;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class CopierTestUtils {
	public static void cleanTempDirectories() throws IOException {
		File stagingDir = new File(CopierTestConstants.TARGET_STAGING_DIR);
		FileUtils.deleteDirectory(stagingDir);
	}

	public static String joinPath(String... args) {
		return StringUtils.join(args, File.separator);
	}

	public static void deleteDirectory(File directory) throws IOException {
		FileUtils.deleteDirectory(directory);
	}

}
