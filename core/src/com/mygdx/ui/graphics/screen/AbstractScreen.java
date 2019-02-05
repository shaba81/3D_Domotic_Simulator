package com.mygdx.ui.graphics.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.application.exception_manager.ExceptionsManager;
import com.mygdx.application.screen_manager.ScreenManager;
import com.mygdx.domain.controller.Controller;
import com.mygdx.foundation.utils.Utils;
import com.mygdx.services.email.EmailSender;
import com.mygdx.services.factory_methos_screens.HousimCreator;
import com.mygdx.ui.graphics.game.Housim;

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
	
	protected void updateFrame(Table imageTable, Texture frameTexture, TextureRegion imgRegion, Image img) {
		imageTable.clear();
		frameTexture = new Texture(Gdx.files.internal("resources/frame.jpg"));
		imgRegion.setRegion(frameTexture);
		img = new Image(imgRegion);
		imageTable.add(img).center();
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

		Dialog dialog = new Dialog("", skin, "dialog") {
			public void result(Object obj) {
				try {
					if (obj.equals("true")) {
						if (isEmail) {
							String email = textInput.getText();
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
							String password = textInput.getText();
							if (Controller.getController().getUserDAO().validateUserOneTimePAss(password,
									emailOneTime)) {
								Controller.getController().getFaceController().setUserAndCommandAccess(emailOneTime,
										true);

								ScreenManager.getInstance().showScreen(new HousimCreator());
								Utils.doorIsOpen = true;
								Housim.getHousim().inputManager.openDoorA = true;
								Housim.getHousim().entrata = true;
								Housim.getHousim().cont = 0;
								Housim.getHousim().cont1 = 0;
							} else {
								showRecoveryAccessDialog(Utils.ACCESS_ONE_TIME_PASS_NOT_MATCH, skin, stage,
										new TextField("", skin), false);
							}
						}
					}
				} catch (Exception e) {
					ExceptionsManager.getExceptionsManager().manageException(e,skin,stage);
				}
			}
		};

		dialog.button("SEND", "true");
		dialog.key(Keys.ESCAPE, "false");
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
		batch.dispose();
		stage.dispose();
		skin.dispose();
		atlas.dispose();

	}

}
