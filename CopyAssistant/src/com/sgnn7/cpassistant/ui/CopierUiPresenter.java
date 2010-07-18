package com.sgnn7.cpassistant.ui;

import java.io.File;

public class CopierUiPresenter {

	private final ICopierUiView view;

	public CopierUiPresenter(ICopierUiModel model, ICopierUiView view) {
		this.view = view;
	}

	public void setSource(File file) {
		view.setSource(file);
	}

	public void show() {
		view.show();

	}
}
