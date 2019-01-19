package com.mygdx.simulator.factory_methos_screens;

import com.badlogic.gdx.Screen;
import com.mygdx.controller.registration.LoginScreen;

public class LoginScreenCreator implements ScreenFactoryMethod{

	@Override
	public Screen makeScreen() {
		// TODO Auto-generated method stub
		return new LoginScreen();
	}

}
