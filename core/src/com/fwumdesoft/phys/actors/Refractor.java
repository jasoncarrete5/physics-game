package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fwumdesoft.phys.Main;

public class Refractor extends HitboxActor {
	private static final TextureRegion texture;
	
	static {
		texture = new TextureRegion(Main.asset.get(Main.Assets.REFRACTOR));
	}
	
	private float refractionIndex;
	
	public Refractor() {
		
	}
}
