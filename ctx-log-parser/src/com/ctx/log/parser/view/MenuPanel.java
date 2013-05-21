package com.ctx.log.parser.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;


public class MenuPanel extends JFrame implements ActionListener  {
	
	
	private static final long serialVersionUID = 1L;
	private JMenuBar menuBar;
	private JFileChooser fileChooser;
	private File fileSelected;
	private AnalysisListener analysisListener;
	private EventListenerList listenerList = new EventListenerList();
	private String versionApp = "";
	
	/**
	 * 
	 */

	
	public MenuPanel(){
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("File");
		JMenu systemMenu = new JMenu("System");		
		JMenu aboutMenu = new JMenu("About");
	    
	    menuBar.add(fileMenu);
	    menuBar.add(systemMenu);  
	    menuBar.add(aboutMenu);
	    
	    JMenuItem openFileAction = new JMenuItem("Open File");
	    JMenuItem appendFileAction = new JMenuItem("Append File");
	    JMenuItem openFolderAction = new JMenuItem("Open Folder");
	    JMenuItem exitAction = new JMenuItem("Exit");
	    
	    JMenuItem runAction = new JMenuItem("Start Analysis");
	    JMenuItem displayAction = new JMenuItem("Display Calls");
	    
	    JMenuItem prefsAction = new JMenuItem("Preferences");
	    JMenuItem aboutAction = new JMenuItem("About");
	    
	    
	    fileMenu.add(openFileAction); 
	    fileMenu.add(appendFileAction); 
	    fileMenu.add(openFolderAction); 
	    fileMenu.add(runAction);
	    fileMenu.add(displayAction);
	    fileMenu.add(exitAction);
	    
	    systemMenu.add(prefsAction);
	    aboutMenu.add(aboutAction);
	    
	    
	    
	    openFileAction.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	fileChooser = new JFileChooser();
        	    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        	    fileChooser.setSelectedFile(fileChooser.getCurrentDirectory());
        	    fileChooser.setDialogTitle("File Chooser");
        	    fileChooser.setMultiSelectionEnabled(false);  
        	    int retValueFile = fileChooser.showOpenDialog(MenuPanel.this);
        	    AnalysisFormEvent loadFile = new AnalysisFormEvent("LOAD_FILE");
        	    
        	    if (retValueFile == JFileChooser.APPROVE_OPTION) {
        	    	fileSelected = fileChooser.getSelectedFile();
        	    	SendParseEvent(new ParseEvent(this,"INFO Loading file...\n"));
        	    	loadFile.setFileName(fileSelected.getAbsolutePath());
        	    	initAnalysisEvent(loadFile);
        	    }   
        	    
            }
        });
	    
	    runAction.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	initAnalysisEvent(new AnalysisFormEvent("PROCESS_LOG_FILE"));
            }
        });
	    
	    displayAction.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	initAnalysisEvent(new AnalysisFormEvent("START_DISPLAY_CALLS"));
            }
        });
	    
	    aboutAction.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	JOptionPane.showMessageDialog(getParent(), "SIP Log analyzer \n Version: " + versionApp ,"About SIP Log analyzer",
            		    JOptionPane.INFORMATION_MESSAGE);
            }
        });
	    
	    exitAction.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
	    
	   
	    
	}
	

	public JMenuBar returnMenuBar() {
		return menuBar;
	}

	public void setAnalysisListener(AnalysisListener analysisListener) {
		this.analysisListener = analysisListener;
	}
	
	public void initAnalysisEvent(AnalysisFormEvent event) {
		if(analysisListener != null) {
			analysisListener.sendEventLogAnalysis(event);
		}
	}
	
	
	public void SendParseEvent(ParseEvent event) {
		Object[] listeners = listenerList.getListenerList();
		for (int i=0; i< listeners.length;i+=2) {
			if(listeners[i] == ParseListener.class) {
				((ParseListener)listeners[i+1]).ParseEventOccurred(event);
			}
		}
	}

	public void setVersion (String version) {
		this.versionApp = version;
	}
	
	public void addParseListener(ParseListener listener) {
		listenerList.add(ParseListener.class, listener);
	}

	public void deleteParseListener(ParseListener listener) {
		listenerList.remove(ParseListener.class, listener);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
