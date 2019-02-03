package com.mygdx.controller.proxy;

public abstract interface AbstractCommand {
	public void lightOn() throws Exception;
	public void tvOn() throws Exception;
	public void fanOn() throws Exception;
	public void speakerOn() throws Exception;
	public void openDoorA() throws Exception;
	public void openDoorB() throws Exception;
	
	public void lightOff() throws Exception;
	public void tvOff() throws Exception;
	public void fanOff() throws Exception;
	public void speakerOff() throws Exception;
	public void help()throws Exception;
	public void goToMainMenuScreen() throws Exception;
	public void safeBoxOn() throws Exception;
	public void safeBoxClose() throws Exception;
}
