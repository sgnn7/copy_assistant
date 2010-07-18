package com.sgnn7.cpassistant.ui;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.event.DocumentListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CopierUiPresenterTest {
	private CopierUiPresenter testObject;
	private ICopierUiView view;
	private ICopierUiModel model;
	private File file;
	private File file2;

	@Before
	public void setup() {
		model = mock(ICopierUiModel.class);
		view = mock(ICopierUiView.class);
		testObject = new CopierUiPresenter(model, view);
		file = new File(".");
		file2 = new File(".");
	}

	@Test
	public void createPresenterDoesNotReturnNull() {
		assertTrue(testObject != null);
	}

	@Test
	public void setSourceSetsSourceInView() {
		testObject.setSource(file);

		verify(view, times(1)).setSource(eq(file));
	}

	@Test
	public void setDestinationSetsDestinationInView() {
		testObject.setDestination(file);

		verify(view, times(1)).setDestination(eq(file));
	}

	@Test
	public void whenViewSetsSourcePathTheModelIsNotified() {
		ArgumentCaptor<DocumentListener> listener = ArgumentCaptor.forClass(DocumentListener.class);
		doReturn(file).when(view).getSource();
		testObject.show();
		verify(view).addSourceChangedListener(listener.capture());

		listener.getValue().changedUpdate(null);

		verify(model, times(1)).setSource(file);
	}

	@Test
	public void whenViewSetsDestinationPathTheModelIsNotified() {
		ArgumentCaptor<DocumentListener> listener = ArgumentCaptor.forClass(DocumentListener.class);
		doReturn(file).when(view).getDestination();
		testObject.show();
		verify(view).addDestinationChangedListener(listener.capture());

		listener.getValue().changedUpdate(null);

		verify(model, times(1)).setDestination(file);
	}

	@Test
	public void whenViewStartsCopyTheModelIsNotified() {
		ArgumentCaptor<ActionListener> listener = ArgumentCaptor.forClass(ActionListener.class);

		testObject.show();
		verify(view).addOperationStartedListener(listener.capture());

		listener.getValue().actionPerformed(null);

		verify(model, times(1)).startCopy();
	}
}
