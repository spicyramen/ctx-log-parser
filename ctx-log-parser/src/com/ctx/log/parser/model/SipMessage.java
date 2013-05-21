package com.ctx.log.parser.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SipMessage {

	
	//2012-05-04 08:12:50,949 INFO  {sjc01-ops-ctx-eng-1|MeetmeApplication|MeetMe-doRequest}-[SipFSM:sendReferenceMessage|158] - TX: BYE sip:8001@10.22.133.238:5060;transport=tcp SIP/2.0
	//[2011/01/13 08:55:56.109] IN 172.24.65.206:4040 --> 142.120.93.129:11156

	private int    processingErrors = 0;
	private String callID = "";
	private String headerLine = "";
	private String digits = "(\\d+)";
	private String messageDirectionRegex = "(RX:|TX:)";
	private String messageDirection = "";
	private String timeStampRegex = "^" + digits + "-" + digits + "-" + digits + "\\s" + digits + ":" + digits + ":" + digits + "," + digits;
	private String fromTagRegex = "From:.*<.*>;tag=(.*)";
	private String toTagRegex = "To:.*<.*>;tag=(.*)";
	private String methodRegex = ".*(RX:|TX:)\\s+(.*)";
	private String validIpAddressRegexDomain = ".*\\@((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])):";
	private String validIpAddressRegex = "((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))";
	private String validIPAddressRegexContact = ".*:((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))(:|;)";
	private String validInviteRegexIn = ".*(RX:)\\s+INVITE\\s+sip:(.*)@((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])):";
	private String validInviteRegexOut = ".*(TX:)\\s+INVITE\\s+sip:(.*)@((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])):";
	private String toTag = "";
	private String fromTag = "";
	private String timeStamp = "";
	private String sipMethod = "";
	private String srcIP     = "";
	private String dstIP     = "";
	private String srcPort   = "";
	private String dstPort   = "";
	private ArrayList<String> sipContent = new ArrayList<String>();
	private ArrayList<String> mobicentsContent = new ArrayList<String>();
	private boolean validMessage = true;	
	
	/**
	 * Constructor
	 */
	
	public SipMessage() {
		
	}
	
	public String convertEpochDate(String epochString) {
		
		if (epochString.isEmpty())
			return "ERROR";
		
		long epoch = Long.parseLong( epochString );
		Date expiry = new Date( epoch );
		Calendar cal = Calendar.getInstance();
		cal.setTime(expiry);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1; // JANUARY 0, FEBRUARY 1...DECEMEBER 11
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		String Smonth = "";
		String Sday   = "";
		String Shour  =  "";
		String Sminute = "";
		String Ssecond = "";
		
		// Correct DATE Format YYYY/MM/DD HH:MM:SS.MS
		
		if (month<10) {
			Smonth = "0" + String.valueOf(month);
		}
		else {
			Smonth = String.valueOf(month);
		}
		
		if (day<10) {
			Sday = "0" + String.valueOf(day);
		}
		else {
			Sday = String.valueOf(day);
		}
		
		if (hour<10) {
			Shour = "0" + String.valueOf(hour);
		}
		else {
			Shour = String.valueOf(hour);
		}
		
		if (minute<10) {
			Sminute = "0" + String.valueOf(minute);
		}
		else {
			Sminute = String.valueOf(minute);
		}
		
		if (second<10) {
			Ssecond = "0" + String.valueOf(second);
		}
		else {
			Ssecond = String.valueOf(second);
		}
		
		int ms = cal.get(Calendar.MILLISECOND);
		
		
		return "[" + String.valueOf(year) + "/" + Smonth + "/" + Sday + " " 
				   + Shour + ":" + Sminute + ":" + Ssecond + "." + String.valueOf(ms) + "]";
		
	}
	
	
	public boolean isValidSipInviteIn(){
		
		Matcher m0 = Pattern.compile(validInviteRegexIn).matcher(headerLine);
		
		try {
			if(m0.find()) {
				return true;
			}
			else {
				return false;
			}
			
		}
		catch(Exception e) {
			processingErrors++;
			
		}	
		
		return false;
				
	}
	
	public boolean isValidSipInviteOut(){
		
		Matcher m0 = Pattern.compile(validInviteRegexOut).matcher(headerLine);
		
		try {
			if(m0.find()) {
				return true;
			}
			else {
				return false;
			}
			
		}
		catch(Exception e) {
			processingErrors++;
			
		}	
		
		return false;
				
	}

	/**
	 * 
	 * @param sipLine
	 * @return
	 */

	public String parseIpMethod(String sipLine){
		
		Matcher m0 = Pattern.compile(validIpAddressRegexDomain).matcher(sipLine);
		String resultIP= "";
		
		try {
			if (m0.lookingAt()) {		
				resultIP = m0.group(1);			
	    	}
		}
		catch(Exception e) {
			processingErrors++;
			return resultIP;
		}
		
		return resultIP;
	}

	/**
	 * 
	 */
	
	public String parseIpContact(String sipLine){
		String resultIP= "";
		Matcher m0 = Pattern.compile(validIPAddressRegexContact).matcher(sipLine);
		
		try {
			if (m0.lookingAt()) {				
				resultIP = m0.group(1);
	    	}
		}
		catch(Exception e) {
			processingErrors++;
			return resultIP;
		}
		return resultIP;
	}
	
	/**
	 * 
	 * @param headerLine
	 */
	
	public void setHeaderLine (String headerLine) {
		this.headerLine = headerLine;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getSipContent() {
		return sipContent;
	}

	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getSipContent(boolean flag) {
		return sipContent;
	}
	
	
	public String getContactIP() {	
		for (String line: sipContent) {
			if(line.contains("Contact:")){
				return parseIpContact(line);
			}
		}
		return "";
	}
	
	/**
	 * 
	 * @return
	 */
	
	public String getCallID() {
		return callID;
	}

	public void setCallID(String callID) {
		this.callID = callID;
	}
	
	public void initializeCtxMessageFormat () {
		
		Matcher m1 = Pattern.compile(timeStampRegex).matcher(headerLine);
		
		try {
			if (m1.lookingAt())
	    	{
				//[2011/01/13 08:55:56.109]
	    		timeStamp = "[" + m1.group(1) + "/" + m1.group(2) + "/" + m1.group(3) + " " + m1.group(4) + ":" + m1.group(5) + ":" + m1.group(6) + "." + m1.group(7) + "]";
	        	
	    	}
		}
		catch(Exception e) {
			validMessage=false;
			processingErrors++;
		}
		
		Matcher m2 = Pattern.compile(messageDirectionRegex).matcher(headerLine);
		m2.find();
		
		try {
			if(m2.group(0).contains("RX")) {
				setMessageDirection("IN");
			}
			else if(m2.group(0).contains("TX")) {
				setMessageDirection("OUT");
			}
			else {
				setMessageDirection("ERROR");
				validMessage=false;
				processingErrors++;
			}
			
		}
		catch(Exception e) {
			processingErrors++;
		}
	
		Matcher m3 = Pattern.compile(methodRegex).matcher(headerLine);
		try {
			if(m3.find()) {
				setSipMethod(m3.group(2));
			}
			else {
				setSipMethod("ERROR");
				validMessage=false;
				processingErrors++;
			}
			
		}
		catch(Exception e) {
			validMessage=false;
			processingErrors++;
		}	
		
		for (String line : sipContent) {   
			 Matcher m4 = Pattern.compile(fromTagRegex).matcher(line);
			 if(m4.find()){
				fromTag = m4.group(1);
			 }
		}
		 
		for (String line : sipContent) {   
			 Matcher m5 = Pattern.compile(toTagRegex).matcher(line);
			 if(m5.find()){
				 toTag = m5.group(1);
			 }
		}
		
		
		
	}
	
	public String getSipMethod() {
		return sipMethod;
	}


	public void setSipMethod(String sipMethod) {
		this.sipMethod = sipMethod;
	}
	
	public String getToTag() {
		return toTag;
	}

	public void setToTag(String toTag) {
		this.toTag = toTag;
	}

	public String getFromTag() {
		return fromTag;
	}

	public void setFromTag(String fromTag) {
		this.fromTag = fromTag;
	}
	
	
	public int getErrors() {
		return processingErrors;
	}

	public String getTimeStamp() {
		return timeStamp;
	}
	
	public String getMessageDirection() {	
			return messageDirection;		
	}
	
	public String getMessageBasicInfo () {
		if (messageDirection.contains("IN")) {
			return timeStamp + " " + "RX:";
		}
		else if(messageDirection.contains("OUT")) {
			return timeStamp + " " + "TX:";
		}
		else {
			return "ERROR";
		}
	}
	
	

	private void setMessageDirection(String messageDirection) {
		
		this.messageDirection = messageDirection;
	
	}

	public void initializeMobicentsMessageFormat() {
		
		
		for (String messageLine : mobicentsContent) {
			
			
			if(messageLine.matches("time\\=\"(.*)\"")) {
				Matcher m1 = Pattern.compile("time\\=\"(.*)\"").matcher(messageLine);
				
				try {
					if (m1.lookingAt()) {
						
						if (m1.group(1).contains("0") || m1.group(1).isEmpty()) {
							processingErrors++;
							setValidMessage(false);
						}
						else {
							timeStamp = convertEpochDate(m1.group(1));
						}
					}
				}
				catch(Exception e) {
					setValidMessage(false);
					processingErrors++;
				}
			}
			
			else if(messageLine.matches("isSender\\=\"(.*)\".*")) {
				Matcher m2 = Pattern.compile("isSender\\=\"(.*)\".*").matcher(messageLine);
				
				try {
					if (m2.lookingAt()) {
						
					
						
						if (m2.group(1).matches("true")) {
							setMessageDirection("OUT");
							
						}
						else if(m2.group(1).contains("false")) {
							setMessageDirection("IN");
							
						}
						else
							setValidMessage(false);
					}
				}
				catch(Exception e) {
					setValidMessage(false);
					processingErrors++;
				}
			}
			
			else if(messageLine.matches("from\\=\"(.*):(\\d+)\".*")) {
				Matcher m3 = Pattern.compile("from\\=\"(.*):(\\d+)\".*").matcher(messageLine);
				
				try {
					if (m3.lookingAt()) {
						
						if (m3.group(1).matches(validIpAddressRegex)) {
							srcIP = m3.group(1);
							srcPort =  m3.group(2);
						}						
					}
				}
				catch(Exception e) {
					setValidMessage(false);
					processingErrors++;
				}
			}
			
			else if(messageLine.matches("to\\=\"(.*):(\\d+)\".*")) {
				Matcher m4 = Pattern.compile("to\\=\"(.*):(\\d+)\".*").matcher(messageLine);
				
				try {
					if (m4.lookingAt()) {
						
						if (m4.group(1).matches(validIpAddressRegex)) {
							dstIP = m4.group(1);
							dstPort =  m4.group(2);
						}						
					}
				}
				catch(Exception e) {
					setValidMessage(false);
					processingErrors++;
				}
			}
			
		}
		
	}

	public ArrayList<String> getMobicentsContent() {
		return mobicentsContent;
	}

	public void setMobicentsContent(ArrayList<String> mobicentsContent) {
		this.mobicentsContent = mobicentsContent;
	}

	public boolean isValidMessage() {
		return validMessage;
	}

	public void setValidMessage(boolean validMessage) {
		this.validMessage = validMessage;
	}

	public String getSrcIP() {
		return srcIP;
	}

	public String getDstIP() {
		return dstIP;
	}

	public String getSrcPort() {
		return srcPort;
	}

	public String getDstPort() {
		return dstPort;
	}
	
}
