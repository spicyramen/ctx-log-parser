package com.ctx.log.parser.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * TODO: Create state machine to read logs faster
 */

public class SipParser {
	
		
	
	static private String word = "(\\w+)";
	static private String digits = "(\\d+)";
	static private String validIpAddressRegex = "((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))";
	static private String sipv2 = "(\\w+\\s+sip:.*|SIP.*)";
	static private String timeStampRegex = "^" + digits + "-" + digits + "-" + digits + "\\s" + digits + ":" + digits + ":" + digits + "," + digits;
	static private String mobicentsTimeStampRegex = "^" + digits + ":" + digits + ":" + digits + "," + digits;
	static private String debugLevel = "\\s(\\w+)\\s+";	
	static private String appInfo = "\\s+\\{(.*)\\}\\-\\[" + word + ":" + word + "\\|" + digits + "\\]\\s-\\s";
	static private String mobicentsAppTCP= "SIP\\-TCP\\-Core\\-PipelineThreadpool\\-\\d+";
	static private String mobicentsAppUDP = "Mobicents\\-SIP\\-Servlets\\-UDPMessageChannelThread\\-\\d+";
	static private String mobicentsAppSelf = "SelfRoutingThread\\-\\d+";
	static private String mobicentsAppInfo = "\\s\\[.*\\]\\s" + "\\(" + "(" + mobicentsAppTCP + "|" + mobicentsAppUDP + "|" + mobicentsAppSelf + ")" + "\\)\\s\\<message.*";
	static private String messageDirection = "(RX:|TX:)";
	static private String sipInfo = "\\s" + sipv2 ;
	static private String callIdRegex = "Call-ID:\\s+((.*)\\@" + validIpAddressRegex + ".*)";
	static private String callIdRegexMobicents = "Call-ID:\\s+(.*)";
	static private String duplicateMessageRegex = "(.*)" + appInfo + "(.*)" + "(RX:|TX:)";
	static private String methodRegex = ".*(RX:|TX:)\\s+(.*)";
	static private String engineParser = timeStampRegex + debugLevel + appInfo + messageDirection + sipInfo ;
	static private String mobicentsParser = mobicentsTimeStampRegex + debugLevel + mobicentsAppInfo;	
	static private String ivrNumber = ":3666@";
	

	
	private String engineAbsoluteFileName = "";
	private String engineFileName = "";
    private int errorsFound = 0;
    private boolean fileProcessed = false;
    private boolean callsDisplayed = false;
	private LinkedList<SipMessage> sipMessages = new LinkedList<SipMessage>();
 	private LinkedList<SipMessageViewer> sipMessagesFormatted = new LinkedList<SipMessageViewer>();
 	private Map<String,SipCall> mapCalls = new HashMap<String,SipCall>();
	
	
 
	/**
 	 * 
 	 * 
 	 */
 	
	public SipParser() {
		
	}
	
	/**
	 * Parse SIP information
	 * Read all SIP Messages (MeetMe Calls) in ctc-engine File based on RX/TX pattern
	 */

