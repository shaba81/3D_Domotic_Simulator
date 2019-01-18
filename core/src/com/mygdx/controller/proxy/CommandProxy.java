package com.mygdx.controller.proxy;

public class CommandProxy implements AbstractCommand{
	
	private boolean typeUser;
	private AbstractCommand abstractCommand;
	
	public CommandProxy(boolean typeUser) {
		this.typeUser = typeUser;
	}

	@Override
	public void getCommand() {
		if (typeUser) {
			abstractCommand = new UserAdministratorCommand();
			
		}else {
			abstractCommand = new UserCommand();
		}
		
		
	}
	
	

}
