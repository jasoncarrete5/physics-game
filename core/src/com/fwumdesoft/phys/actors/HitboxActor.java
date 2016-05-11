package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * An Actor object that has a rectangular hitbox {@link Polygon} that will match
 * all dimensions of the actor.
 */
public class HitboxActor extends Actor {
	private Polygon hitbox;
	
	public HitboxActor() {
		hitbox = new Polygon(new float[8]);
	}
	
	@Override
	protected void positionChanged() {
		hitbox.setPosition(getX(), getY());
	}
	
	@Override
	protected void rotationChanged() {
		hitbox.setRotation(getRotation());
	}
	
	@Override
	protected void sizeChanged() {
		float[] verts = hitbox.getVertices();
		verts[2] = getWidth();
		verts[4] = getWidth();
		verts[5] = getHeight();
		verts[7] = getHeight();
	}
	
	/** Called when this Actor's scale is changed. */
	protected void scaleChanged() {
		hitbox.setScale(getScaleX(), getScaleY());
	}
	
	/** Called when this Actor's origin is changed. */
	protected void originChanged() {
		hitbox.setOrigin(getOriginX(), getOriginY());
	}
	
	@Override
	public void scaleBy(float scale) {
		float lastScaleX = getScaleX(), lastScaleY = getScaleY();
		super.scaleBy(scale);
		if(lastScaleX != getScaleX() || lastScaleY != getScaleY()) {
			scaleChanged();
		}
	}
	
	@Override
	public void scaleBy(float scaleX, float scaleY) {
		float lastScaleX = getScaleX(), lastScaleY = getScaleY();
		super.scaleBy(scaleX, scaleY);
		if(lastScaleX != getScaleX() || lastScaleY != getScaleY()) {
			scaleChanged();
		}
	}
	
	@Override
	public void setScale(float scaleX, float scaleY) {
		if(scaleX != getScaleX() || scaleY != getScaleY()) {
			super.setScale(scaleX, scaleY);
			scaleChanged();
		}
	}
	
	@Override
	public void setScale(float scaleXY) {
		if(scaleXY != getScaleX() || scaleXY != getScaleY()) {
			super.setScale(scaleXY);
			scaleChanged();
		}
	}
	
	@Override
	public void setScaleX(float scaleX) {
		if(scaleX != getScaleX()) {
			super.setScaleX(scaleX);
			scaleChanged();
		}
	}
	
	@Override
	public void setScaleY(float scaleY) {
		if(scaleY != getScaleY()) {
			super.setScaleY(scaleY);
			scaleChanged();
		}
	}
	
	@Override
	public void setOrigin(float originX, float originY) {
		if(originX != getOriginX() || originY != getOriginY()) {
			super.setOrigin(originX, originY);
			originChanged();
		}
	}
	
	@Override
	public void setOrigin(int alignment) {
		float lastOriginX = getOriginX(), lastOriginY = getOriginY();
		super.setOrigin(alignment);
		if(lastOriginX != getOriginX() || lastOriginY != getOriginY()) {
			originChanged();
		}
	}
	
	@Override
	public void setOriginX(float originX) {
		if(originX != getOriginX()) {
			super.setOriginX(originX);
			originChanged();
		}
	}
	
	@Override
	public void setOriginY(float originY) {
		if(originY != getOriginY()) {
			super.setOriginY(originY);
			originChanged();
		}
	}
	
	/**
	 * @return This Actor's hitbox.
	 */
	public Polygon hitbox() {
		return hitbox;
	}
}