	public boolean parseCtxFile() {
			
		boolean sipInfo = false;
		BufferedReader br = null;
		ArrayList<String> sipMessageContent = new ArrayList<String>();
		String fileLine = null;
		String sipMessageHeader = null;
		String callID = null;
		
        /**
         * Open File and verify Read Access
         */
		
		try {
			if (verifyFileAccess(getEngineFile())) 
				br = new BufferedReader(new FileReader(getEngineFile()));
			else {
				errorsFound++;
				return false;	
			}			
		} 
		catch (Exception e) {
				errorsFound++;
				return false;
		}
		
		
		/**
		 * Process SIP message in File
		 */
		
        try {    
        	
			while((fileLine = br.readLine()) != null) {
				
				if (!sipInfo && fileLine.matches(engineParser)){						// Find first SIP message Line
					sipMessageHeader = fileLine;				
				    sipInfo = true;
				}
				
				else if (sipInfo && !fileLine.matches(timeStampRegex + "(.*)")) {		// Obtain SIP Contents
					   
						if (!fileLine.isEmpty()) {
					    	
					    	if(fileLine.matches(callIdRegex)){
					    		
					    		Matcher callIdMatcher = Pattern.compile(callIdRegex).matcher(fileLine);
					    		callIdMatcher.find();
					    		
					    		try {
					    			if(!callIdMatcher.group(1).isEmpty()) {
					    				callID = callIdMatcher.group(1);
					    			}			
					    		}
					    		catch(Exception e) {
					    			errorsFound++;
					    		}
					    		
					    		/*
					    		 *  Create new call objects
					    		 */
					    		mapCalls.put(callID, new SipCall(callID));					    		
					    	}					
					    	sipMessageContent.add(fileLine);						
					    }
					    
				}
				else if (sipInfo && !fileLine.matches(engineParser)) {					// Populate SIP message, we have finished reading
					
					processNewSipMessage(1,sipMessageHeader,sipMessageContent,callID);				
					sipMessageHeader="";
					sipMessageContent.clear();
					callID = "";
					sipInfo = false;
				
				}
				else {
					sipInfo = false;
				}
			}
		} catch (Exception e) {
			errorsFound++;
			return false;
		}
        
        fileProcessed = true;
        try {
			br.close();
		} catch (IOException e) {		
			errorsFound++;
			return false;
		}
        
        return true;
        
	}

	/**
	 * 
	 * @return parseMobicentsFile process text File and obtain SIP info
	 */
	
	
	public boolean parseMobicentsFile() {
		
		boolean sipInfo = false;
		boolean processSipMessage = false;
		BufferedReader br = null;
		ArrayList<String> sipMessageContent = new ArrayList<String>();
		String fileLine = null;
		String sipMessageHeader = null;
		String callID = null;
		
		
		//19:21:17,046 INFO  [gov.nist.javax.sip.stack.SIPTransactionStack] (SIP-TCP-Core-PipelineThreadpool-29) <message
		
		try {
			if (verifyFileAccess(getEngineFile())) 
				br = new BufferedReader(new FileReader(getEngineFile()));
			else {
				errorsFound++;
				return false;	
			}			
		} 
		catch (Exception e) {
				errorsFound++;
				return false;
		}
		
		try {    
	        	
				while((fileLine = br.readLine()) != null) {
					
					if (!sipInfo && fileLine.matches(mobicentsParser)){
					    sipInfo = true;
					    sipMessageContent.add(fileLine);
					    
					}
					
					else if (sipInfo && !fileLine.matches(mobicentsTimeStampRegex + "(.*)")) {
						
						if (!fileLine.isEmpty()) {
					    	
							sipMessageContent.add(fileLine);		//Add Content to array					
						    
							if(fileLine.matches("\\<!\\[CDATA\\[.*")){ 	// Find header
								Matcher callIdMatcher = Pattern.compile("\\<!\\[CDATA\\[(.*)").matcher(fileLine);
					    		callIdMatcher.find();
					    		
					    		try {
					    			if(!callIdMatcher.group(1).isEmpty()) {
					    				sipMessageHeader = callIdMatcher.group(1);
					    			}			
					    		}
					    		catch(Exception e) {
					    			errorsFound++;
					    		}
								
							}
							
							if(fileLine.matches(callIdRegexMobicents)){	// Get CallID
					    		
					    		Matcher callIdMatcher = Pattern.compile(callIdRegexMobicents).matcher(fileLine);
					    		callIdMatcher.find();
					    		
					    		try {
					    			if(!callIdMatcher.group(1).isEmpty()) {
					    				callID = callIdMatcher.group(1);
					    				
					    			}			
					    		}
					    		catch(Exception e) {
					    			errorsFound++;
					    		}
					    		
					    		/*
					    		 *  Create new call objects
					    		 */
					    		mapCalls.put(callID, new SipCall(callID));
					    		
					    	}
					    	
					    	 
					    	if (fileLine.matches("\\<\\/message\\>")) {		// Find end of Message
					    		
					    		 sipMessageContent.add(fileLine);
					    		 processSipMessage = true;
								 sipInfo = false;
					    		 processNewSipMessage(2,sipMessageHeader,sipMessageContent,callID);
								 
							 }		
						}
						 
					}
					else if (!sipInfo && processSipMessage && (!fileLine.matches(mobicentsParser)||fileLine.matches(""))) {					// Populate SIP message, we have finished reading
						
						 sipMessageHeader="";
						 sipInfo = false;
						 processSipMessage = false;
						 sipMessageContent.clear();
					}
					else {
						
						 processSipMessage = false;
						 sipInfo = false;
					}
				}
		 }	
		 catch (Exception e) {
			  errorsFound++;
			  return false;
		  }
    
	fileProcessed = true;
	try {
		br.close();
	} catch (IOException e) {		
		errorsFound++;
		return false;
	}
    
    return true;
		
	}
	
	
	public void parseAsteriskFile() {
		
	}
	
