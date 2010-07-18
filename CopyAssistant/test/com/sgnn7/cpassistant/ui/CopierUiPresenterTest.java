package com.sgnn7.cpassistant.ui;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class CopierUiPresenterTest {
	private CopierUiPresenter testObject;
	private ICopierUiView view;
	private ICopierUiModel model;

	@Before
	public void setup() {
		model = mock(ICopierUiModel.class);
		view = mock(ICopierUiView.class);
		testObject = new CopierUiPresenter(model, view);
	}

	@Test
	public void createPresenterDoesNotReturnNull() {
		assertTrue(testObject != null);
	}

	@Test
	public void setSourceInPresenterSetsSourceInView() {
		File file = new File(".");

		testObject.setSource(file);

		verify(view, times(1)).setSource(eq(file));
	}
}
