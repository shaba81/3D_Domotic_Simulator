package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class GameEntity {

	public float x;
	public float z;
	public float width;
	public float height;

	public GameEntity(float _x, float _z, float _width, float _height) {
		x = _x;
		z = _z;
		width = _width;
		height = _height;
	}

	public void updatePosition(float _x, float _z) {
		x = _x;
		z = _z;
	}

	public Vector3 getPosition() {
		Vector3 v = new Vector3(x, 0, z);
		return v;
	}

	public Rectangle getBounds() {
		return new Rectangle(Math.abs(x), Math.abs(z), width, height);
	}

	public boolean isColliding(GameEntity g) {
		Rectangle thisRect = this.getBounds();
		Rectangle otherRect = g.getBounds();

		if (thisRect.overlaps(otherRect))
			return true;

		return false;
	}
	
	public boolean contains(GameEntity g) {
		Rectangle thisRect = new Rectangle(x,z,width,height);
		Rectangle otherRect = new Rectangle(g.x,g.z,g.width,g.height);

		if (thisRect.contains(otherRect))
			return true;

		return false;
	}
}
