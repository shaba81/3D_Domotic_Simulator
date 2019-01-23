package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.simulator.factory_methos_screens.LogScreenCreator;
import com.mygdx.simulator.factory_methos_screens.LoginScreenCreator;

public class Drop extends Game {

	@Override
	public void create() {
		ScreenManager.getInstance().initialize(this);
//		ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN);
		setScreen(new com.mygdx.simulator.tween.Splash());
	}

	@Override
	public void render() {
		super.render();
	}

}
