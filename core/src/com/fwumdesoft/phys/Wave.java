package com.fwumdesoft.phys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public class Wave extends Actor {
	private Polygon hitbox;
	private Vector2 velocity;
	
	public Wave(float width, float height, float speed) {
		setSize(width, height);
		setOrigin(Align.center);
		hitbox = new Polygon();
		velocity = Vector2.X.cpy().scl(speed);
	}
	
	/**
	 * Sets the wave in motion at the specified position and direction.
	 * @param startX 
	 * @param startY
	 * @param direction
	 */
	public void propagate(float startX, float startY, float direction) {
		if(getStage() == null) {
			Gdx.app.error("Wave", "Stage is null. Cannot propagate.");
			return;
		}
		
		setPosition(startX, startY);
		setRotation(direction);
		addAction(Actions.repeat(20, Actions.moveBy(velocity.x, velocity.y, 1f, Interpolation.linear)));
	}
	
	@Override
	protected void positionChanged() {
		hitbox.setPosition(getX(), getY());
		
		//TODO tell all air molecules to move if they are within the hitbox
	}
	
	@Override
	protected void rotationChanged() {
		hitbox.setRotation(getRotation());
		velocity.setAngle(getRotation());
	}
	
	@Override
	public String toString() {
		String result = super.toString();
		result += ": {X: " + getX() + ", Y: " + getY();
		result += " Speed: " + velocity.len() + " Direction: " + getRotation();
		result += "}";
		return result;
	}
}
