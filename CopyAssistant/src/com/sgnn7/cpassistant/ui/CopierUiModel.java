package com.sgnn7.cpassistant.ui;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.sgnn7.cpassistant.Copier;
import com.sgnn7.cpassistant.FolderOperationHandler;
import com.sgnn7.cpassistant.VolumeInfoProvider;
import com.sgnn7.cpassistant.copyimpl.FileChannelCopyImplementation;

public class CopierUiModel implements ICopierUiModel {

	private File source;
	private File destination;

	@Override
	public void setSource(File file) {
		this.source = file;
		System.err.println("Source: " + file.getAbsolutePath());
	}

	@Override
	public void setDestination(File file) {
		this.destination = file;
		System.err.println("Destination: " + file.getAbsolutePath());
	}

	public void startCopy() {
		Copier copier = new Copier(new FileChannelCopyImplementation(), new FolderOperationHandler(),
				new VolumeInfoProvider());

		copier.setSources(Arrays.asList(source.getAbsolutePath()));
		copier.setDestination(destination.getAbsolutePath());

		try {
			copier.beginCopying();
		} catch (IOException e) {
			// TODO: do something
			e.printStackTrace();
		}
	}
}
