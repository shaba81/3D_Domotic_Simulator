package com.mygdx.interfaces;

import java.security.Principal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.controller.Controller;
import com.mygdx.controller.database.model.User;
import com.mygdx.controller.database.persistence.dao.UserDAO;
import com.mygdx.controller.proxy.UserAdministratorCommand;
import com.mygdx.controller.proxy.UserCommand;
import com.mygdx.game.GameScreen;
import com.mygdx.game.ScreenManager;
import com.mygdx.simulator.email.EmailSender;
import com.mygdx.simulator.factory_methos_screens.GameScreenCreator;

import utilis.Utils;

public abstract class AbstractScreen implements Screen {

	protected OrthographicCamera camera;
	protected SpriteBatch batch;
	protected Viewport viewport;
	protected Stage stage;
	protected TextureAtlas atlas;
	protected Skin skin;
	protected Table mainTable;
	protected TextButton backButton;
	protected boolean back;
	protected String emailOneTime;

	public AbstractScreen() {
		// TODO Auto-generated constructor stub
		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

		this.camera.update();
		this.viewport.apply();

		this.atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.atlas"));
		this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

		Gdx.input.setCursorCatched(false);
		emailOneTime = "";
		this.back = false;
	}

	public void show() {

		this.batch = new SpriteBatch();
		this.stage = new Stage(this.viewport, this.batch);
		Gdx.input.setInputProcessor(this.stage);

		// Creo table
		this.mainTable = new Table();
		// Dico alla table di riempire lo stage
		this.mainTable.setFillParent(true);
		// Allineo le cose nella table
//		this.mainTable.center();
	}
	
	public void add(Label x) {
		this.mainTable.add(x);
		this.mainTable.row();
	}

	public void add(TextField x) {
		this.mainTable.add(x);
		this.mainTable.row();
	}

	public void add(TextButton x) {
		this.mainTable.add(x);
		this.mainTable.row();
	}

	public void showRecoveryAccessDialog(String text, Skin skin, Stage stage, TextField textInput, boolean isEmail) {

		System.out.println("MMMM");
		Dialog dialog = new Dialog("", skin, "dialog") {
			public void result(Object obj) {
				try {
					if (obj.equals("true")) {
						if (isEmail) {
							String email = textInput.getText();
							System.out.println("Email da mandare a: " + textInput.getText());
							if (email.equals(""))
								showRecoveryAccessDialog(Utils.ACCESS_FAILED_THREE_TIMES, skin, stage,
										new TextField("", skin), true);
							else {
								if (Controller.getController().getUserDAO().emailIsRegister(email)) {
									String pass = Controller.getController().generatePassword();
									String bodyMessage = Utils.MESSAGE_ONE_TIME_PASS + "\n\n\n One-time-pass: " + pass;

									EmailSender.sendMessage(email, Utils.OGJ_ONE_TIME_PASS, bodyMessage);
									Controller.getController().getUserDAO().updateOneTimePass(pass, email);
									emailOneTime = email;

									showRecoveryAccessDialog(Utils.ACCESS_ONE_TIME_PASS, skin, stage,
											new TextField("", skin), false);
								} else {
									showRecoveryAccessDialog(Utils.ACCESS_RECOVERY_EMAIL_NOT_FOUND, skin, stage,
											new TextField("", skin), true);
								}
							}
						} else {
							System.out.println("Password da validare: " + textInput.getText());
							String password = textInput.getText();
							if (Controller.getController().getUserDAO().validateUserOneTimePAss(password, emailOneTime)) {
								/*
								 * TO DO User and command for GAME SCREEN, fare la query con la mail
								 */
								User user = Controller.getController().getUserDAO().getUserByEmail(emailOneTime);
								String idUser = Utils.getIdUserFromImage(user.getPathImage());
								user.setIdUser(idUser);
								Utils.userLogged = idUser;
								Utils.saveOnLog(Utils.ACCESS_SUCCESS_LOG);

								/*
								 * Proxy o non proxy decision
								 */
								GameScreen.getGameScreen().setUser(user);

								if( user.isAdministrator() )
									GameScreen.getGameScreen().setCommand(new UserAdministratorCommand());
								else
									GameScreen.getGameScreen().setCommand(new UserCommand());

								ScreenManager.getInstance().showScreen(new GameScreenCreator());
							} else {
								showRecoveryAccessDialog(Utils.ACCESS_ONE_TIME_PASS_NOT_MATCH, skin, stage,
										new TextField("", skin), false);
							}
						}
					}
				} catch (Exception e) {
					System.err.println(e.getStackTrace());
				}
			}
		};

		dialog.button("SEND", "true");
		Label emailLabel = new Label(text, skin);
		dialog.getContentTable().row();
		dialog.getContentTable().add(emailLabel).left();
		dialog.getContentTable().row();
		dialog.getContentTable().add(textInput);
		dialog.show(stage);
		stage.addActor(dialog);
		stage.setKeyboardFocus(textInput);
	}

	@Override
	public void dispose() {
		System.out.println("SUPER");
		batch.dispose();
		stage.dispose();
		skin.dispose();
		atlas.dispose();

	}

}
