package com.fwumdesoft.phys;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.fwumdesoft.phys.actors.HitboxActor;

/**
 * Contains all tools that the user is able to add to the game.
 */
public class Toolbox {
	private ObjectMap<HitboxActor, Boolean> tools;
	
	public Toolbox(Array<HitboxActor> movables) {
		tools = new ObjectMap<>();
		movables.forEach(actor -> tools.put(actor, true));
	}
	
	public void putBack(HitboxActor actor) {
		tools.put(actor, true);
	}
	
	public ObjectMap<HitboxActor, Boolean> getTools() {
		return tools;
	}
	
	public List<HitboxActor> getAsList() {
		List<HitboxActor> lstTools = new List<>(Main.uiskin);
		Array<HitboxActor> listItems = tools.keys().toArray();
		lstTools.setItems(listItems);
		return lstTools;
	}
	
	@Override
	public String toString() {
		return "Toolbox contents are " + tools;
	}
}
