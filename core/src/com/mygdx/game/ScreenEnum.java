package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.mygdx.registration.AdministrationScreen;
import com.mygdx.registration.FaceCaptureScreen;
import com.mygdx.registration.LoginScreen;
import com.mygdx.registration.RegistrationScreen;

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
	LOGIN_SCREEN {
		public Screen getScreen() {
			return new LoginScreen();
		}
	},
	ADMINISTRATION_SCREEN {
		public Screen getScreen() {
			return new AdministrationScreen();
		}
	},
	REGISTRATION_SCREEN{
		public Screen getScreen() {
			return new RegistrationScreen();
		}
	},
	FACE_CAPTURE_SCREEN{
		public Screen getScreen() {
			return new FaceCaptureScreen();
		}
	}
	;
	public abstract Screen getScreen();

}
