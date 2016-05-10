package com.fwumdesoft.phys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.utils.Align;

public class Wave extends Actor {
	private Polygon hitbox;
	private Vector2 velocity;
	
	public Wave(float width, float height, float speed) {
		setSize(width, height);
		setOrigin(Align.center);
		hitbox = new Polygon(new float[] {0, 0, getWidth(), 0, getWidth(), getHeight(), 0, getHeight()});
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
		final MoveByAction moveAction = Actions.moveBy(velocity.x, velocity.y, 1f, Interpolation.linear);
		Action updateSpeed = Actions.run(() -> {
			moveAction.setAmount(velocity.x, velocity.y);
		});
		addAction(Actions.forever(Actions.parallel(moveAction, updateSpeed)));
	}
	
	@Override
	protected void positionChanged() {
		hitbox.setPosition(getX(), getY());
		
		//tell air particles in hitbox to move
		for(Actor actor : getStage().getActors()) {
			if(actor instanceof AirMolecule) {
				AirMolecule air = (AirMolecule)actor;
				if(Intersector.overlapConvexPolygons(hitbox, air.getHitbox())) {
					if(air.hasActions())
						continue;
					Action moveForward = Actions.moveBy(velocity.x, velocity.y, 1f, Interpolation.linear);
					Action moveBackward = Actions.moveBy(-velocity.x, -velocity.y, 1f, Interpolation.sine);
					air.addAction(Actions.sequence(moveForward, moveBackward));
				}
			}
		}
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
