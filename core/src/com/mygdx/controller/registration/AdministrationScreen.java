package com.mygdx.controller.registration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.ScreenEnum;
import com.mygdx.game.ScreenManager;
import com.mygdx.interfaces.AbstractScreen;

import utilis.Utils;

public class AdministrationScreen extends AbstractScreen{

	private Table rightTable;

	private TextButton registrationButton;
	private TextButton showHouseInteractionButton;
	private TextButton backButton;

	private boolean registration;
	private boolean showHouseInteraction;
	private boolean back;

	public AdministrationScreen() {

		this.registration = false;
		this.showHouseInteraction = false;
		this.back = false;

		rightTable = new Table();
		rightTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		rightTable.center();
		rightTable.debug();
	}

	
	
	@Override
	public void show() {

		super.show();
		this.mainTable.center();
		this.rightTable.right();

		this.registrationButton = new TextButton("Registration", skin);
		this.showHouseInteractionButton = new TextButton("Show house interaction", skin);
		this.backButton = new TextButton("Back to Menu", skin);

		// In ascolto di eventi
		this.registrationButton.addListener(new ClickListener() {
			@Override
				public void clicked(InputEvent event, float x, float y) {
					registration = true;
				}
		});

		this.showHouseInteractionButton.addListener(new ClickListener() {
			@Override
				public void clicked(InputEvent event, float x, float y) {
					showHouseInteraction = true;
				}
		});

		this.backButton.addListener(new ClickListener() {
			@Override
				public void clicked(InputEvent event, float x, float y) {
					back = true;
				}
		});

		this.rightTable.add(this.registrationButton);
		this.rightTable.row();
		this.rightTable.add(this.showHouseInteractionButton);
		this.rightTable.row();
		this.rightTable.add(this.backButton);
		this.rightTable.row();

		mainTable.setFillParent(true);
		// Aggiungo table allo stage
		stage.addActor(mainTable);
		stage.addActor(this.rightTable);
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
			ScreenManager.getInstance().showScreen(ScreenEnum.REGISTRATION_CREDENTIALS_SCREEN);
		}

		if( this.showHouseInteraction ) {
			this.showHouseInteraction = false;
			/*
			 * TODO SCREEN PER IL LOG
			 */
			//ScreenManager.getInstance().showScreen(ScreenEnum.SHOW_INTERACTION_MENU);
		}

		if( this.back ) {
			this.back = false;
			Utils.showPopUp(Utils.ADMINISTRATION_SCREEN_BACK_POPUP, skin, stage, "admin_screen");
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
