package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.fwumdesoft.phys.Main;

/**
 * This Actor can fire Waves.
 */
public class Transmitter extends HitboxActor {
	private static final TextureRegion texture;
	
	static {
		texture = new TextureRegion(Main.asset.get(Main.Assets.TRANSMITTER));
	}
	
	public Transmitter() {
		setSize(40, 40);
		setOrigin(Align.center);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	public void transmit() {
		Wave wave = new Wave();
		getStage().addActor(wave);
		Vector2 leading = Vector2.X.cpy().rotate(getRotation()).scl(5);
		wave.propagate(getX(Align.center) + leading.x, getY(Align.center) + leading.y, getRotation());
		wave.debug();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Transmitter: {");
		sb.append(" Position: (" + MathUtils.round(getX()) + ", " + MathUtils.round(getY()) + ")");
		sb.append(" Rotation: " + MathUtils.round(getRotation() % 360));
		sb.append(" }");
		return sb.toString();
	}
}
