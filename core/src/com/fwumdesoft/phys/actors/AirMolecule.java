package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.fwumdesoft.phys.Main;

public class AirMolecule extends HitboxActor implements Poolable {
	private static final TextureRegion texture;
	
	static {
		texture = new TextureRegion(Main.asset.get(Main.Assets.AIR));
	}
	
	/**
	 * No-arg constructor required for object pooling.
	 */
	public AirMolecule() {
		setSize(1.5f, 1.5f);
		setOrigin(Align.center);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	@Override
	public void reset() {
		hitbox().dirty();
	}
	
	@Override
	public String toString() {
		String result = super.toString();
		result += ": {X: " + getX() + ", Y: " + getY() + ", Rotation: " + getRotation();
		result += "}";
		return result;
	}
}
