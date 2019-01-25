package com.mygdx.controller.proxy;

public abstract interface AbstractCommand {
	public void lightOn();
	public void tvOn();
	public void fanOn();
	public void speakerOn();
	
	public void lightOff();
	public void tvOff();
	public void fanOff();
	public void speakerOff();
	
	public void goToMainMenuScreen();
	
}
