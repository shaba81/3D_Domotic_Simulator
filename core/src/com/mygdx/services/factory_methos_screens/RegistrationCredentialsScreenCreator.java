package com.mygdx.services.factory_methos_screens;

import com.badlogic.gdx.Screen;
import com.mygdx.ui.graphics.screen.RegistrationCredentialsScreen;

public class RegistrationCredentialsScreenCreator implements ScreenFactoryMethod{

	@Override
	public Screen makeScreen() {
		return new RegistrationCredentialsScreen();
	}

}
