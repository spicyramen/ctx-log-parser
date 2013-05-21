package com.ctx.log.parser.model;

import java.util.ArrayList;

public class SipMessageViewer {

	private String srcIP = "";
	private String dstIP = "";
	private String srcPort = "5060";
	private String dstPort = "5060";
	private String messageDirection = "";
    private String timeStamp = "";
    private ArrayList<String> content = new ArrayList<String>();
	private int messageID = 0;
	private boolean completed;
	
	
	
	public String returnMessageHeader() {
		
		return timeStamp + " " + messageDirection + " " + srcIP + ":" + srcPort + " --> " + dstIP + ":" + dstPort; 
	}

	
	public boolean isCompleted() {
		return completed;
	}


	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public SipMessageViewer(int id) {
		this.messageID = id;
	}
	
	public SipMessageViewer() {
		// TODO Auto-generated constructor stub
	}

	public int getMessageID() {
		return messageID;
	}


	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}
	
	public ArrayList<String> getContent() {
		return content;
	}

	public void setContent(ArrayList<String> content) {
		this.content = content;
	}

	public String getSrcIP() {
		return srcIP;
	}

	public void setSrcIP(String srcIP) {
		this.srcIP = srcIP;
	}

	public String getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}

	public String getDstIP() {
		return dstIP;
	}

	public void setDstIP(String dstIP) {
		this.dstIP = dstIP;
	}

	public String getDstPort() {
		return dstPort;
	}

	public void setDstPort(String dstPort) {
		this.dstPort = dstPort;
	}

	public String getMessageDirection() {
		return messageDirection;
	}

	public void setMessageDirection(String messageDirection) {
		this.messageDirection = messageDirection;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	
	
	
	
}
