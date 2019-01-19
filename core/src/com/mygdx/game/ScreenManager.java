package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.mygdx.simulator.factory_methos_screens.ScreenFactoryMethod;

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
    
    
 // Show in the game the screen which enum type is received
//    public void showScreen(ScreenEnum screenEnum) {
// 
//        // Get current screen to dispose it
//        Screen currentScreen = game.getScreen();
// 
//        // Show new screen
//        Screen newScreen = screenEnum.getScreen();
//        game.setScreen(newScreen);
// 
//        // Dispose previous screen
//        if (currentScreen != null) {
//            currentScreen.dispose();
//        }
//    }
    
    //Returns the screen based on the screenEnum passed to it
//    public Screen getScreen(ScreenEnum screenEnum)
//    {
//    	return screenEnum.getScreen();
//    }

}
