package com.mygdx.game;

import com.badlogic.gdx.Screen;

public enum ScreenEnum {

	MAIN_MENU {
		public Screen getScreen() {
			return new MainMenuScreen();
		}
	},
	GAME_SCREEN {
		public Screen getScreen() {
			return new GameScreen();
		}
	},
	REGISTER_SCREEN {
		public Screen getScreen() {
			return new RegisterScreen();
		}
	}
	;
	public abstract Screen getScreen();

}
