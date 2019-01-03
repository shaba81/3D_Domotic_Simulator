package com.mygdx.controller.registration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.controller.database.model.User;
import com.mygdx.controller.database.persistence.PostgreDAOFactory;
import com.mygdx.controller.database.persistence.dao.UserDAO;
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

			if (this.registration) {
				this.registration = false;
				PostgreDAOFactory postgreDAOFactory = new PostgreDAOFactory();
				UserDAO userDAO = postgreDAOFactory.getUtenteDAO();

				/*
				 * Decommentare le funzioni per il salvataggio.
				 * Ora se si preme il bottone + come se simulasse la registrazione. quindi la booleana la mette a false.
				 * Ma successivamente sarà messa all'interno del metodo così da settarla solo quando effettivamente il 
				 * salvataggio è avvenuto con successo e questo controllo lo facciamo dentro la funzione.
				 */
				if (Utils.isFirstAccess) {
					System.out.println("FIRST ACCESS");
//					this.registrationUser(userDAO, true);
					Utils.isFirstAccess = false;
				} else {
					System.out.println("NORMAL USER REGISTRATION");
//					this.registrationUser(userDAO, false);
				}
			}

			if (this.backToRegistrationScreen) {
				this.backToRegistrationScreen = false;
				Utils.showPopUp(Utils.FACE_CAPTURE_SCREEN_BACK_POPUP, skin, stage, "face_capture_screen");
			}
	}

	/**
	 * Save User into DB. 
	 * 
	 * @param userDAO
	 * @param isAdministrator
	 */
	private void registrationUser(UserDAO userDAO, boolean isAdministrator) {
		try {
			/*
			 * TODO: fare distinzione di popup, quando fallisce va benisismo. Quando ha successo farlo accedere 
			 */
			User user = new User();
			user.setEmail(this.credentials.get(0));
			user.setAdministrator(isAdministrator);
			user.setNickName(this.credentials.get(2));
			user.setTelefonNumber(this.credentials.get(1));
			user.setPathImage(this.credentials.get(3));

			if (userDAO.registration(user))
				Utils.showMessageDialog(Utils.REGISTRATION_SUCCESS_POPUP, skin, stage);
			else {
				Utils.showMessageDialog(Utils.REGISTRATION_FAILED_POPUP, skin, stage);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
