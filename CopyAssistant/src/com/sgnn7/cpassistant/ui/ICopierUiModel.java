package com.sgnn7.cpassistant.ui;

import java.io.File;

public interface ICopierUiModel {
	void setSource(File file);

	void setDestination(File file);

	void startCopy();
}