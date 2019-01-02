package com.mygdx.registration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.interfaces.AbstractScreen;

import utilis.Utils;

public class FaceCaptureScreen extends AbstractScreen {

	private TextButton registrationButton;
	private TextButton backToRegistrationScreenButton;

	private boolean registration;
	private boolean backToRegistrationScreen;

	public FaceCaptureScreen() {

		this.registration = false;
		this.backToRegistrationScreen = false;
	}

	@Override
	public void show() {
		
		super.show();
		this.mainTable.center();
		this.registrationButton = new TextButton("SignIn", skin);
		this.backToRegistrationScreenButton = new TextButton("Back to registration screen", skin);

		// In ascolto di eventi
		this.registrationButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				registration = true;
			}
		});

		this.backToRegistrationScreenButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				backToRegistrationScreen = true;
			}
		});

		this.add(this.registrationButton);
		this.add(this.backToRegistrationScreenButton);

		// Aggiungo table allo stage
		stage.addActor(mainTable);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.batch.begin();

		this.batch.end();

		this.stage.act();
		this.stage.draw();

		if( this.registration ) {
			this.registration = false;
			/*
			 * TODO CHIAMATA A DB E SALVATAGGIO
			 */
			//ScreenManager.getInstance().showScreen(ScreenEnum.REGISTRATION_SCREEN);
		}

		if( this.backToRegistrationScreen ) {
			this.backToRegistrationScreen = false;
			Utils.showPopUp(Utils.FACE_CAPTURE_SCREEN_BACK_POPUP, skin, stage, this);
		}

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

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
