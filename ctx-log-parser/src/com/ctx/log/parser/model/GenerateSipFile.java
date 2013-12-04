package com.ctx.log.parser.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class GenerateSipFile {

	
public GenerateSipFile(){
	
}


public String initDiagram(ArrayList<String> messagesText) {
	/*
	 * Generate temporary file based on valid SIP messages
	 */
	
	File tempFile;
	try{
		tempFile = File.createTempFile("ctx-log-viewer", ".tmp");
		FileWriter fileWriter = new FileWriter(tempFile.getAbsolutePath());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		for(String str: messagesText) {
			 bufferedWriter.write(str);
			 bufferedWriter.newLine();
			 bufferedWriter.flush();	     
		}
		
		fileWriter.close();
		bufferedWriter.close();
		String fileName = tempFile.getAbsolutePath();
		return fileName;
		
	}
	catch(IOException e){
        e.printStackTrace();
        return null;
    }
		
}


}
