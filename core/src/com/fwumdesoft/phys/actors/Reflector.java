package com.fwumdesoft.phys.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.fwumdesoft.phys.Main;

public class Reflector extends Actor {
	private static final TextureRegion texture;
	
	static {
		texture = new TextureRegion(Main.asset.get(Main.Assets.REFLECTOR));
	}
	
	
}
