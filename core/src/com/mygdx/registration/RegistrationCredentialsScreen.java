package com.mygdx.registration;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ScreenEnum;
import com.mygdx.game.ScreenManager;

public class RegistrationScreen implements Screen{

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Viewport viewport;
	private Stage stage;
	private TextureAtlas atlas;
	protected Skin skin;

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

	public RegistrationScreen() {
		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

		this.camera.update();
		this.viewport.apply();

		this.atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
		this.skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);

		this.faceCapture = false;
		this.backToAdministration = false;
	}

	
	@Override
	public void show() {
		this.batch = new SpriteBatch();
		this.stage = new Stage(this.viewport, this.batch);
		Gdx.input.setInputProcessor(this.stage);

		// Creo table
		Table mainTable = new Table();
		// Dico alla table di riempire lo stage
		mainTable.setFillParent(true);
		// Allineo le cose nella table
		mainTable.center();

		this.faceCaptureButton = new TextButton("Face capture", skin);
		this.backToAdministrationButton= new TextButton("Back to Administration", skin);

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
		this.txtEmail.setMessageText("Email");

		this.telephoneNumberLabel = new Label("Telephone number: ", skin);
		this.txtTelephoneNumber = new TextField("", skin);
		this.txtTelephoneNumber.setMessageText("Telephone number");

		this.nickNameLabel = new Label("Nickname: ", skin);
		this.txtNickName = new TextField("", skin);
		this.txtNickName.setMessageText("Nickname");

		mainTable.add(this.emailLabel);
		mainTable.row();
		mainTable.add(this.txtEmail);
		mainTable.row();
		mainTable.add(this.telephoneNumberLabel);
		mainTable.row();
		mainTable.add(this.txtTelephoneNumber);
		mainTable.row();
		mainTable.add(this.nickNameLabel);
		mainTable.row();
		mainTable.add(this.txtNickName);
		mainTable.row();
		mainTable.add(this.faceCaptureButton);
		mainTable.row();
		mainTable.add(this.backToAdministrationButton);
		mainTable.row();

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

		if( faceCapture ) {
			faceCapture = false;
			ScreenManager.getInstance().showScreen(ScreenEnum.FACE_CAPTURE_SCREEN);
		}

		if( backToAdministration ) {
			backToAdministration = false;
			ScreenManager.getInstance().showScreen(ScreenEnum.ADMINISTRATION_SCREEN);
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

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		skin.dispose();
		atlas.dispose();
	}

}
