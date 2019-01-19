package com.mygdx.simulator.factory_methos_screens;

import com.badlogic.gdx.Screen;
import com.mygdx.game.GameScreen;

public class GameScreenCreator implements ScreenFactoryMethod{

	@Override
	public Screen makeScreen() {
		// TODO Auto-generated method stub
		return GameScreen.getGameScreen();
	}

}
