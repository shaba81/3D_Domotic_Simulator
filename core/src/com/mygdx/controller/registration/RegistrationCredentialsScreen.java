package com.mygdx.controller.registration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.controller.Controller;
import com.mygdx.game.ScreenManager;
import com.mygdx.interfaces.AbstractScreen;
import com.mygdx.simulator.factory_methos_screens.FaceDetectionScreenCreator;

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

		
        
		if(!Utils.backToRegistrationScreen)
		{
		   this.txtEmail = new TextField("", skin);
		   this.txtTelephoneNumber = new TextField("", skin);
		   this.txtNickName = new TextField("", skin);
		}
		else
		{
			this.txtEmail = new TextField(Utils.credentials.get(0), skin);
			this.txtTelephoneNumber = new TextField(Utils.credentials.get(1), skin);
			this.txtNickName = new TextField(Utils.credentials.get(2), skin);
		}
		
		this.emailLabel = new Label("Email: ", skin);
		this.txtEmail.setMessageText("Ex: ciao@caro.it");

		this.telephoneNumberLabel = new Label("Telephone number: ", skin);
		this.txtTelephoneNumber.setMessageText("Ex: +390123456789");

		this.nickNameLabel = new Label("Nickname: ", skin);
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
				faceCapture = false;
				Utils.credentials.add(0, this.txtEmail.getText());
				Utils.credentials.add(1, this.txtTelephoneNumber.getText());
				Utils.credentials.add(2, this.txtNickName.getText());

				/*
				 * This function return: 1: if email already exist. 2: if telephone number
				 * already exist. 3: if nickname already exist.
				 */
				int result = Controller.getController().getUserDAO().userExist(Utils.credentials.get(0), Utils.credentials.get(1),
						Utils.credentials.get(2));

				String dialogText = this.errorOccurr(result);

				if (dialogText != "")
					Utils.showMessageDialog(dialogText, skin, stage);
				else
				{
//					Utils.resp = Utils.REGISTRATION_CREDENTIALS_SUCCESSFULLY_INSERT;
					Utils.saveOnLog(Utils.REGISTRATION_CREDENTIALS_SUCCESSFULLY_INSERT);
					ScreenManager.getInstance().showScreen(new FaceDetectionScreenCreator());
				}
			}

			if (backToAdministration) {
				backToAdministration = false;
				if( !Utils.isFirstAccess )
					Utils.showPopUp(Utils.REGISTRATION_CREDENTIALS_SCREEN_BACK_POPUP, skin, stage, Utils.ADMIN_SCREEN_POP);
				else {
					Utils.showPopUp(Utils.SCREEN_BACK_GAME_POPUP, skin, stage, Utils.GAME_SCREEN_POP);
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
		else if (  !Utils.credentials.get(2).equals("")  && result == 3)
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_NICKNAME_EXIST_POPUP;
		else if (Utils.credentials.get(0).equals("") || Utils.credentials.get(1).equals("")
				|| Utils.credentials.get(2).equals(""))
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_MISSING_CRED_POPUP;
		else if (!Utils.credentials.get(0).contains("@"))
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_MISSING_AT_EMAIL_POPUP;
		else if (!Utils.credentials.get(0).contains("."))
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_MISSING_POINT_EMAIL_POPUP;
		else if (!Utils.credentials.get(1).matches("[0-9+]+"))
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_NUMBER_CONTAINS_LETTER_POPUP;
		else if (Utils.credentials.get(1).length() != 13)
			resultText = Utils.REGISTRATION_CREDENTIALS_SCREEN_NUMBER_TOO_SHORT_POPUP;
		else if (Utils.credentials.get(1).charAt(0) != '+')
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
