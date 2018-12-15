package com.mygdx.game;

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
		Vector3 v = new Vector3(x,0,z);
		return v;
	}

	public String isColliding(GameEntity g) {
		float dx = (x + width / 2) - (g.x + g.width / 2);
		float dy = (z + height / 2) - (g.z + g.height / 2);
		float w = (width + g.width);
		float h = (height + g.height);
		float crossWidth = width * dy;
		float crossHeight = height * dx;
		String collision = "none";

		  if(Math.abs(dx)<=width && Math.abs(dy)<=height){
		        if(crossWidth>crossHeight){
		            collision=(crossWidth>(-crossHeight))?"bottom":"left";
		        }else{
		            collision=(crossWidth>-(crossHeight))?"right":"top";
		        }
		    }
		
		return collision;
	}
}