	public void parseCucmFile () {
		
	}
	
	/**
	 * 
	 * @param sipMessage
	 * @param sipContent
	 */
	
	private void processNewSipMessage(int type,String sipMessageHeader, ArrayList<String> sipContent, String callID) {	
		
		/*
		 * Creating SIP Message Object 
		 * Populate headerLine variable with first line of SIP message 
		 * containing Timestamp,function,direction,sipInfo
		 * 
		 */
		
		
		if (type == 1) {							// CTX
			
			SipMessage message = new SipMessage();
			message.setValidMessage(true);
			message.setCallID(callID);
			message.setHeaderLine(sipMessageHeader);
			
			/*
			 * Populating ArrayList with SIP Message header
			 */
			
			Matcher m0 = Pattern.compile(methodRegex).matcher(sipMessageHeader);
			try {
				if(m0.find()) {
					message.getSipContent().add(m0.group(2));
				}
				else {
					message.getSipContent().add("ERROR");
					errorsFound++;
				}
				
			}
			catch(Exception e) {
				errorsFound++;
			}	
			
			/*
			 * Populating ArrayList with SIP Message info
			 */
			for (String messageLine : sipContent) {		
				message.getSipContent().add(messageLine);
			}				
			
			/*
			 * Add SIP message to Array of SIP Messages
			 */
			Matcher m1 = Pattern.compile(duplicateMessageRegex).matcher(sipMessageHeader);
			m1.find();
		
			try {
				if(m1.group(7).contains("TX:") && (m1.group(4).contains("sendSipBye") || (m1.group(4).contains("sendSipReInvite")))) {
					
					//CTX sends a duplicate message, do nothing
				}	
				else {		
					
					// Add SIP Message			
					message.initializeCtxMessageFormat();
					if (message.isValidMessage())
						sipMessages.add(message);
				}			
			}
			catch(Exception e) {
				System.out.println("Error " + e);
				errorsFound++;
			}
			
			
			
		}
		else if (type == 2) {						// Mobicents
			
			
			SipMessage message = new SipMessage();
			message.setValidMessage(true);
			message.setCallID(callID);
			message.setHeaderLine(sipContent.get(0));
			ArrayList<String> mobicentsMessageContent = sipContent;
			boolean printInfo  = false;
					
			/*
			 * Populating ArrayList with SIP Message info
			 */
			
			for (String messageLine : sipContent) {	
				
				if(messageLine.matches("\\<!\\[CDATA\\[.*") && !printInfo){ 	// Find start of content
					Matcher callIdMatcher = Pattern.compile("\\<!\\[CDATA\\[(.*)").matcher(messageLine);
		    		callIdMatcher.find();
		    		
		    		try {
		    			if(!callIdMatcher.group(1).isEmpty()) {
		    				
		    				messageLine = callIdMatcher.group(1);			
		    				printInfo = true;
		    			}			
		    		}
		    		catch(Exception e) {
		    			errorsFound++;
		    		}
					
				}
				
				if(messageLine.matches("\\]\\]\\>.*") && printInfo){ 			// Find end of message
				
					printInfo = false;
					break;
				}
				
				if ( printInfo) {
					
					message.getSipContent().add(messageLine);
					
				}			
				
			}	
			
			message.setMobicentsContent(mobicentsMessageContent);
			message.initializeMobicentsMessageFormat();
			if (message.isValidMessage())
				sipMessages.add(message);
			
		}
		else if (type == 3) {	// Asterisk
			
		}
		else if (type == 4) {	// CUCM
			
		}
		
		else {					// Unknown
			
		}
		
		
		
	}

	
	
	
	
