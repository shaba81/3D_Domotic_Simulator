package com.mygdx.controller.registration;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.controller.database.persistence.PostgreDAOFactory;
import com.mygdx.controller.database.persistence.dao.UserDAO;
import com.mygdx.game.ScreenEnum;
import com.mygdx.game.ScreenManager;
import com.mygdx.interfaces.AbstractScreen;

import utilis.Utils;

public class LoginScreen extends AbstractScreen {

	private Label idLabel;
	private Label pWordLabel;

	private TextField txtId;
	private TextField txtPassword;

	private ArrayList<String> credentials;

	private TextButton backButton;
	private boolean back = false;

	private TextButton accessButton;
	private boolean access = false;

	public LoginScreen() {

		credentials = new ArrayList<String>();
	}

	@Override
	public void show() {

		super.show();
		this.mainTable.center();
		backButton = new TextButton("Back to Menu", skin);
		accessButton = new TextButton("Access", skin);

		// In ascolto di eventi
		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				back = true;
			}
		});

		accessButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				access = true;
			}
		});

		idLabel = new Label("Id: ", skin);
		pWordLabel = new Label("Password: ", skin);

		txtId = new TextField("", skin);
		txtId.setMessageText("Id");

		txtPassword = new TextField("", skin);
		txtPassword.setPasswordCharacter('*');
		txtPassword.setPasswordMode(true);
		txtPassword.setMessageText("PassWord");

		this.add(idLabel);
		this.add(txtId);
		this.add(pWordLabel);
		this.add(txtPassword);
		this.add(accessButton);
		this.add(backButton);

		// Aggiungo table allo stage
		stage.addActor(mainTable);

	}

	@Override
	public void render(float delta) {
		try {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			batch.begin();

			batch.end();

			stage.act();
			stage.draw();

			if (back) {
				back = false;
				Utils.showPopUp(Utils.LOGIN_SCREEN_BACK_POPUP, skin, stage, this);
			}

			if (access) {
				this.access = false;
				// to do accesso
				this.credentials.add(0,this.txtId.getText());
				this.credentials.add(1,txtPassword.getText());
				PostgreDAOFactory postgreDAOFactory = new PostgreDAOFactory();
				UserDAO utenteDAO = postgreDAOFactory.getUtenteDAO();
				if( utenteDAO.validateUserAdminCredentials(this.credentials.get(1), this.credentials.get(0)) ) {
					ScreenManager.getInstance().showScreen(ScreenEnum.ADMINISTRATION_SCREEN);
				}
				else {
					Utils.showMessageDialog(Utils.LOGIN_SCREEN_WRONG_CREDENTIAL_POPUP, skin, stage);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
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
