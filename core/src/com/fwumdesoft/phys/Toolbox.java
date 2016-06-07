package com.fwumdesoft.phys;

import com.badlogic.gdx.utils.Array;
import com.fwumdesoft.phys.actors.HitboxActor;

/**
 * Contains all tools that the user is able to add to the game.
 */
public class Toolbox {
	private Array<HitboxActor> tools;
	
	public Toolbox(Array<HitboxActor> movables) {
		tools = movables;
	}
	
	@Override
	public String toString() {
		return "Toolbox: " + tools;
	}
}
