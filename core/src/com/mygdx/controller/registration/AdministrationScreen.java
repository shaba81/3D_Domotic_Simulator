package com.mygdx.controller.registration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.ScreenManager;
import com.mygdx.interfaces.AbstractScreen;
import com.mygdx.simulator.factory_methos_screens.LogScreenCreator;
import com.mygdx.simulator.factory_methos_screens.RegistrationCredentialsScreenCreator;

import utilis.Utils;

public class AdministrationScreen extends AbstractScreen {

	private Table rightTable;

	private Table centerTable;

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
		

		centerTable = new Table();
		centerTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		centerTable.center();
		
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

		Label nameLabel = new Label("User:", skin);
		Image img_user_unclicked = new Image(new Texture("resources/images/a3.jpg"));
		Image img_user_clicked = new Image(new Texture("resources/images/a3.jpg"));
		ImageButton user1 = new ImageButton(img_user_unclicked.getDrawable(), img_user_clicked.getDrawable());
		user1.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				// accedo al panel dell'utente
			}

		});

		Label nameLabel2 = new Label("User:", skin);
		Image img_user_unclicked2 = new Image(new Texture("resources/images/a3.jpg"));
		Image img_user_clicked2 = new Image(new Texture("resources/images/a3.jpg"));
		ImageButton user2 = new ImageButton(img_user_unclicked2.getDrawable(), img_user_clicked2.getDrawable());
		user2.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				// accedo al panel dell'utente
			}

		});
		
		Label nameLabel3 = new Label("User:", skin);
		Image img_user_unclicked3= new Image(new Texture("resources/images/a3.jpg"));
		Image img_user_clicked3 = new Image(new Texture("resources/images/a3.jpg"));
		ImageButton user3= new ImageButton(img_user_unclicked3.getDrawable(), img_user_clicked3.getDrawable());
		user3.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				// accedo al panel dell'utente
			}

		});
		
		
		Label nameLabel4 = new Label("User:", skin);
		Image img_user_unclicked4 = new Image(new Texture("resources/images/a3.jpg"));
		Image img_user_clicked4 = new Image(new Texture("resources/images/a3.jpg"));
		ImageButton user4 = new ImageButton(img_user_unclicked4.getDrawable(), img_user_clicked4.getDrawable());
		user4.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				// accedo al panel dell'utente
			}

		});
		
		Label nameLabel5 = new Label("User:", skin);
		Image img_user_unclicked5 = new Image(new Texture("resources/images/a3.jpg"));
		Image img_user_clicked5 = new Image(new Texture("resources/images/a3.jpg"));
		ImageButton user5 = new ImageButton(img_user_unclicked5.getDrawable(), img_user_clicked5.getDrawable());
		user5.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				// accedo al panel dell'utente
			}

		});
		
		Label nameLabel6 = new Label("User:", skin);
		Image img_user_unclicked6 = new Image(new Texture("resources/images/a3.jpg"));
		Image img_user_clicked6 = new Image(new Texture("resources/images/a3.jpg"));
		ImageButton user6 = new ImageButton(img_user_unclicked6.getDrawable(), img_user_clicked6.getDrawable());
		user6.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				// accedo al panel dell'utente
			}

		});

		// label per distanziare lol
		Label dist = new Label("", skin);

		centerTable.add(nameLabel);
		centerTable.add(dist).width(62);
		centerTable.add(nameLabel2);

		// 1-1
		centerTable.row();
		centerTable.add(user1).size(120, 120);

		centerTable.add(dist).width(62);
		// 1-2
		centerTable.add(user2).size(120, 120).width(120);
		centerTable.row();

		centerTable.add(nameLabel3);
		centerTable.add(dist).width(62);
		centerTable.add(nameLabel4);
		// 2-1
		centerTable.row();
		centerTable.add(user3).size(120, 120);

		centerTable.add(dist).width(62);
		// 2-2
		centerTable.add(user4).size(120, 120).width(120);
		centerTable.row();

		centerTable.add(nameLabel5);
		centerTable.add(dist).width(62);
		centerTable.add(nameLabel6);
		
		// 3-1
		centerTable.row();
		centerTable.add(user5).size(120, 120);
		
		centerTable.add(dist).width(62);
		// 3-2
		centerTable.add(user6).size(120, 120).width(120);
		centerTable.row();
		
		
		
		
		
		
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
		stage.addActor(centerTable);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.batch.begin();

		this.batch.end();

		this.stage.act();
		this.stage.draw();

		if (this.registration) {
			this.registration = false;
//			Utils.resp = Utils.ADMIN_REGISTERS_A_NEW_USER;
			Utils.saveOnLog(Utils.ADMIN_REGISTERS_A_NEW_USER);
			ScreenManager.getInstance().showScreen(new RegistrationCredentialsScreenCreator());
		}

		if (this.showHouseInteraction) {
			this.showHouseInteraction = false;
			/*
			 * TODO SCREEN PER IL LOG
			 */
			ScreenManager.getInstance().showScreen(new LogScreenCreator());
		}

		if (this.back) {
			this.back = false;
			Utils.showPopUp(Utils.ADMINISTRATION_SCREEN_BACK_POPUP, skin, stage, Utils.MAIN_SCREEN_POP);
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
