package com.ctx.log.parser.view;

public class AnalysisFormEvent {
	private String runCommand;
	private String fileName;
	
	
	public AnalysisFormEvent(String command) {
		this.runCommand = command;
	
	}

	public AnalysisFormEvent(String command,String fileName) {
		this.runCommand = command;
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getCommand() {
		return runCommand;
	}


	public void setCommand(String command) {
		this.runCommand = command;
	}



	
	
	
}