	/**
	 * 
	 * @return to Print in Window
	 */
	
	public LinkedList<SipMessageViewer> getSipMessagesFormatted() {
		return sipMessagesFormatted;
	}

	/**
	 * 
	 * @return
	 */
	
	public int getSipMessagesFormattedNumber(){
		return sipMessagesFormatted.size();
	}
	
	/**
	 * Populate IP addresses for each SIP Message based on SIP call
	 * 
	 */
	
	public void processCtxSipMessagesDetails() {
		
		
		SipMessage message;
		SipCall sipCall;
		SipMessageViewer messageViewer;
	
		for(int i=0; i< sipMessages.size(); i++) {
			
			message = sipMessages.get(i); // Obtain SIP Message	
			sipCall = mapCalls.get(message.getCallID());		
			messageViewer = new SipMessageViewer(i);
			
			if (message.getMessageDirection().contains("IN") && message.getSipMethod().contains("INVITE")) {						
				/*
				 * Initial INVITE from SBC
				 */
				if(!sipCall.isFormatted()) {
					
					sipCall.setCallId(message.getCallID());
					sipCall.setFromTagSbc(message.getFromTag());
					sipCall.setSbcIP(message.getContactIP());
					sipCall.setCtxIP(message.parseIpMethod(message.getSipMethod()));			
					sipCall.setViewCall(message.isValidSipInviteIn());	// Display only complete Calls which include Initial Invite
					sipCall.setFormatted(true);
					
					messageViewer.setTimeStamp(message.getTimeStamp());
					messageViewer.setMessageDirection(message.getMessageDirection());
					messageViewer.setSrcIP(sipCall.getIpInfo(message.getFromTag(),"",message.getSipMethod()));
					messageViewer.setDstIP(sipCall.getCtxIP());
					messageViewer.setContent(message.getSipContent());
					messageViewer.setCompleted(true);
					
					// Add SIP Message to SIP Messages with Format Array
					if(sipCall.isViewCall())
						sipMessagesFormatted.add(messageViewer);
					
				}
				else {
					
					// SIP Re-INVITE
					messageViewer.setTimeStamp(message.getTimeStamp());
					messageViewer.setMessageDirection(message.getMessageDirection());
					messageViewer.setSrcIP(sipCall.getIpInfo(message.getFromTag(),message.getToTag(),message.getSipMethod()));
					messageViewer.setDstIP(sipCall.getCtxIP());
					messageViewer.setContent(message.getSipContent());
					
					// Add SIP Message to SIP Messages with Format Array
					if(sipCall.isViewCall())
						sipMessagesFormatted.add(messageViewer);
					
				}
				
				
			}
			else if(sipCall.isFormatted() && message.getMessageDirection().contains("OUT") && message.getSipMethod().contains("INVITE")){
				
				
				// Initial INVITE to Bridge
				if (sipCall.getFromTagBridge().isEmpty() && !message.getSipMethod().contains(ivrNumber) && message.isValidSipInviteOut())  {
					
					sipCall.setFromTagBridge(message.getFromTag());
					sipCall.setBridgeIP(message.parseIpMethod(message.getSipMethod()));

					messageViewer.setTimeStamp(message.getTimeStamp());
					messageViewer.setMessageDirection(message.getMessageDirection());
					messageViewer.setSrcIP(sipCall.getCtxIP());		
					messageViewer.setDstIP(sipCall.getIpInfo(message.getFromTag(),"",message.getSipMethod()));
					messageViewer.setContent(message.getSipContent());
					
					// Add SIP Message to SIP Messages with Format Array
					if(sipCall.isViewCall())
						sipMessagesFormatted.add(messageViewer);								
				}
				
				// Initial INVITE to IVR				
				else if(sipCall.getFromTagIVR().isEmpty() && message.getSipMethod().contains(ivrNumber)) {
			
					sipCall.setFromTagIVR(message.getFromTag());
					sipCall.setIvrIP(message.parseIpMethod(message.getSipMethod()));
	
					messageViewer.setTimeStamp(message.getTimeStamp());
					messageViewer.setMessageDirection(message.getMessageDirection());
					messageViewer.setSrcIP(sipCall.getCtxIP());
					messageViewer.setDstIP(sipCall.getIpInfo(message.getFromTag(),"",message.getSipMethod()));
					messageViewer.setContent(message.getSipContent());
					
					// Add SIP Message to SIP Messages with Format Array
					if(sipCall.isViewCall())
						sipMessagesFormatted.add(messageViewer);
				} 
				
				else {
		
					// Re-INVITE
					messageViewer.setTimeStamp(message.getTimeStamp());
					messageViewer.setMessageDirection(message.getMessageDirection());
					messageViewer.setSrcIP(sipCall.getCtxIP());
					messageViewer.setDstIP(sipCall.getIpInfo(message.getFromTag(),message.getToTag(),message.getSipMethod()));
					messageViewer.setContent(message.getSipContent());
					
					// Add SIP Message to SIP Messages with Format Array
					if(sipCall.isViewCall())
						sipMessagesFormatted.add(messageViewer);
					
				}
					
			}	
			else if(sipCall.isFormatted() && message.getMessageDirection().contains("OUT")) {
				
				messageViewer.setTimeStamp(message.getTimeStamp());
				messageViewer.setMessageDirection(message.getMessageDirection());
				messageViewer.setSrcIP(sipCall.getCtxIP());
				messageViewer.setDstIP(sipCall.getIpInfo(message.getFromTag(),message.getToTag(),message.getSipMethod()));
				messageViewer.setContent(message.getSipContent());
				
				// Add SIP Message to SIP Messages with Format Array
				if(sipCall.isViewCall())
					sipMessagesFormatted.add(messageViewer);		
				
			}
			else if(sipCall.isFormatted() && message.getMessageDirection().contains("IN")) {
				
				messageViewer.setTimeStamp(message.getTimeStamp());
				messageViewer.setMessageDirection(message.getMessageDirection());
				messageViewer.setSrcIP(sipCall.getIpInfo(message.getFromTag(),message.getToTag(),message.getSipMethod()));
				messageViewer.setDstIP(sipCall.getCtxIP());
				messageViewer.setContent(message.getSipContent());
				
				// Add SIP Message to SIP Messages with Format Array
				if(sipCall.isViewCall())
					sipMessagesFormatted.add(messageViewer);
				
			}
			else {
				// ERROR				
				errorsFound++;		
			}
			
		}
			
		
		fileProcessed = true;
	}
	
	
	public void processMobicentsSipMessagesDetails() {
		
		SipMessage message;
		SipCall sipCall;
		SipMessageViewer messageViewer;
	
		for(int i=0; i< sipMessages.size(); i++) {
			
			message = sipMessages.get(i); // Obtain SIP Message	
			sipCall = mapCalls.get(message.getCallID());		
			messageViewer = new SipMessageViewer(i);
		
			sipCall.setCallId(message.getCallID());
			sipCall.setViewCall(true);
			
			messageViewer.setTimeStamp(message.getTimeStamp());
			messageViewer.setMessageDirection(message.getMessageDirection());
			messageViewer.setSrcIP(message.getSrcIP());
			messageViewer.setSrcPort(message.getSrcPort());
			messageViewer.setDstIP(message.getDstIP());
			messageViewer.setDstPort(message.getDstPort());
			messageViewer.setContent(message.getSipContent());
			messageViewer.setCompleted(true);
			
			
			if(sipCall.isViewCall())
				sipMessagesFormatted.add(messageViewer);
			
		}	
		
		
	}
	
	
	public boolean isFileProcessed() {
		return fileProcessed;
	}



