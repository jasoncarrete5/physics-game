package com.fwumdesoft.phys;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool.Poolable;

public class AirMolecule extends Actor implements Poolable {
	private static final TextureRegion texture;
	
	static {
		texture = new TextureRegion(Main.assets.get("textures/air.png", Texture.class));
	}
	
	public AirMolecule(float x, float y) {
		setPosition(x, y);
		setOrigin(Align.center);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	@Override
	public void reset() {
		//Nothing to reset
	}
}
