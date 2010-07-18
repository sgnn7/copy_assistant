package com.sgnn7.cpassistant.ui;

import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.event.DocumentListener;

public interface ICopierUiView {
	public void setSource(File file);

	public void setDestination(File file);

	public void createUi();

	public File getSource();

	public File getDestination();

	public void addDestinationChangedListener(DocumentListener listener);

	public void addSourceChangedListener(DocumentListener listener);

	public void addOperationStartedListener(ActionListener listener);
}
