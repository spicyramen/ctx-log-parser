package com.ctx.log.parser.view;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;


public class FormPanel extends JPanel  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel nameLabel = new JLabel("Calls Processed: ");
	JLabel totalCallsLabel = new JLabel("0");
	JLabel messagesLabel = new JLabel("Messages Processed: ");
	JLabel totalMessageslLabel = new JLabel("0");
	
	
	public FormPanel() {
		Dimension dim = getPreferredSize();
		dim.width = 200;
		dim.height = 300;
		setPreferredSize(dim);
		Border innerBorder = BorderFactory.createTitledBorder("Call information");
		Border outterBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);	
		setBorder(BorderFactory.createCompoundBorder(outterBorder, innerBorder));
		
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.anchor = GridBagConstraints.LINE_END;
		gc.gridx = 0;
		gc.gridy = 0;
		add(nameLabel,gc);
		
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 1;
		gc.gridy = 0;
		add(totalCallsLabel,gc);
		
		gc.anchor = GridBagConstraints.LINE_END;
		gc.gridx = 0;
		gc.gridy = 1;
		add(messagesLabel,gc);
		
		gc.anchor = GridBagConstraints.LAST_LINE_END;
		gc.gridx = 1;
		gc.gridy = 1;
		add(totalMessageslLabel,gc);
		
			
	}
	
	public void setTotalCalls(int totalCalls) {
		totalCallsLabel.setText(Integer.toString(totalCalls));
	}
	
	public void setTotalMessages(int totalCalls) {
		totalMessageslLabel.setText(Integer.toString(totalCalls));
	}
	
}
