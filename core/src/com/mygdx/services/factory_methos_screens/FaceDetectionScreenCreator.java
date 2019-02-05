package com.mygdx.services.factory_methos_screens;

import com.badlogic.gdx.Screen;
import com.mygdx.ui.graphics.screen.FaceDetectionScreen;

public class FaceDetectionScreenCreator implements ScreenFactoryMethod{

	@Override
	public Screen makeScreen() {
		return new FaceDetectionScreen();
	}

}
