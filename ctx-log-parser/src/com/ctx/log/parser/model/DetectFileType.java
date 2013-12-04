package com.ctx.log.parser.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DetectFileType {

	static private String digits = "(\\d+)";
	static private String timeStampRegex = "^" + digits + "-" + digits + "-" + digits + "\\s" + digits + ":" + digits + ":" + digits + "," + digits;
	static private String mobicentsTimeStampRegex = "^" + digits + ":" + digits + ":" + digits + "," + digits;
	private String engineAbsoluteFileName = "";
	private String engineFileName = "";
    private boolean fileProcessed = false;
    static private int READ_LIMT   = 125;
	
    
public DetectFileType() {
	
}
    
public String DetectLogType() {
	
	BufferedReader br = null;
	String fileLine = null;
	int lineCounter = 0;
	
	try {
		
		if (VerifyFileAccess(getAbsolutFileName())) {
			br = new BufferedReader(new FileReader(getAbsolutFileName()));
			try {    
				while((fileLine = br.readLine()) != null && lineCounter < READ_LIMT) {
				
					if (fileLine.matches(timeStampRegex + "(.*)")){
						fileProcessed = true;
						return "CISCO_CTX";
					}
					if (fileLine.matches(mobicentsTimeStampRegex + "(.*)")){
						fileProcessed = true;
						return "MOBICENTS";
					}
					
				lineCounter++;
				}
			
			} 
			catch (Exception e) {			
				return "";
			}
			
		}
		
		else {		
			return "";	
		}			
	
	} 
	catch (Exception e) {
			return "";
	}
	  
	return "";
}

private boolean VerifyFileAccess(String logFileName)  {
		
		
		if (!logFileName.equals("") && logFileName!=null) {
			setEngineFile(logFileName);
			
			File configFile = new File(getAbsolutFileName());
			
			if (configFile.exists() && configFile.canRead()) {
				this.engineFileName = configFile.getName();
				return true;
			} 
			else {
				
				return false;
				
			}
		} 
		else {
			
			return false;
		}

		
	}
	
public boolean isFileProcessed() {
	return fileProcessed;
}

public String getAbsolutFileName() {
		return engineAbsoluteFileName;
	}

public String getEngineFileName() {
	return engineFileName;
}

public void setEngineFile(String logFile) {		
		
		if (!this.engineAbsoluteFileName.equals(logFile)) {
			fileProcessed = false; 
		}
		
		this.engineAbsoluteFileName = logFile;
		
	}

}