	/**
	 * 
	 * @return
	 * We prefix TimeStamp + Direction to display info
	 */
	
	public ArrayList<String> getSipMessagesDetailedInfo(boolean BasicInfo) {
		
		ArrayList<String> sipInfo = new ArrayList<String>();
		int header = 1;
		for(int i=0; i< sipMessages.size(); i++) {
			 ArrayList<String> content = sipMessages.get(i).getSipContent();
			 
			 for (String line : content) { 
				if (header == 1) { 
					sipInfo.add(sipMessages.get(i).getMessageBasicInfo() + " " + line);
					header++;
				}
				else {
					sipInfo.add(line);
				}
			 }
			 header = 1;
		 }
		return sipInfo;
	}
	
	/**
	 * 
	 * @return
	 */
	
	public ArrayList<String> getSipMessagesDetailedInfo() {
		
		ArrayList<String> sipInfo = new ArrayList<String>();
		for(int i=0; i< sipMessages.size(); i++) {
			 ArrayList<String> content = sipMessages.get(i).getSipContent();
			 for (String line : content) { 
					sipInfo.add(line);
			 }
		 }
		return sipInfo;
	}
	
	/**
	 * 
	 * @return
	 */
	
	public ArrayList<String> getSipMessagesContentFormat() {
		
		ArrayList<String> sipInfo = new ArrayList<String>();
		
		for(int i=0; i< sipMessagesFormatted.size(); i++) {
				 sipInfo.add(sipMessagesFormatted.get(i).returnMessageHeader());		 
				 ArrayList<String> content = sipMessagesFormatted.get(i).getContent();
				 for (String line : content) {   
					 sipInfo.add(line);
				 }		 
				 sipInfo.add("-----------------------");
			 
		 }
		return sipInfo;
	}
	
	
	/**
	 * 
	 * @param logFileName
	 * @return
	 */
	
	
	private boolean verifyFileAccess(String logFileName)  {
		
		
		if (!logFileName.equals("") && logFileName!=null) {
			setEngineFile(logFileName);
			
			File configFile = new File(getEngineFile());
			
			if (configFile.exists() && configFile.canRead()) {
				engineFileName = configFile.getName();
				return true;
			} 
			else {
				
				errorsFound++;
				return false;
				
			}
		} 
		else {
			errorsFound++;
			return false;
		}

		
	}
	
