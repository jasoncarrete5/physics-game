package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.fwumdesoft.phys.Main;

/**
 * This Actor receives Waves.
 */
public class Receiver extends HitboxActor {
	private static final TextureRegion texture;
	
	static {
		texture = new TextureRegion(Main.asset.get(Main.Assets.RECEIVER));
	}
	
	public Receiver() {
		setSize(20, 40);
		setOrigin(Align.center);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Receiver: {");
		sb.append("\nPosition: (" + MathUtils.round(getX()) + ", " + MathUtils.round(getY()) + ")");
		sb.append("\nRotation: " + MathUtils.round(getRotation() % 360));
		sb.append("\n}");
		return sb.toString();
	}
}
