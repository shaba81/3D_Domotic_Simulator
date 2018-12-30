package com.mygdx.registration;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ScreenEnum;
import com.mygdx.game.ScreenManager;

public class FaceCaptureScreen implements Screen {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Viewport viewport;
	private Stage stage;
	private TextureAtlas atlas;
	protected Skin skin;

	private TextButton registrationButton;
	private TextButton backToRegistrationScreenButton;

	private boolean registration;
	private boolean backToRegistrationScreen;

	public FaceCaptureScreen() {
		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

		this.camera.update();
		this.viewport.apply();

		this.atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
		this.skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);

		this.registration = false;
		this.backToRegistrationScreen = false;
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

		mainTable.add(this.registrationButton);
		mainTable.row();
		mainTable.add(this.backToRegistrationScreenButton);
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

		if( this.registration ) {
			this.registration = false;
			/*
			 * TODO CHIAMATA A DB E SALVATAGGIO
			 */
			//ScreenManager.getInstance().showScreen(ScreenEnum.REGISTRATION_SCREEN);
		}

		if( this.backToRegistrationScreen ) {
			this.backToRegistrationScreen = false;
			ScreenManager.getInstance().showScreen(ScreenEnum.REGISTRATION_SCREEN);
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
