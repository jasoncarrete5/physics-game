package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.utils.Align;

public class Wave extends HitboxActor {
	private Vector2 velocity;
	
	public Wave() {
		this(5, 20, 30);
	}
	
	public Wave(float width, float height, float speed) {
		setSize(width, height);
		setOrigin(Align.center);
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
		super.positionChanged();
		
		//tell air particles in hitbox to move
		for(Actor actor : getStage().getActors()) {
			if(actor instanceof AirMolecule) {
				AirMolecule air = (AirMolecule)actor;
				if(Intersector.overlapConvexPolygons(hitbox(), air.hitbox())) {
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
		super.rotationChanged();
		velocity.setAngle(getRotation());
	}
	
	public void setSpeed(float speed) {
		velocity.nor().scl(speed);
	}
	
	public float getSpeed() {
		return velocity.len();
	}
	
	@Override
	public String toString() {
		String result = super.toString();
		result += ": {X: " + getX() + ", Y: " + getY();
		result += ", Velocity: " + velocity + " Direction: " + getRotation();
		result += "}";
		return result;
	}
}
