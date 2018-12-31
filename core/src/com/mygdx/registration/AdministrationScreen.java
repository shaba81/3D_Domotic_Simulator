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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ScreenEnum;
import com.mygdx.game.ScreenManager;

import utilis.Utils;

public class AdministrationScreen implements Screen{

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Viewport viewport;
	private Stage stage;
	private TextureAtlas atlas;
	protected Skin skin;

	private TextButton registrationButton;
	private TextButton showHouseInteractionButton;
	private TextButton backButton;

	private boolean registration;
	private boolean showHouseInteraction;
	private boolean back;

	public AdministrationScreen() {
		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

		this.camera.update();
		this.viewport.apply();

		this.atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
		this.skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);

		this.registration = false;
		this.showHouseInteraction = false;
		this.back = false;
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

		this.registrationButton = new TextButton("Registration", skin);
		this.showHouseInteractionButton = new TextButton("Show house interaction", skin);
		this.backButton = new TextButton("Back to Menu", skin);

		// In ascolto di eventi
		this.registrationButton.addListener(new ClickListener() {
			@Override
				public void clicked(InputEvent event, float x, float y) {
					registration = true;
				}
		});

		this.showHouseInteractionButton.addListener(new ClickListener() {
			@Override
				public void clicked(InputEvent event, float x, float y) {
					showHouseInteraction = true;
				}
		});

		this.backButton.addListener(new ClickListener() {
			@Override
				public void clicked(InputEvent event, float x, float y) {
					back = true;
				}
		});

		mainTable.add(this.registrationButton);
		mainTable.row();
		mainTable.add(this.showHouseInteractionButton);
		mainTable.row();
		mainTable.add(this.backButton);
		mainTable.row();

		mainTable.setFillParent(true);
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
			ScreenManager.getInstance().showScreen(ScreenEnum.REGISTRATION_CREDENTIALS_SCREEN);
		}

		if( this.showHouseInteraction ) {
			this.showHouseInteraction = false;
			/*
			 * TODO SCREEN PER IL LOG
			 */
			//ScreenManager.getInstance().showScreen(ScreenEnum.SHOW_INTERACTION_MENU);
		}

		if( this.back ) {
			this.back = false;
			Utils.showPopUp(Utils.ADMINISTRATION_SCREEN_BACK_POPUP, skin, stage, this);
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
