package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.mygdx.controller.registration.AdministrationScreen;
import com.mygdx.controller.registration.FaceDetectionScreen;
import com.mygdx.controller.registration.LoginScreen;
import com.mygdx.controller.registration.RegistrationCredentialsScreen;

public enum ScreenEnum {

	MAIN_MENU {
		public Screen getScreen() {
			return new MainMenuScreen();
		}
	},
	GAME_SCREEN {
		private Screen gameScreen;

		public Screen getScreen() {
			if (gameScreen == null)
				gameScreen = new GameScreen();

			return gameScreen;
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
	REGISTRATION_CREDENTIALS_SCREEN {
		public Screen getScreen() {
			return new RegistrationCredentialsScreen();
		}
	},
	FACE_DETECTION_SCREEN {
		public Screen getScreen() {
			return new FaceDetectionScreen();
		}
	};
	public abstract Screen getScreen();

}
