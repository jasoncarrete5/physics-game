package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.utils.Align;

public class Wave extends HitboxActor {
	private Vector2 velocity;
	
	public Wave() {
		this(3, 6, 30);
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
		
		for(Actor actor : getStage().getActors()) {
			if(actor instanceof AirMolecule) { //tell air particles in hitbox to move
				AirMolecule air = (AirMolecule)actor;
				if(Intersector.overlapConvexPolygons(hitbox(), air.hitbox())) {
					if(air.hasActions()) continue; //TODO Implement a way for waves to interfere
					Action moveForward = Actions.moveBy(velocity.x, velocity.y, 1f, Interpolation.linear);
					Action moveBackward = Actions.moveBy(-velocity.x, -velocity.y, 1f, Interpolation.sine);
					air.addAction(Actions.sequence(moveForward, moveBackward));
				}
			} else if(actor instanceof Reflector) {
				Reflector refl = (Reflector)actor;
				if(refl.hitbox().contains(getX(Align.center), getY(Align.center))) { //reflect off reflector with proper angle of reflection
					clearActions();
					Vector2 normal = refl.getNormal();
					float incidence = velocity.angle(normal);
					Action moveBack = Actions.moveBy(-velocity.x * 0.1f, -velocity.y * 0.1f);
					Action propagate = Actions.run(() -> {
						propagate(getX(), getY(), getRotation() + 180 + 2 * incidence);
					});
					addAction(Actions.sequence(moveBack, propagate));
				}
			} else if(actor instanceof Refractor) {
				Refractor refr = (Refractor)actor;
				if(refr.hitbox().contains(getX(Align.center), getY(Align.center))) { //Refract through the refractor
					clearActions();
					Vector2 normal = refr.getNormal();
					float refractionAngle = refr.getRefractionAngle(velocity.angle(normal));
					Action moveOutOfRefractor = Actions.moveBy((refr.getWidth() + 1) * MathUtils.cosDeg(getRotation()),
							(refr.getHeight() + 1) * MathUtils.sinDeg(getRotation()));
					Action propagate = Actions.run(() -> {
						propagate(getX(), getY(), 2 * getRotation() - refractionAngle);
					});
					addAction(Actions.sequence(moveOutOfRefractor, propagate));
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
