package com.sgnn7.cpassistant.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CopierUiPresenter {

	private final ICopierUiView view;
	private final ICopierUiModel model;

	public CopierUiPresenter(ICopierUiModel model, ICopierUiView view) {
		this.model = model;
		this.view = view;
	}

	public void setSource(File file) {
		view.setSource(file);
	}

	public void show() {
		view.createUi();
		view.addSourceChangedListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				model.setSource(view.getSource());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				model.setSource(view.getSource());
			}
		});

		view.addDestinationChangedListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				model.setDestination(view.getDestination());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				model.setDestination(view.getDestination());
			}
		});

		view.addOperationStartedListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.startCopy();
			}
		});
	}

	public void setDestination(File file) {
		view.setDestination(file);
	}
}
