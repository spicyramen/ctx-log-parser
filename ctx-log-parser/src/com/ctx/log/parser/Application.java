package com.ctx.log.parser;

import javax.swing.SwingUtilities;
import com.ctx.log.parser.controller.Engine;
import com.ctx.log.parser.model.SipParser;
import com.ctx.log.parser.view.MainFrame;

/**
 * Purpose: Parse SIP Logs
 * 
 * @author Gonzalo Gasca Meza
 * University of Oxford
 * Department of Computer Science, Wolfson Building,  
 * Parks Rd, Oxford OX1, United Kingdom
 * +44 1865 273838
 * gonzalo.gasca.meza@cs.ox.ac.uk
    
 */

public class Application {
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {	
				runApp();
			}			
		});
	}
	
	public static void runApp() {
		SipParser model = new SipParser();
		MainFrame ui = new MainFrame(model);
		Engine controller = new Engine(model,ui);	
		ui.initMethod(controller);
	
	}
}
