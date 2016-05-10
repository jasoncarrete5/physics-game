package com.fwumdesoft.phys;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool.Poolable;

public class AirMolecule extends Actor implements Poolable {
	private static final TextureRegion texture;
	
	static {
		texture = new TextureRegion(Main.assets.get("textures/air.png", Texture.class));
	}
	
	private Polygon hitbox;
	
	/**
	 * No-arg constructor required for object pooling.
	 */
	public AirMolecule() {
		setSize(1f, 1f);
		setOrigin(Align.center);
		hitbox = new Polygon(new float[] {0, 0, getWidth(), 0, getWidth(), getHeight(), 0, getHeight()});
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	@Override
	protected void positionChanged() {
		hitbox.setPosition(getX(), getY());
	}
	
	@Override
	protected void rotationChanged() {
		hitbox.setRotation(getRotation());
	}
	
	public Polygon getHitbox() {
		return hitbox;
	}
	
	@Override
	public void reset() {
		hitbox.dirty();
	}
}
