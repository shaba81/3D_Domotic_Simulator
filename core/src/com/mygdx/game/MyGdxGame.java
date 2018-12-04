package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.utils.UBJsonReader;

public class MyGdxGame implements ApplicationListener {
	private PerspectiveCamera camera;
	private ModelBatch modelBatch;
	private Model model;
	private ModelInstance modelInstance;
	private Environment environment;
	private AnimationController controller;
	private float speed = 0.3f;

	@Override
	public void create() {
		// Create camera sized to screens width/height with Field of View of 75 degrees
		camera = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Move the camera 5 units back along the z-axis and look at the origin
		camera.position.set(0f, 10f, 50f);
		camera.lookAt(0f, 0f, 0f);

		// Near and Far (plane) represent the minimum and maximum ranges of the camera
		// in, um, units
		camera.near = 0.1f;
		camera.far = 300.0f;

		// A ModelBatch is like a SpriteBatch, just for models. Use it to batch up
		// geometry for OpenGL
		modelBatch = new ModelBatch();

		// Model loader needs a binary json reader to decode
		UBJsonReader jsonReader = new UBJsonReader();
		// Create a model loader passing in our json reader
		G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
		// Now load the model by name
		// Note, the model (g3db file ) and textures need to be added to the assets
		// folder of the Android proj
		model = modelLoader.loadModel(Gdx.files.getFileHandle("Door_Component_BI3.g3db", FileType.Internal));
		// Now create an instance. Instance holds the positioning data, etc of an
		// instance of your model
		modelInstance = new ModelInstance(model);
		
		controller = new AnimationController(modelInstance);

		modelInstance.transform.scale(0.1f, 0.1f, 0.1f);
		modelInstance.transform.translate(0, -40, 0);

		// Finally we want some light, or we wont see our color. The environment gets
		// passed in during
		// the rendering process. Create one, then create an Ambient ( non-positioned,
		// non-directional ) light.
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));

	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		model.dispose();
	}
	
	public void OpenDoor() {

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			
			controller.setAnimation("Plane.001|Door", 1, new AnimationListener() {
				@Override
				public void onEnd(AnimationController.AnimationDesc animation) {
					//controller.queue("other anim",-1,1f,null,0f);
				}

				@Override
				public void onLoop(AnimationController.AnimationDesc animation) {
				}
			});
		}
	}

	public void moveCamera() {

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.position.x += camera.direction.x * speed;
			camera.position.z += camera.direction.z * speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.position.x -= camera.direction.x * speed;
			camera.position.z -= camera.direction.z * speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.position.x += camera.direction.z * speed;
			camera.position.z -= camera.direction.x * speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.position.x -= camera.direction.z * speed;
			camera.position.z += camera.direction.x * speed;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.direction.rotate(1f, 0, 1, 0);

		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.direction.rotate(-1f, 0, 1, 0);
		}
	}

	@Override
	public void render() {
		// You've seen all this before, just be sure to clear the GL_DEPTH_BUFFER_BIT
		// when working in 3D
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		moveCamera();
		camera.update();

		// You need to call update on the animation controller so it will advance the
		// animation. Pass in frame delta
		OpenDoor();
		controller.update(Gdx.graphics.getDeltaTime());
		// Like spriteBatch, just with models! pass in the box Instance and the
		// environment
		modelBatch.begin(camera);
		modelBatch.render(modelInstance, environment);
		modelBatch.end();
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
}
