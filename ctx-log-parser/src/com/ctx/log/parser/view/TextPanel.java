package com.ctx.log.parser.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import java.io.IOException;

import com.beust.jcommander.JCommander;


public class TextPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	private ArrayList<String> messagesText = new ArrayList<String>();
	private ArrayList<String> diagramMessages = new ArrayList<String>();

	
	public TextPanel() {
		textArea = new JTextArea();
		setLayout(new BorderLayout());
		add(new JScrollPane(textArea),BorderLayout.CENTER);	
	}
	
	public void appendText(String text) {
		textArea.append(text);
	}
	
	
	public void clearText() {
		textArea.setText("");
		flushMessagesArray();
	}

	public ArrayList<String> getMessagesText() {
		return messagesText;
	}

	public void setMessagesArray(ArrayList<String> messagesArray) {
		messagesText = messagesArray;
	}

	private void flushMessagesArray() {
		messagesText.clear();
		diagramMessages.clear();
	}
	
	public void displayTextMessages() {
		
		SwingWorker<List<String>,Integer> worker = new SwingWorker<List<String>,Integer>() {
			
			@Override
			protected List<String> doInBackground() throws Exception {
				
				List<String> messages = new ArrayList<String>();
				int count = 0;
				for (String line : messagesText) {   
					appendText(line + "\n");
					messages.add(line);
					count++;
					publish(count);
				}
				return messages;
				
			}	
		};
		
		worker.execute();
		
	}
	
	public void generateDiagram(String fileName) throws Exception {
		
		try{
			
			SipTextViewer lTF = new SipTextViewer();
		    JCommander lJCommander = new JCommander(lTF,fileName,"-hsl");
		    if (lTF.isShowHelp()) {
		      // Displays help
		      lJCommander.usage();
		    } 
		    else {
		    
		    	if (lTF.isFileProvided()) {
		    	  diagramMessages = lTF.display(System.out);
		    	  displayDiagram();
		    	  //flushMessagesArray();
		    	} 
		    	
		    	else {
		    		appendText("You must specify a log file.");
		    		lJCommander.usage();
		    	}
		    }	
		    
		}
		catch(IOException e){
            e.printStackTrace();
        }
	}
	
	public void displayDiagram() {
		
		SwingWorker<List<String>,Integer> worker = new SwingWorker<List<String>,Integer>() {
			
			@Override
			protected List<String> doInBackground() throws Exception {
				
				List<String> messages = new ArrayList<String>();
				int count = 0;
				for (String line : diagramMessages) {   
					appendText(line + "\n");
					messages.add(line);
					count++;
					publish(count);
				}
				return messages;
				
			}	
		};
		
		worker.execute();
		
	}
	
	
}
