package com.mygdx.game;

import com.badlogic.gdx.Game;

public class Drop extends Game{

	@Override
	public void create() {
		ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().showScreen( ScreenEnum.MAIN_MENU );
		
	}
	
	@Override
	public void render() {
		super.render();
	}

}
