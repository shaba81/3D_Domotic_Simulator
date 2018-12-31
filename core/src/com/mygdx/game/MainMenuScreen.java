package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

import utilis.Utils;

public class MainMenuScreen implements Screen {

	// final Drop game;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Viewport viewport;
	private Stage stage;

	private TextureAtlas atlas;
	protected Skin skin;

	private Texture title;
	private TextButton playButton;
	private TextButton administrationButton;
	private TextButton exitButton;

	private boolean play = false;
	private boolean administration = false;
	private boolean back;

	public MainMenuScreen() {
		// this.game = game;

		camera = new OrthographicCamera();
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

		camera.update();
		viewport.apply();

		atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
		skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);

		this.back = false;

	}

	@Override
	public void show() {

		batch = new SpriteBatch();
		stage = new Stage(viewport, batch);
		Gdx.input.setInputProcessor(stage);

		title = new Texture(Gdx.files.internal("title.png"));
		// Creo table
		Table mainTable = new Table();
		// Dico alla table di riempire lo stage
		mainTable.setFillParent(true);
		// Allineo le cose nella table
		mainTable.bottom();

		// Creo bottoni
		playButton = new TextButton("Play", skin);
		administrationButton = new TextButton("Administration", skin);
		exitButton = new TextButton("Exit", skin);

		// In ascolto di eventi
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				play = true;
			}
		});
		administrationButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				administration = true;
			}
		});
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				back = true;
			}
		});

		// Aggiungo bottoni alla table
		mainTable.add(playButton);
		mainTable.row();
		mainTable.add(administrationButton);
		mainTable.row();
		mainTable.add(exitButton);

		// Aggiungo table allo stage
		stage.addActor(mainTable);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(title, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2, 700, 150);
		batch.end();

		stage.act();
		stage.draw();

		if (play) {
			play = false;
			ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN);
		}

		if (administration) {
			administration = false;
			ScreenManager.getInstance().showScreen(ScreenEnum.LOGIN_SCREEN);
		}

		if( back ) {
			back = false;
			Utils.showPopUp(Utils.MAIN_MENU_BACK_POPUP, skin, stage, this);
		}

	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		title.dispose();
		skin.dispose();
		atlas.dispose();

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		camera.update();

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
