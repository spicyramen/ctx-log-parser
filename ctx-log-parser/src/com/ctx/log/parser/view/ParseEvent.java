package com.ctx.log.parser.view;
import java.util.EventObject;


public class ParseEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	
	
	public ParseEvent(Object source, String text) {
		super(source);
		this.text = text;
		
	}
	
	public String getText() {
		return text;
	}

}
