package com.sgnn7.cpassistant.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.sgnn7.org.cpassistant.app.CopierApp;

public class CopierUiView implements ICopierUiView {

	@Override
	public void show() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
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

				Dimension minimumSize = new Dimension(400, 150);
				frame.setMinimumSize(minimumSize);
				frame.setPreferredSize(minimumSize);
				frame.setResizable(false);

				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setLocation(screenSize.width / 2 - frame.getWidth() / 2, screenSize.height / 2
						- frame.getHeight() / 2);

				GridBagLayout gridBagLayout = new GridBagLayout();
				frame.setLayout(gridBagLayout);
			}

			private void addComponentsToFrame(JFrame frame) {
				addLabel(frame, 1, 1, "Source: ");
				addLabel(frame, 1, 2, "Destination: ");
				addText(frame, 2, 1, "None");
				addText(frame, 2, 2, "None");
				addBrowseButton(frame, 3, 1, "Browse");
				addBrowseButton(frame, 3, 2, "Browse");
				addProgressBar(frame, 1, 3, "Copy progress", "Click to start copy");
			}

			private void addBrowseButton(final JFrame frame, int x, int y, String text) {
				GridBagConstraints c = setPosition(x, y);
				JButton button = new JButton(text);
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						final JFileChooser fc = new JFileChooser();
						fc.showOpenDialog(frame);
					}
				});
				frame.getContentPane().add(button, c);
			}

			private void addLabel(JFrame frame, int x, int y, String text) {
				GridBagConstraints c = setPosition(x, y);
				c.weightx = 1;
				JLabel label = new JLabel(text);

				// TODO: Debug statements. Remove when done
				label.setBackground(Color.LIGHT_GRAY);
				label.setOpaque(true);

				frame.getContentPane().add(label, c);
			}

			private void addText(JFrame frame, int x, int y, String text) {
				GridBagConstraints c = setPosition(x, y);
				c.anchor = GridBagConstraints.WEST;
				c.weightx = 5;
				JTextField textField = new JTextField(text);
				frame.add(textField, c);
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
				// progressBar.setValue(0);
				progressBar.setStringPainted(true);
				progressBar.setToolTipText(tooltipText);
				// progressBar.addMouseListener(new MouseListener)

				frame.add(progressBar, c);
			}

			private GridBagConstraints setPosition(int x, int y) {
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;
				c.anchor = GridBagConstraints.CENTER;
				c.gridx = x;
				c.gridy = y;
				c.insets = new Insets(8, 12, 8, 12);
				c.weightx = 1;
				c.weighty = 1;
				return c;
			}
		});
	}

	@Override
	public void setSource(File file) {

	}
}
