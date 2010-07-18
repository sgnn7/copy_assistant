package com.sgnn7.cpassistant.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentListener;

import com.sgnn7.org.cpassistant.app.CopierApp;

public class CopierUiView implements ICopierUiView {

	private JTextField sourceFile;
	private JTextField destinationFile;
	private ActionListener startCopyingListener;

	@Override
	public void createUi() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("Copy Assistant v" + CopierApp.VERSION);
		setFrameParameters(frame);

		addComponentsToFrame(frame);

		frame.pack();
		frame.setVisible(true);
	}

	private void setFrameParameters(JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension minimumSize = new Dimension(550, 150);
		frame.setMinimumSize(minimumSize);
		frame.setPreferredSize(minimumSize);
		frame.setResizable(false);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(screenSize.width / 2 - frame.getWidth() / 2, screenSize.height / 2 - frame.getHeight() / 2);

		GridBagLayout gridBagLayout = new GridBagLayout();
		frame.setLayout(gridBagLayout);
	}

	private void addComponentsToFrame(JFrame frame) {
		addLabel(frame, 1, 1, "Source: ");
		addLabel(frame, 1, 2, "Destination: ");
		sourceFile = addText(frame, 2, 1, "None");
		destinationFile = addText(frame, 2, 2, "None");
		addFileBrowseButton(frame, sourceFile, 3, 1, "Browse", false);
		addFileBrowseButton(frame, destinationFile, 3, 2, "Browse", true);
		addProgressBar(frame, 1, 3, "Copy progress", "Double-click to start copy");
	}

	private JButton addFileBrowseButton(final JFrame frame, final JTextField textFieldToUpdate, int x, int y,
			String text, final boolean directoriesOnly) {
		GridBagConstraints c = setPosition(x, y);
		c.weightx = 1;
		JButton button = new JButton(text);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				openFileDialog(frame, textFieldToUpdate, directoriesOnly);
			}

			private void openFileDialog(final JFrame frame, final JTextField textFieldToUpdate,
					final boolean directoriesOnly) {
				final JFileChooser fc = new JFileChooser();
				if (directoriesOnly)
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fc.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION && fc.getSelectedFile() != null) {
					textFieldToUpdate.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		frame.getContentPane().add(button, c);

		return button;
	}

	private void addLabel(JFrame frame, int x, int y, String text) {
		GridBagConstraints c = setPosition(x, y);
		c.weightx = 1;
		c.insets.right = 0;
		JLabel label = new JLabel(text);

		// TODO: Debug statements. Remove when done
		// label.setBackground(Color.LIGHT_GRAY);
		// label.setOpaque(true);

		frame.getContentPane().add(label, c);
	}

	private JTextField addText(JFrame frame, int x, int y, String text) {
		GridBagConstraints c = setPosition(x, y);
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 15;
		c.insets = new Insets(8, 0, 8, 0);
		JTextField textField = new JTextField(text);
		textField.setEditable(false);
		frame.add(textField, c);
		return textField;
	}

	private void addProgressBar(JFrame frame, int x, int y, String tooltipText, String caption) {
		GridBagConstraints c = setPosition(x, y);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.8;

		JProgressBar progressBar = new JProgressBar(0, 1);
		progressBar.setDoubleBuffered(true);
		progressBar.setName(tooltipText);
		progressBar.setString(caption);
		progressBar.setStringPainted(true);
		progressBar.setToolTipText(tooltipText);

		progressBar.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (arg0.getClickCount() >= 2) {
					startCopyingListener.actionPerformed(null);
				}
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});

		frame.add(progressBar, c);
	}

	private GridBagConstraints setPosition(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = x;
		c.gridy = y;
		c.insets = new Insets(8, 12, 8, 12);
		c.weighty = 1;
		return c;
	}

	@Override
	public void setSource(File file) {
		sourceFile.setText(file.getAbsolutePath());
	}

	@Override
	public void setDestination(File file) {
		destinationFile.setText(file.getAbsolutePath());
	}

	@Override
	public File getSource() {
		return new File(sourceFile.getText());
	}

	@Override
	public void addSourceChangedListener(DocumentListener listener) {
		sourceFile.getDocument().addDocumentListener(listener);
	}

	@Override
	public void addDestinationChangedListener(DocumentListener listener) {
		destinationFile.getDocument().addDocumentListener(listener);
	}

	@Override
	public File getDestination() {
		return new File(destinationFile.getText());
	}

	@Override
	public void addOperationStartedListener(ActionListener listener) {
		this.startCopyingListener = listener;

	}
}
