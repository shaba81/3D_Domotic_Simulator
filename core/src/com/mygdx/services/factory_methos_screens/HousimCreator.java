package com.mygdx.services.factory_methos_screens;

import com.badlogic.gdx.Screen;
import com.mygdx.ui.graphics.game.Housim;

public class HousimCreator implements ScreenFactoryMethod{

	@Override
	public Screen makeScreen() {
		return Housim.getHousim();
	}

}
