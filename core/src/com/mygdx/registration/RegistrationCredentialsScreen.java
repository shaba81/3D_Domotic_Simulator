package com.mygdx.registration;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.database.persistence.PostgreDAOFactory;
import com.mygdx.database.persistence.dao.UserDAO;
import com.mygdx.game.ScreenEnum;
import com.mygdx.game.ScreenManager;
import com.mygdx.interfaces.AbstractScreen;

import utilis.Utils;

public class RegistrationCredentialsScreen extends AbstractScreen {

	private Label emailLabel;
	private Label telephoneNumberLabel;
	private Label nickNameLabel;

	private TextField txtEmail;
	private TextField txtTelephoneNumber;
	private TextField txtNickName;
	private ArrayList<String> credentials;

	private TextButton faceCaptureButton;
	private boolean faceCapture;

	private TextButton backToAdministrationButton;
	private boolean backToAdministration;

	public RegistrationCredentialsScreen() {

		this.faceCapture = false;
		this.backToAdministration = false;
		this.credentials = new ArrayList<String>();
	}

	@Override
	public void show() {

		super.show();
		this.mainTable.center();
		this.faceCaptureButton = new TextButton("Face capture", skin);
		this.backToAdministrationButton = new TextButton("Back to Administration", skin);

		this.faceCaptureButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				faceCapture = true;
			}
		});

		this.backToAdministrationButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				backToAdministration = true;
			}
		});

		this.emailLabel = new Label("Email: ", skin);
		this.txtEmail = new TextField("", skin);
		this.txtEmail.setMessageText("Ex: ciao@caro.it");

		this.telephoneNumberLabel = new Label("Telephone number: ", skin);
		this.txtTelephoneNumber = new TextField("", skin);
		this.txtTelephoneNumber.setMessageText("Ex: +390123456789");

		this.nickNameLabel = new Label("Nickname: ", skin);
		this.txtNickName = new TextField("", skin);
		this.txtNickName.setMessageText("Ex: Maria");

		this.add(this.emailLabel);
		this.add(this.txtEmail);
		this.add(this.telephoneNumberLabel);
		this.add(this.txtTelephoneNumber);
		this.add(this.nickNameLabel);
		this.add(this.txtNickName);
		this.add(this.faceCaptureButton);
		this.add(this.backToAdministrationButton);

		// Aggiungo table allo stage
		stage.addActor(mainTable);
	}

	@Override
	public void render(float delta) {
		try {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			this.batch.begin();

			this.batch.end();

			this.stage.act();
			this.stage.draw();

			if (faceCapture) {
				PostgreDAOFactory postgreDAOFactory = new PostgreDAOFactory();
				UserDAO utenteDAO = postgreDAOFactory.getUtenteDAO();
				faceCapture = false;
				this.credentials.add(0, this.txtEmail.getText());
				this.credentials.add(1, this.txtTelephoneNumber.getText());
				this.credentials.add(2, this.txtNickName.getText());

				/*
				 * This function return: 1: if email already exist. 2: if telephone number
				 * already exist. 3: if nickname already exist.
				 */
				int result = utenteDAO.userExist(this.credentials.get(0), this.credentials.get(1),
						this.credentials.get(2));

				if (result == 1)
					Utils.showMessageDialog(Utils.REGISTRATION_CREDENTIALS_SCREEN_EMAIL_EXIST_POPUP, skin, stage);
				else if (result == 2)
					Utils.showMessageDialog(Utils.REGISTRATION_CREDENTIALS_SCREEN_TELEPHONE_EXIST_POPUP, skin, stage);
				else if (result == 3)
					Utils.showMessageDialog(Utils.REGISTRATION_CREDENTIALS_SCREEN_NICKNAME_EXIST_POPUP, skin, stage);
				if (this.credentials.get(0).equals("") || this.credentials.get(1).equals("")
						|| this.credentials.get(1).equals(""))
					Utils.showMessageDialog(Utils.REGISTRATION_CREDENTIALS_SCREEN_MISSING_CRED_POPUP, skin, stage);
				else if (!this.credentials.get(0).contains("@") || !this.credentials.get(0).contains("."))
					Utils.showMessageDialog(Utils.REGISTRATION_CREDENTIALS_SCREEN_BAD_EMAIL_FORMAT_POPUP, skin, stage);
				else if ( !this.credentials.get(1).matches("[0-9+]+") || this.credentials.get(1).length() != 13 || this.credentials.get(1).charAt(0) != '+')
					Utils.showMessageDialog(Utils.REGISTRATION_CREDENTIALS_SCREEN_BAD_NUMBER_FORMAT_POPUP, skin, stage);
				else
					ScreenManager.getInstance().showScreen(ScreenEnum.FACE_CAPTURE_SCREEN);

			}

			if (backToAdministration) {
				backToAdministration = false;
				Utils.showPopUp(Utils.REGISTRATION_CREDENTIALS_SCREEN_BACK_POPUP, skin, stage, this);
			}
		} catch (Exception e) {
			// TODO: handle exception
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
