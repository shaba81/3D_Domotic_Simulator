package com.mygdx.application.screen_manager;

import com.badlogic.gdx.Screen;
import com.mygdx.services.factory_methos_screens.ScreenFactoryMethod;
import com.mygdx.ui.graphics.game.Drop;

public class ScreenManager {
	
	// Singleton: unique instance
    private static ScreenManager instance;
 
    // Reference to drop class
    private Drop game;
 
    // Singleton: private constructor
    private ScreenManager() {
        super();
    }
 
    // Singleton: retrieve instance
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }
    
    // Initialization with the game class
    public void initialize(Drop game) {
        this.game = game;
    }
    
    public void showScreen(ScreenFactoryMethod screenFactoryMethod)
    {
    	 // Get current screen to dispose it
        Screen currentScreen = game.getScreen();
 
        // Show new screen
        Screen newScreen = this.getScreen(screenFactoryMethod);
        game.setScreen(newScreen);
 
        // Dispose previous screen
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
    
    /**
     * It's the method that, passed it a string, returns its own screen.
     * @param nameScreen
     * @return
     */
    public Screen getScreen(ScreenFactoryMethod screenFactoryMethod)
    {
    	return screenFactoryMethod.makeScreen();
    }
    
}
