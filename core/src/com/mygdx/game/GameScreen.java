package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

	// final Drop game;

	private PerspectiveCamera camera;
	private ModelBatch modelBatch;
	private Model entranceDoorModel;
	private ModelInstance entranceDoorInstance;
	private ModelInstance floorInstance;
	private Environment environment;
	private AnimationController controller;
	private ModelBuilder modelBuilder;
	private Material material; // used for the floor.
	private Texture floorTexture;
	private GameEntity player;

	private float movementSpeed = 25f;
	private boolean forward = false;
	private boolean back = false;
	private boolean left = false;
	private boolean right = false;

	// Width and Height of the room's floor.
	private float floorWidth = 120;
	private float floorHeight = 120;

	public GameScreen() {
		// this.game = game;

		// Create camera sized to screens width/height with Field of View of 75 degrees
		camera = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Move the camera 5 units back along the z-axis and look at the origin
		player = new GameEntity(0,50,20,20);
		camera.position.set(0f, 15f, 50f);
		camera.lookAt(0f, 0f, 0f);

		// Near and Far (plane) represent the minimum and maximum ranges of the camera
		// in, um, units
		camera.near = 0.1f;
		camera.far = 300.0f;

		// A ModelBatch is like a SpriteBatch, just for models. Use it to batch up
		// geometry for OpenGL

		modelBuilder = new ModelBuilder();

		// Model loader needs a binary json reader to decode
		UBJsonReader jsonReader = new UBJsonReader();
		// Create a model loader passing in our json reader
		G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
		// Now load the model by name
		// Note, the model (g3db file ) and textures need to be added to the assets
		// folder of the Android proj
		entranceDoorModel = modelLoader
				.loadModel(Gdx.files.getFileHandle("Door_Component_BI3.g3db", FileType.Internal));
		// Now create an instance. Instance holds the positioning data, etc of an
		// instance of your model
		entranceDoorInstance = new ModelInstance(entranceDoorModel);
		entranceDoorInstance.transform.scale(0.1f, 0.1f, 0.1f);
		entranceDoorInstance.transform.translate(0, 0, 0);

		// Floor Initialization
		material = new Material();
		floorTexture = new Texture(Gdx.files.internal("floorTexture.jpg"));
		material.set(new TextureAttribute(TextureAttribute.Diffuse, floorTexture));
		floorInstance = new ModelInstance(createPlaneModel(floorWidth, floorHeight, material, 0, 0, 1, 1));
		floorInstance.transform.translate(0, 0, -floorHeight / 2);
		floorInstance.transform.rotate(Vector3.X, 270);

		controller = new AnimationController(entranceDoorInstance);
		controller.allowSameAnimation = true;

		// Finally we want some light, or we wont see our color. The environment gets
		// passed in during
		// the rendering process. Create one, then create an Ambient ( non-positioned,
		// non-directional ) light.
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));
	}

	@Override
	public void show() {

		modelBatch = new ModelBatch();

		Gdx.input.setCursorCatched(true);
		Gdx.input.setInputProcessor(new InputProcessor() {
			private int dragX, dragY;
			float rotateSpeed = 0.05f;

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				Vector3 direction = camera.direction.cpy();

				// rotating on the y axis
				float x = dragX - screenX;

				camera.rotate(Vector3.Y, x * rotateSpeed);

				// rotating on the x and z axis is different
				float y = (float) Math.sin((double) (dragY - screenY) / 180f);
				if (Math.abs(camera.direction.y + y * (rotateSpeed * 5.0f)) < 0.9) {
					camera.direction.y += y * (rotateSpeed * 5.0f);
				}

				camera.update();
				dragX = screenX;
				dragY = screenY;

				Gdx.app.log(direction.toString(), "Direction Vector");
				return true;
			}

			@Override
			public boolean keyDown(int keycode) {
				if (keycode == Input.Keys.W) {
					forward = true;
				}
				if (keycode == Input.Keys.A) {
					left = true;
				}
				if (keycode == Input.Keys.S) {
					back = true;
				}
				if (keycode == Input.Keys.D) {
					right = true;
				}
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				if (keycode == Input.Keys.W) {
					forward = false;
				}
				if (keycode == Input.Keys.A) {
					left = false;
				}
				if (keycode == Input.Keys.S) {
					back = false;
				}
				if (keycode == Input.Keys.D) {
					right = false;
				}
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				// TODO Auto-generated method stub
				return false;
			}

		});
	}

	public void OpenDoor() {

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

			controller.setAnimation("Plane.001|Door", 1, new AnimationListener() {
				@Override
				public void onEnd(AnimationController.AnimationDesc animation) {
					// controller.queue("other anim",-1,1f,null,0f);
				}

				@Override
				public void onLoop(AnimationController.AnimationDesc animation) {
				}
			});
		}
	}

	public void walk(float timeElapsed) {
		float speed = movementSpeed;
		if ((forward | back) & (right | left)) {
			speed /= Math.sqrt(2);
		}

		if (forward) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.x *= speed * timeElapsed;
			v.z *= speed * timeElapsed;
			camera.translate(v);
			camera.update();
		}
		if (back) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.x = -v.x;
			v.z = -v.z;
			v.x *= speed * timeElapsed;
			v.z *= speed * timeElapsed;
			camera.translate(v);
			camera.update();
		}
		if (left) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.rotate(Vector3.Y, 90);
			v.x *= speed * timeElapsed;
			v.z *= speed * timeElapsed;
			camera.translate(v);
			camera.update();
		}
		if (right) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.rotate(Vector3.Y, -90);
			v.x *= speed * timeElapsed;
			v.z *= speed * timeElapsed;
			camera.translate(v);
			camera.update();

		}
		
		Gdx.app.log(camera.position.toString(), "Position Vector");

	}

	private Model createPlaneModel(final float width, final float height, final Material material, final float u1,
			final float v1, final float u2, final float v2) {

		modelBuilder.begin();
		MeshPartBuilder bPartBuilder = modelBuilder.part("rect", GL20.GL_TRIANGLES,
				Usage.Position | Usage.Normal | Usage.TextureCoordinates, material);
		// NOTE ON TEXTURE REGION, MAY FILL OTHER REGIONS, USE GET region.getU() and so
		// on
		bPartBuilder.setUVRange(u1, v1, u2, v2);
		bPartBuilder.rect(-(width * 0.5f), -(height * 0.5f), 0, (width * 0.5f), -(height * 0.5f), 0, (width * 0.5f),
				(height * 0.5f), 0, -(width * 0.5f), (height * 0.5f), 0, 0, 0, -1);

		return (modelBuilder.end());
	}

	@Override
	public void render(float delta) {
		// You've seen all this before, just be sure to clear the GL_DEPTH_BUFFER_BIT
		// when working in 3D
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		walk(Gdx.graphics.getDeltaTime());
		OpenDoor();

		controller.update(Gdx.graphics.getDeltaTime());
		modelBatch.begin(camera);

		modelBatch.render(entranceDoorInstance, environment);
		modelBatch.render(floorInstance, environment);

		modelBatch.end();

		// Use this to change Screen
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			ScreenManager.getInstance().showScreen(ScreenEnum.MAIN_MENU);
			// game.setScreen(game.menu);
		}
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		entranceDoorModel.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
}
