package com.ctx.log.parser.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import com.ctx.log.parser.model.SipParser;

public class MainFrame extends JFrame {
	
private TextPanel textPanel;
private ToolBar toolBar;
private FormPanel formPanel;
private MenuPanel menuBar;
private StatusPanel statusPanel;
private String version = "2.00.10";

private static final long serialVersionUID = 1L;

	
	public MainFrame(SipParser model) throws HeadlessException  {
		
	super("SIP Log Analyzer");
	System.setProperty("apple.laf.useScreenMenuBar", "true");
	System.setProperty("apple.awt.fileDialogForDirectories", "true");	
	
	setLayout(new BorderLayout());
	toolBar = new ToolBar();
	textPanel = new TextPanel();
	formPanel = new FormPanel();
	menuBar = new MenuPanel();
	statusPanel = new StatusPanel();
		
	menuBar.setVersion(version);
	
	toolBar.addParseListener(new ParseListener () {
		public void ParseEventOccurred(ParseEvent event) {
			String text = event.getText();
			statusPanel.appendText(text);
		}
	});
	
	menuBar.addParseListener(new ParseListener () {
		public void ParseEventOccurred(ParseEvent event) {
			String text = event.getText();
			statusPanel.appendText(text);
		}
	});
	
	
	add(formPanel,BorderLayout.WEST);
	add(toolBar,BorderLayout.NORTH);
	add(textPanel,BorderLayout.CENTER);
	add(statusPanel,BorderLayout.SOUTH);
	
	statusPanel.setPreferredSize(new Dimension(getWidth(), 20));
	statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
	
	setJMenuBar(menuBar.getJMenuBar());
	setSize(850,350);
	setLocation(230,200);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setVisible(true);	

	}

	
	public void initMethod(AnalysisListener analysisListener) {
		toolBar.setAnalysisListener(analysisListener);
		menuBar.setAnalysisListener(analysisListener);
	}

	
	public void clearTextPanel() {
		textPanel.clearText();
	}
	
	
	public void printStatusMessage(String string,int errorType) {	
		
		switch (errorType){
		case 0: statusPanel.appendText(string + "\n"); 
				statusPanel.displayOk();
				break;
		case 1: statusPanel.appendText(string + "\n"); 
				statusPanel.displayOk();
				break;
		case 2: statusPanel.appendText(string + "\n");
				statusPanel.displayWarn();
				break;
		case 3: statusPanel.appendText(string + "\n");
				statusPanel.displayError();
				break;
		default: break;
		
		}		
	}


	public void printLineMessage(String string) {
		textPanel.appendText(string + "\n");
	}
	
	public void printMessages(ArrayList<String> sipMessagesText) {
		textPanel.setMessagesArray(sipMessagesText);
		textPanel.displayTextMessages();
	}
	
	public void printCallsNumber(int numCalls) {
		formPanel.setTotalCalls(numCalls);
	}
	
	public void printMessagesNumber(int numMessages) {
		formPanel.setTotalMessages(numMessages);
	}

	
	public void showProgressBar(boolean display) {
		statusPanel.showProgressBar(display);
	}
	
	public void setProgresBarMax(int max) {
		statusPanel.setProgressBarMax(max);
	}
	
	public void setProgresBarIndeterminate() {
		statusPanel.setProgressBarIndeterminate();
	}

}
