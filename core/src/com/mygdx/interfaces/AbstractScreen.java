package com.mygdx.interfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

//////////7
public abstract class AbstractScreen implements Screen{
	
	protected OrthographicCamera camera;
	protected SpriteBatch batch;
	protected Viewport viewport;
	protected Stage stage;
	protected TextureAtlas atlas;
	protected Skin skin;
	protected Table mainTable;

	public AbstractScreen() {
		// TODO Auto-generated constructor stub
		this.camera = new OrthographicCamera();
		this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

		this.camera.update();
		this.viewport.apply();
		
		this.atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
		this.skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
		
		Gdx.input.setCursorCatched(false);
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
	
	public void add(Label x)
	{
		this.mainTable.add(x);
		this.mainTable.row();
	}
	
	public void add(TextField x)
	{
		this.mainTable.add(x);
		this.mainTable.row();
	}
	
	public void add(TextButton x)
	{
		this.mainTable.add(x);
		this.mainTable.row();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
		skin.dispose();
		atlas.dispose();

	}

}

