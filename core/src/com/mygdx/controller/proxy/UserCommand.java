package com.mygdx.controller.proxy;

public class UserCommand implements AbstractCommand {

	UserAdministratorCommand uac = new UserAdministratorCommand();
	

	@Override
	public void lightOn() {
		uac.lightOn();
	}

	@Override
	public void tvOn() {
		throw new CommandPermissionException();

	}

	@Override
	public void fanOn() {
		uac.fanOn();

	}

	@Override
	public void speakerOn() {
		uac.speakerOn();
		
	}

	@Override
	public void lightOff() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tvOff() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fanOff() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void speakerOff() {
		// TODO Auto-generated method stub
		
	}



}
