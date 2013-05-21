package com.ctx.log.parser.view;


import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class StatusPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;
	private JLabel statusLabel;
	private Icon logoGreen;
	private Icon logoYellow;
	private Icon logoRed;
	
	
	public StatusPanel() {
		
	
		logoGreen = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("images/green_icon.png")).getImage());
		logoYellow = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("images/yellow_icon.png")).getImage());
		logoRed = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("images/red_icon.jpg")).getImage());
		
		
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusLabel = new JLabel("Application started!",logoGreen,JLabel.CENTER);	
		Dimension size = statusLabel.getPreferredSize();
		size.width= 300;
	
		progressBar = new JProgressBar();	
		progressBar.setPreferredSize(size);
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		add(statusLabel);
		add(progressBar);
		
		showProgressBar(false);
		
	
		
	}
	
	public void setProgressBarMax(int max) {
		progressBar.setMaximum(max);
	}

	
	public void showProgressBar(boolean display) {
		if (display){
			progressBar.setVisible(true);
		}
		else {
			progressBar.setVisible(false);
		}
	}

	public void setProgressBarIndeterminate() {
		progressBar.setIndeterminate(true);
	}
	
	public void appendText(String text) {
		statusLabel.setText(text);		
	}
	
	
	public void displayOk() {
		statusLabel.setIcon(logoGreen);
	}
	
	public void displayWarn() {
		statusLabel.setIcon(logoYellow);
	}
	
	public void displayError() {
		statusLabel.setIcon(logoRed);
	}
	
}
