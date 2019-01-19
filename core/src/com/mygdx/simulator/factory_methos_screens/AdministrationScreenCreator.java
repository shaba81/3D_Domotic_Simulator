package com.mygdx.simulator.factory_methos_screens;

import com.badlogic.gdx.Screen;
import com.mygdx.controller.registration.AdministrationScreen;

public class AdministrationScreenCreator implements ScreenFactoryMethod{

	@Override
	public Screen makeScreen() {
		// TODO Auto-generated method stub
		return new AdministrationScreen();
	}

}
