package com.sgnn7.cpassistant.copyimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.apache.commons.io.FileUtils;

public class FileChannelCopyImplementation implements ICopyImplementation {
	final static int BUFFER_SIZE = (int) Math.pow(2, 16);

	static {
		System.err.println("Buffer size: " + FileUtils.byteCountToDisplaySize(BUFFER_SIZE));
	}

	@Override
	public void copyFile(File in, File out) throws IOException {
		if (!in.exists())
			in.createNewFile();

		FileChannel inChannel = new FileInputStream(in).getChannel();
		FileChannel outChannel = new FileOutputStream(out).getChannel();

		boolean isDone = false;
		long totalBytesTransfered = 0;

		try {
			while (!isDone) {
				totalBytesTransfered += inChannel.transferTo(totalBytesTransfered, BUFFER_SIZE, outChannel);
				isDone = (totalBytesTransfered >= in.length());
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}
}
