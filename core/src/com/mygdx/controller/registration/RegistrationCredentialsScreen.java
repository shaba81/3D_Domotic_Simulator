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

public class RegistrationCredentialsScreen extends AbstractScreen {

	private Label emailLabel;
	private Label telephoneNumberLabel;
	private Label nickNameLabel;

	private TextField txtEmail;
	private TextField txtTelephoneNumber;
	private TextField txtNickName;

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
		String text = "Back to Administration";
		this.backToAdministrationButton = new TextButton("", skin);

		if( Utils.isFirstAccess )
			text = "Back to simulation";

		this.backToAdministrationButton.setText(text);

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

				String dialogText = this.errorOccurr(result);

				if (dialogText != "")
					Utils.showMessageDialog(dialogText, skin, stage);
				else
					ScreenManager.getInstance().showScreen(ScreenEnum.FACE_CAPTURE_SCREEN);

			}

			if (backToAdministration) {
				backToAdministration = false;
				if( !Utils.isFirstAccess )
					Utils.showPopUp(Utils.REGISTRATION_CREDENTIALS_SCREEN_BACK_POPUP, skin, stage, "registration_credentials_screen");
				else {
					Utils.showPopUp(Utils.SCREEN_BACK_GAME_POPUP, skin, stage, "registration_credentials_screen_first_acc");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private String errorOccurr(int result) {
		String resultText = "";
		if (result == 1)
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_EMAIL_EXIST_POPUP;
		else if (result == 2)
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_TELEPHONE_EXIST_POPUP;
		else if (  !this.credentials.get(2).equals("")  && result == 3)
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_NICKNAME_EXIST_POPUP;
		else if (this.credentials.get(0).equals("") || this.credentials.get(1).equals("")
				|| this.credentials.get(2).equals(""))
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_MISSING_CRED_POPUP;
		else if (!this.credentials.get(0).contains("@"))
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_MISSING_AT_EMAIL_POPUP;
		else if (!this.credentials.get(0).contains("."))
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_MISSING_POINT_EMAIL_POPUP;
		else if (!this.credentials.get(1).matches("[0-9+]+"))
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_NUMBER_CONTAINS_LETTER_POPUP;
		else if (this.credentials.get(1).length() != 13)
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_NUMBER_TOO_SHORT_POPUP;
		else if (this.credentials.get(1).charAt(0) != '+')
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_MISSIN_PLUS_NUMBER_POPUP;

		return resultText;
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
