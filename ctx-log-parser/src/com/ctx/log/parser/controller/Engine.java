package com.ctx.log.parser.controller;

import javax.swing.SwingUtilities;
import com.ctx.log.parser.model.DetectFileType;
import com.ctx.log.parser.model.GenerateSipFile;
import com.ctx.log.parser.model.SipParser;
import com.ctx.log.parser.view.AnalysisFormEvent;
import com.ctx.log.parser.view.AnalysisListener;
import com.ctx.log.parser.view.MainFrame;


public class Engine implements AnalysisListener{
	
	private SipParser model;
	private MainFrame ui;
	private DetectFileType id;
	private String fileType;
	private GenerateSipFile newDiagramFile;
	
	public Engine(SipParser model, MainFrame view) {
		
		this.fileType = "";
		this.model = model;
		this.ui = view;
		this.id = new DetectFileType();
		this.newDiagramFile = new GenerateSipFile();
		
	}

	
	/**
	 * 
	 * 
	 */
	
	public void sendEventLogAnalysis(AnalysisFormEvent event) {
		
		
		if (event.getCommand().equals("LOAD_FILE")) {
	
			id.setEngineFile(event.getFileName());
						
			if(!id.isFileProcessed()) { 
				String logType = id.DetectLogType();
				if (logType.contains("CISCO_CTX")) {				
					model.setEngineFile(event.getFileName());
					logType = "Cisco CTX log detected.";
					ui.printStatusMessage(logType + " FileName: " + event.getFileName(),1);
					fileType = "CISCO_CTX";
				}
				else if (logType.contains("CISCO_CUCM")) {
					model.setEngineFile(event.getFileName());
					logType = "Cisco CUCM log detected.";
					ui.printStatusMessage(logType + " FileName: " + event.getFileName(),1);
					fileType = "CISCO_CUCM";
				}
				else if (logType.contains("MOBICENTS")) {
					model.setEngineFile(event.getFileName());
					logType = "Telestax Mobicents log detected.";
					ui.printStatusMessage(logType + " FileName: " + event.getFileName(),1);
					fileType = "MOBICENTS";
				}
				else if (logType.contains("ASTERISK")) {
					logType = "Asterisk log detected.";
					ui.printStatusMessage(logType + " FileName: " + event.getFileName(),1);
					fileType = "ASTERISK";
				}
				else {
					logType = "Unknown Log detected.";
					ui.printStatusMessage(logType + " FileName: " + event.getFileName(),3);
					fileType = "UNKNOWN";
				}
				
			}
			
		}
		
		else if (event.getCommand().equals("PROCESS_LOG_FILE")) {
			
			if (fileType.contains("CISCO_CTX")) {
				
				if (!model.getEngineFile().equals("") && model.getEngineFile()!=null ) {
					
					if (!model.isFileProcessed()) {	
						
						ui.clearTextPanel();
						ui.setProgresBarIndeterminate();
						ui.showProgressBar(true);
						ui.printStatusMessage("Processing log...  ",0);
						
						SwingUtilities.invokeLater(new Runnable() {
							
					        public void run() {					
					        		model.setDisplay(false);
					        		model.emptySipCalls();
					        		model.emptySipMessages();
					        		model.emptySipMessagesFormatted();
					        		model.parseCtxFile();		//	TODO: Improve File Parse algorithm		        		
					        		model.processCtxSipMessagesDetails();	
					        		model.getErrorsFound();	
					        		ui.printCallsNumber(model.getNumCalls());
					        		ui.printMessagesNumber(model.getSipMessagesNumberFound());
					        		ui.printMessages(model.getSipMessagesDetailedInfo(true));			        		
					        		ui.printStatusMessage("Log analysis completed. ",0);
					        		ui.showProgressBar(false);			        	
					        	
					        }
					    });
					}
					else {
						
						ui.printStatusMessage("File already processed ",2);
		        	}
					
					
				}
				
				else {
					ui.printStatusMessage("No file loaded",2);	
					
				}
			}
			else if (fileType.contains("CISCO_CUCM")) {
				if (!model.getEngineFile().equals("") && model.getEngineFile()!=null ) {
					
					if (!model.isFileProcessed()) {	
						
						ui.clearTextPanel();
						ui.setProgresBarIndeterminate();
						ui.showProgressBar(true);
						ui.printStatusMessage("Processing log...  ",0);
						
						SwingUtilities.invokeLater(new Runnable() {
					        public void run() {		        					    
					        		model.setDisplay(false);  		
					        		model.emptySipCalls();
					        		model.emptySipMessages();
					        		model.emptySipMessagesFormatted();
					        	//	model.parseMobicentsFile();
					        	//	model.processMobicentsSipMessagesDetails();
					        		model.getErrorsFound();
					        		ui.printCallsNumber(model.getNumCalls());
					        		ui.printMessagesNumber(model.getSipMessagesNumberFound());
					        		ui.printMessages(model.getSipMessagesDetailedInfo(true));	
					        		ui.printStatusMessage("Log analysis completed. ",0);
					        		ui.showProgressBar(false);			        	
					        }
					    });
					}
					else {
						
						ui.printStatusMessage("File already processed ",2);
		        	}
					
					
				}
				
				else {
					ui.printStatusMessage("No file loaded",2);	
					
				}
			}
			else if (fileType.contains("MOBICENTS")) {
				if (!model.getEngineFile().equals("") && model.getEngineFile()!=null ) {
					
					if (!model.isFileProcessed()) {	
						
						ui.clearTextPanel();
						ui.setProgresBarIndeterminate();
						ui.showProgressBar(true);
						ui.printStatusMessage("Processing log...  ",0);
						
						SwingUtilities.invokeLater(new Runnable() {
					        public void run() {		        					    
					        		model.setDisplay(false);  		
					        		model.emptySipCalls();
					        		model.emptySipMessages();
					        		model.emptySipMessagesFormatted();
					        		model.parseMobicentsFile();
					        		model.processMobicentsSipMessagesDetails();
					        		model.getErrorsFound();
					        		ui.printCallsNumber(model.getNumCalls());
					        		ui.printMessagesNumber(model.getSipMessagesNumberFound());
					        		ui.printMessages(model.getSipMessagesDetailedInfo(true));	
					        		ui.printStatusMessage("Log analysis completed. ",0);
					        		ui.showProgressBar(false);			        	
					        }
					    });
					}
					else {
						
						ui.printStatusMessage("File already processed ",2);
		        	}
					
					
				}
				
				else {
					ui.printStatusMessage("No file loaded",2);	
					
				}
			}
			else if (fileType.contains("ASTERISK")) {
				ui.printStatusMessage("Not implemented",1);	
			}
			else if (fileType.contains("UNKNOWN")) {
				ui.printStatusMessage("Invalid File loaded. Please load valid Log File",3);	
			}
			else if(id.getEngineFileName().equals("") && id.getEngineFileName()!=null) {
				ui.printStatusMessage("No file loaded",2);	
			}
			else {
				ui.printStatusMessage("Application error. Please try again.",3);
			}
			
		
		
		}
		
		
		else if (event.getCommand().equals("START_DISPLAY_CALLS")) {
		
			if (!model.getEngineFile().equals("") && model.getEngineFile()!=null ) {		
				
				if(model.getSipMessagesFormattedNumber() > 0 && !model.isDisplayed()) {
					
					ui.printStatusMessage("Displaying calls...",0);
					//ui.printMessages(model.getSipMessagesContentFormat());
					ui.printStatusMessage("Total messages displayed: " + model.getSipMessagesFormattedNumber(),0);
					ui.drawDiagram(newDiagramFile.initDiagram(model.getSipMessagesContentFormat()));
					model.setDisplay(true);	
					
				}
				else if (model.isDisplayed()) {
					
					ui.printStatusMessage("Calls already displayed",2);
				
				}
				else{
					
					ui.printStatusMessage("No calls to display.",2);
				}				
						
			}
			
			else {
				ui.printStatusMessage("No file loaded",2);	
			}
		}	
		
	}
	
	

}