	public String getEngineFileName() {
		return engineFileName;
	}



	/**
	 * @return All sipMessages processed during parsing
	 */
		
	public LinkedList<SipMessage> getSipMessages() {
		return sipMessages;
	}

	public void emptySipMessages() {
		sipMessages.clear();
	}
	
	public int getSipMessagesNumberFound() {
		return sipMessages.size();
	}


	public Map<String, SipCall> getMap() {
		return mapCalls;
	}
	
	public int getNumCalls() {
		return mapCalls.size();
	}

	public void emptySipCalls() {
		mapCalls.clear();
	}
	
	public void emptySipMessagesFormatted() {
		sipMessagesFormatted.clear();
	}

	public void printMapCalls (Map<String,SipCall> mp) {
		for (Entry<String, SipCall> entry : mp.entrySet()) {
		    String key = entry.getKey();
		    System.out.println(key);
		}
	}
	
	public boolean getCallIdMap(String callID){
		return mapCalls.containsKey(callID);
	}
	
	public String getEngineFile() {
		return engineAbsoluteFileName;
	}


	public void setEngineFile(String engineFile) {		
		
		if (!this.engineAbsoluteFileName.equals(engineFile)) {
			fileProcessed = false; 
		}
		this.engineAbsoluteFileName = engineFile;
		
	}
	
	public int getErrorsFound() {
		return errorsFound;
	}

	public boolean isDisplayed() {
		return callsDisplayed;
	}

	public void setDisplay(boolean display) {
		callsDisplayed = display;
	}
	
	
}
