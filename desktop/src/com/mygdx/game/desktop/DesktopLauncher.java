package com.mygdx.game.desktop;

import org.opencv.core.Core;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.controller.Controller;
import com.mygdx.game.Drop;

import utilis.ExceptionsManager;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "House Simulator";
		config.width = 1366;
		config.height = 768;

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new LwjglApplication(new Drop(), config);
	}
}