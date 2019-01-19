package com.mygdx.game;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.interfaces.AbstractScreen;
import com.mygdx.simulator.factory_methos_screens.FaceDetectionScreenCreator;
import com.mygdx.simulator.factory_methos_screens.LoginScreenCreator;

import utilis.Utils;

public class MainMenuScreen extends AbstractScreen {

	private Texture title;
	private TextButton playButton;
	private TextButton administrationButton;
	private TextButton exitButton;

	private boolean access;
	private boolean administration;
	private boolean back;

	public MainMenuScreen() {
		this.access = false;
		this.administration = false;
		this.back = false;
//		System.out.println("hola: "+Gdx.files.getLocalStoragePath());
	}

	@Override
	public void show() {

		super.show();
		mainTable.bottom();
        
		title = new Texture(Gdx.files.internal("title.png"));
		// Creo bottoni
		playButton = new TextButton("Access", skin);
		administrationButton = new TextButton("Administration", skin);
		exitButton = new TextButton("Exit", skin);

		// In ascolto di eventi
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				access = true;
			}
		});
		administrationButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				administration = true;
			}
		});
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				back = true;
			}
		});

		// Aggiungo bottoni alla table
		this.add(playButton);
		this.add(administrationButton);
		this.add(exitButton);

		// Aggiungo table allo stage
		this.stage.addActor(mainTable);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(title, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2, 700, 150);
		batch.end();

		stage.act();
		stage.draw();

		if (access) {
			access = false;
			Utils.isAccess = true;
			ScreenManager.getInstance().showScreen(new FaceDetectionScreenCreator());
//			ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN);
		}

		if (administration) {
			Utils.isAccess = false;
			administration = false;
			ScreenManager.getInstance().showScreen(new LoginScreenCreator());
		}

		if( back ) {
			Utils.isAccess = false;
			back = false;
			Utils.showPopUp(Utils.MAIN_MENU_BACK_POPUP, skin, stage, "main_menu_screen");
		}

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

}
