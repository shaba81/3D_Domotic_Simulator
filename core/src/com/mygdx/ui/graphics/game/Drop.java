package com.mygdx.ui.graphics.game;

import com.badlogic.gdx.Game;
import com.mygdx.application.screen_manager.ScreenManager;
import com.mygdx.services.factory_methos_screens.LogScreenCreator;
import com.mygdx.services.factory_methos_screens.LoginScreenCreator;

public class Drop extends Game {

	@Override
	public void create() {
		ScreenManager.getInstance().initialize(this);
		setScreen(new com.mygdx.ui.graphics.game.Splash());
	}

	@Override
	public void render() {
		super.render();
	}

}
