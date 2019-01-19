package com.mygdx.simulator.factory_methos_screens;

import com.badlogic.gdx.Screen;
import com.mygdx.controller.registration.RegistrationCredentialsScreen;

public class RegistrationCredentialsScreenCreator implements ScreenFactoryMethod{

	@Override
	public Screen makeScreen() {
		// TODO Auto-generated method stub
		return new RegistrationCredentialsScreen();
	}

}
