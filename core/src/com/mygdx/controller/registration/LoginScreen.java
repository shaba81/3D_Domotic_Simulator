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
import com.mygdx.controller.email.EmailSender;
import com.mygdx.controller.sms.SmsSender;
import com.mygdx.game.ScreenEnum;
import com.mygdx.game.ScreenManager;
import com.mygdx.interfaces.AbstractScreen;

import utilis.Configuration;
import utilis.Utils;

public class LoginScreen extends AbstractScreen {

	private Label idLabel;
	private Label pWordLabel;

	private TextField txtId;
	private TextField txtPassword;

	private ArrayList<String> credentials;

	private TextButton backButton;
	private boolean back;

	private TextButton accessButton;
	private boolean access;
	private boolean isFirstAccessInternal;
	private int countFailedLogin;

	public LoginScreen() {

		this.back = false;
		this.access = false;
		this.isFirstAccessInternal = true;
		this.countFailedLogin = 0;
		this.credentials = new ArrayList<String>();
	}

	@Override
	public void show() {

		super.show();
		this.mainTable.center();
		String text = "Back to Administration";
		this.backButton = new TextButton("", skin);

		if( Utils.isFirstAccess )
			text = "Back to simulation";

		this.backButton.setText(text);
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

			if(this.isFirstAccessInternal && Utils.isFirstAccess) {
				this.isFirstAccessInternal = false;
				Utils.showMessageDialog(Utils.LOGIN_SCREEN_FIRST_ACCESS_POPUP, skin, stage);
			}

			if (back && !Utils.isFirstAccess) {
				back = false;
				Utils.showPopUp(Utils.LOGIN_SCREEN_BACK_POPUP, skin, stage, "login_back_adm");
			}

			if (back && Utils.isFirstAccess) {
				back = false;
				Utils.showPopUp(Utils.SCREEN_BACK_GAME_POPUP, skin, stage, "login_back_game");
			}

			if (access) {
				this.access = false;

				this.credentials.add(0,this.txtId.getText());
				this.credentials.add(1,txtPassword.getText());
				PostgreDAOFactory postgreDAOFactory = new PostgreDAOFactory();
				UserDAO utenteDAO = postgreDAOFactory.getUtenteDAO();
				if( this.credentials.get(0).equals("") ) {
					Utils.showMessageDialog(Utils.LOGIN_SCREEN_NO_ID_INSERT_POPUP, skin, stage);
				}
				else if ( this.credentials.get(1).equals("") ) {
					Utils.showMessageDialog(Utils.LOGIN_SCREEN_NO_PASSWORD_INSERT_POPUP, skin, stage);
				}
				else if( utenteDAO.validateUserAdminCredentials(this.credentials.get(1), this.credentials.get(0)) ) {
					if( !Utils.isFirstAccess )
						ScreenManager.getInstance().showScreen(ScreenEnum.ADMINISTRATION_SCREEN);
					else{
						ScreenManager.getInstance().showScreen(ScreenEnum.FACE_DETECTION_SCREEN);
					}
				}
				else {

					if( this.countFailedLogin == 3 ) {
						this.countFailedLogin = 0;
						Utils.showMessageDialog(Utils.LOGIN_SCREEN_TOO_MANY_FAILED_ATTEMPTS_POPUP,skin, stage);
						this.updateAndSendCredentials(utenteDAO);
					}
					else {						
						++this.countFailedLogin;
						Utils.showMessageDialog(Utils.LOGIN_SCREEN_WRONG_CREDENTIAL_POPUP, skin, stage);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void updateAndSendCredentials(UserDAO utenteDAO) throws Exception {
		String newPass = Utils.generatePasswordAdministrator();
		String[] emailTelephoneNumeberAdmin = utenteDAO.updateCredentilsAdministrator(Utils.ID_USER, Utils.ID_SUPPLY, newPass);
		String bodyMessage = Utils.MESSAGE_RECOVERY_PASS_ADMIN_EMAIL + "\n\n\n New password: " + newPass;
		String bodyMessageSMS = Utils.MESSAGE_RECOVERY_PASS_ADMIN_SMS + "\nNew password: " + newPass;

		EmailSender.sendMessage(emailTelephoneNumeberAdmin[0], Utils.OBJ_EMAIL_RECOVERY_PASS_ADMIN, bodyMessage);
		SmsSender.sendSms(bodyMessageSMS,emailTelephoneNumeberAdmin[1]);
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
