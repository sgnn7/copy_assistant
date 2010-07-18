package com.sgnn7.org.cpassistant.app;

import com.sgnn7.cpassistant.ui.CopierUiModel;
import com.sgnn7.cpassistant.ui.CopierUiPresenter;
import com.sgnn7.cpassistant.ui.CopierUiView;
import com.sgnn7.cpassistant.ui.ICopierUiModel;
import com.sgnn7.cpassistant.ui.ICopierUiView;

public class CopierApp {
	public static final String VERSION = "0.1";

	public static void main(String[] args) {
		ICopierUiView view = new CopierUiView();
		ICopierUiModel model = new CopierUiModel();
		final CopierUiPresenter presenter = new CopierUiPresenter(model, view);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				presenter.show();
			}
		});
	}
}
