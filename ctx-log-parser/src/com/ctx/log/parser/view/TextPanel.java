package com.ctx.log.parser.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;


public class TextPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> MessagesText;
	
	
	
	private JTextArea textArea;

	
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
	}

	public ArrayList<String> getMessagesText() {
		return MessagesText;
	}

	public void setMessagesArray(ArrayList<String> messagesText) {
		MessagesText = messagesText;
	}

	
	public void displayTextMessages() {
		
		SwingWorker<List<String>,Integer> worker = new SwingWorker<List<String>,Integer>() {

			
			@Override
			protected List<String> doInBackground() throws Exception {
				
				List<String> messages = new ArrayList<String>();
				int count = 0;
				
				for (String line : MessagesText) {   
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
