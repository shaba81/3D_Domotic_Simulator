package com.mygdx.game;

import com.badlogic.gdx.Game;

public class Drop extends Game{

	@Override
	public void create() {
		
		this.setScreen(new MainMenuScreen(this));
		
	}
	
	@Override
	public void render() {
		super.render();
	}

}
