package com.mygdx.controller.proxy;

public class CommandPermissionException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommandPermissionException() {
		super();
	}
	
	public CommandPermissionException(String message) {
		super(message);
	}
}
