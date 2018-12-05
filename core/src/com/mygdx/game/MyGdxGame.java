package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame implements ApplicationListener {
	private PerspectiveCamera camera;
	private ModelBatch modelBatch;
	private Model model;
	private ModelInstance modelInstance;
	private Environment environment;
	private AnimationController controller;

	private float movementSpeed = 25f;
	private boolean forward = false;
	private boolean back = false;
	private boolean left = false;
	private boolean right = false;

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
		controller.allowSameAnimation = true;

		modelInstance.transform.scale(0.1f, 0.1f, 0.1f);
		modelInstance.transform.translate(0, -40, 0);

		// Finally we want some light, or we wont see our color. The environment gets
		// passed in during
		// the rendering process. Create one, then create an Ambient ( non-positioned,
		// non-directional ) light.
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));

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
					// controller.queue("other anim",-1,1f,null,0f);
				}

				@Override
				public void onLoop(AnimationController.AnimationDesc animation) {
				}
			});
		}
	}

	// public void moveCamera() {
	//
	// if (Gdx.input.isKeyPressed(Input.Keys.W)) {
	// camera.position.x += camera.direction.x * speed;
	// camera.position.z += camera.direction.z * speed;
	// }
	// if (Gdx.input.isKeyPressed(Input.Keys.S)) {
	// camera.position.x -= camera.direction.x * speed;
	// camera.position.z -= camera.direction.z * speed;
	// }
	// if (Gdx.input.isKeyPressed(Input.Keys.A)) {
	// camera.position.x += camera.direction.z * speed;
	// camera.position.z -= camera.direction.x * speed;
	// }
	// if (Gdx.input.isKeyPressed(Input.Keys.D)) {
	// camera.position.x -= camera.direction.z * speed;
	// camera.position.z += camera.direction.x * speed;
	// }
	// if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
	//
	// vect.rotate(1f, 0, 1, 0);
	// camera.lookAt(vect);
	//
	// }
	// if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
	// camera.direction.rotate(-1f, 0, 1, 0);
	// }
	// }

	public void walk(float timeElapsed) {
		float speed = movementSpeed;
		if ((forward | back) & (right | left)) {
			speed /= Math.sqrt(2);
		}
		System.out.println(speed);
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

	}

	@Override
	public void render() {
		// You've seen all this before, just be sure to clear the GL_DEPTH_BUFFER_BIT
		// when working in 3D
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		walk(Gdx.graphics.getDeltaTime());

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
