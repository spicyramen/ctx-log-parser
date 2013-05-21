package com.ctx.log.parser.model;


public class SipCall {

	
	
	private String callId = "";
	private String sipURI = "";
	private String disconnectCode = "";
	private String ctxIP = "";
	private String sbcIP = "";
	private String bridgeIP = "";
	private String ivrIP = "";
	private String fromTagSbc = "";
	private String fromTagBridge = "";
	private String fromTagIVR = "";
	private String ivrNumber = "3666";
	private boolean viewCall;
	private boolean formatted;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((callId == null) ? 0 : callId.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SipCall other = (SipCall) obj;
		if (callId == null) {
			if (other.callId != null)
				return false;
		} else if (!callId.equals(other.callId))
			return false;
		return true;
	}
	
	
	public boolean isViewCall() {
		return viewCall;
	}


	public void setViewCall(boolean viewCall) {
		this.viewCall = viewCall;
	}
	
	public String getCallId() {
		return callId;
	}
	
	public SipCall(String callId){
		this.callId = callId;
	}


	public String getSipURI() {
		return sipURI;
	}


	public void setSipURI(String sipURI) {
		this.sipURI = sipURI;
	}


	public String getDisconnectCode() {
		return disconnectCode;
	}


	public void setDisconnectCode(String disconnectCode) {
		this.disconnectCode = disconnectCode;
	}
	
	public String getCtxIP() {
		return ctxIP;
	}


	public void setCtxIP(String ctxIP) {
		this.ctxIP = ctxIP;
	}
	
	public String getSbcIP() {
		return sbcIP;
	}


	public void setSbcIP(String sbcIP) {
		this.sbcIP = sbcIP;
	}


	public String getBridgeIP() {
		return bridgeIP;
	}


	public void setBridgeIP(String bridgeIP) {
		this.bridgeIP = bridgeIP;
	}

	public String getIvrIP() {
		return ivrIP;
	}


	public void setIvrIP(String ivrIP) {
		this.ivrIP = ivrIP;
	}


	public String getFromTagIVR() {
		return fromTagIVR;
	}


	public void setFromTagIVR(String fromTagIVR) {
		this.fromTagIVR = fromTagIVR;
	}
	
	public String getFromTagSbc() {
		return fromTagSbc;
	}


	public void setFromTagSbc(String fromTagSbc) {
		this.fromTagSbc = fromTagSbc;
	}


	public String getFromTagBridge() {
		return fromTagBridge;
	}


	public void setFromTagBridge(String fromTagBridge) {
		this.fromTagBridge = fromTagBridge;
	}


	public void setCallId(String callId) {
		this.callId = callId;
	}


	public boolean isFormatted() {
		return formatted;
	}


	public void setFormatted(boolean formatted) {
		this.formatted = formatted;
	}

	/**
	 * 
	 * @param fromTag
	 * @param toTag
	 * @return
	 */
	
public String getIpInfo(String fromTag,String toTag,String method) {
		
		if(fromTag.contains(getFromTagSbc()) && toTag.contains("") && method.contains("INVITE") && !fromTag.isEmpty() && !getFromTagSbc().isEmpty()) {
			return getSbcIP();
		}
		else if(fromTag.contains(getFromTagBridge()) && toTag.contains("") && method.contains("INVITE") && !fromTag.isEmpty() && !getFromTagBridge().isEmpty()){
			return getBridgeIP();
		}
		else if(fromTag.contains(getFromTagIVR()) && toTag.contains("") && method.contains("INVITE") && method.contains(ivrNumber) && !fromTag.isEmpty() && !getFromTagIVR().isEmpty()){
			return getIvrIP();
		}
		else if(fromTag.contains(getFromTagSbc()) && !toTag.isEmpty() && !getFromTagSbc().isEmpty()) {
			return getSbcIP();
		}
		else if(fromTag.contains(getFromTagBridge()) && !toTag.isEmpty() && !getFromTagBridge().isEmpty()) {
			return getBridgeIP();
		}
		else if(fromTag.contains(getFromTagIVR()) && !toTag.isEmpty() && !getFromTagIVR().isEmpty()) {
			return getIvrIP();
		}
		else if(!fromTag.isEmpty() && toTag.contains(getFromTagSbc()) && !getFromTagSbc().isEmpty()) {
			return getSbcIP();
		}
		else if(!fromTag.isEmpty() && toTag.contains(getFromTagBridge()) && !getFromTagBridge().isEmpty()) {
			return getBridgeIP();
		}
		else{
			System.out.println(Object.class + "getIpInfo()! From: " + fromTag + " To: " + toTag);
			return "";
		}		
	}
	
	
	
}
