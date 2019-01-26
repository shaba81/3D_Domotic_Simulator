package com.mygdx.simulator.factory_methos_screens;

import com.badlogic.gdx.Screen;
import com.mygdx.controller.registration.CredentialsChangeScreen;

public class CredentialsChangeScreenCreator implements ScreenFactoryMethod {

	@Override
	public Screen makeScreen() {
		return new CredentialsChangeScreen();
	}

}
