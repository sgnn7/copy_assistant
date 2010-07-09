package com.sgnn7.cpassistant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Copier {
	private List<String> sources;
	private String destination;
	private boolean overwriteFilesEnabled;

	public void setSources(List<String> sources) {
		this.sources = sources;
	}

	public List<String> getFilesFromSources() {
		List<String> fileList = new ArrayList<String>(100);
		for (String source : sources) {
			fileList.addAll(getDirectoryTree(source));
		}
		return fileList;
	}

	private List<String> getDirectoryTree(String source) {
		List<String> fileList = new ArrayList<String>();
		fileList.add(source);

		for (File currentSource : new File(source).listFiles()) {
			if (currentSource.isFile()) {
				fileList.add(currentSource.getPath());
			} else if (currentSource.isDirectory()) {
				List<String> directoryTree = getDirectoryTree(currentSource.getPath());
				fileList.addAll(directoryTree);
			}
		}
		return fileList;
	}

	public List<String> beginCopying() throws IOException {
		int filesDone = 0;
		if (sources.isEmpty() || destination == null || destination.isEmpty()) {
			throw new RuntimeException("Invalid parameters set");
		}

		makeDirectoryIfNotExists(destination);

		List<String> sourceFilenames = getFilesFromSources();
		List<String> failedFiles = new ArrayList<String>();

		long startingTime = System.currentTimeMillis();
		int totalFiles = sourceFilenames.size();
		BigDecimal totalFileSize = getFileSizes(sourceFilenames);

		if (!targetCanHoldAllFiles(destination, totalFileSize.longValue())) {
			throw new RuntimeException("Target too small");
		}

		System.out.println("Total size: " + FileUtils.byteCountToDisplaySize(totalFileSize.longValue()));

		for (String sourceFilename : sourceFilenames) {
			File sourceFile = new File(sourceFilename);

			// XXX: could be better handled
			String path = sourceFile.getPath();
			if (path.charAt(1) == ':') {
				path = path.substring(3);
			}

			File targetFile = new File(destination, path);
			if (!sourceFile.exists() || overwriteFilesEnabled) {
				try {
					if (sourceFile.isFile() && sourceFileIsNotInAFailedFolder(sourceFile, failedFiles)) {
						copyFile(sourceFile, targetFile);
					} else if (sourceFile.isDirectory()) {
						makeDirectoryIfNotExists(new File(destination, path).getPath());
					}
				} catch (IOException e) {
					// e.printStackTrace();
					failedFiles.add(sourceFile.getAbsolutePath());
					continue;
				}
			}
			filesDone++;
			printDebugInfo(startingTime, filesDone, totalFiles);
		}
		return failedFiles;
	}

	private boolean sourceFileIsNotInAFailedFolder(File sourceFile, List<String> failedFiles) throws IOException {
		boolean isNotInFailedFolder = true;
		if (failedFiles.contains(sourceFile.getParentFile().getAbsolutePath())) {
			isNotInFailedFolder = false;

			// throw new IOException("File '" + sourceFile.getPath() + "' belongs to a folder that failed to copy");
			failedFiles.add(sourceFile.getAbsolutePath());
		}
		return isNotInFailedFolder;
	}

	private void printDebugInfo(long startingTime, int filesDone, int totalFiles) {
		double percentageDone = filesDone / (double) totalFiles;
		double timeTaken = (System.currentTimeMillis() - startingTime) / 1000;
		double timePerPercentage = timeTaken / percentageDone;
		double remainingTime = timePerPercentage * (1 - percentageDone);

		String output = String.format("%.2f complete. Time: %1dm %02.0fs. Remaining: %1dm %02.0fs.",
				percentageDone * 100, (int) timeTaken / 60, timeTaken % 60, (int) remainingTime / 60,
				remainingTime % 60);
		System.out.println(output);
	}

	private BigDecimal getFileSizes(List<String> sourceFilenames) {
		BigDecimal totalSize = new BigDecimal(0);
		for (String sourceFilename : sourceFilenames) {
			File sourceFile = new File(sourceFilename);
			if (sourceFile.isFile()) {
				totalSize = totalSize.add(BigDecimal.valueOf(sourceFile.length()));
			}
		}
		return totalSize;
	}

	void makeDirectoryIfNotExists(String destinationDirectory) throws IOException {
		File directory = new File(destinationDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	void copyFile(File in, File out) throws IOException {
		if (!in.exists())
			in.createNewFile();

		FileChannel inChannel = new FileInputStream(in).getChannel();
		FileChannel outChannel = new FileOutputStream(out).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			throw e;
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}

	boolean targetCanHoldAllFiles(String destinationPath, long sourcesSize) {
		File target = new File(destinationPath);
		long usableSpace = target.getUsableSpace();
		return usableSpace >= sourcesSize;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public boolean isOverwriteFilesEnabled() {
		return overwriteFilesEnabled;
	}

	public void setOverwriteFilesEnabled(boolean overwriteFilesEnabled) {
		this.overwriteFilesEnabled = overwriteFilesEnabled;
	}

}