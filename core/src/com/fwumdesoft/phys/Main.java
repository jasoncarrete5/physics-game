package com.fwumdesoft.phys;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Main extends Game {
	public static AssetManager assets;
	public static Skin uiskin;
	public static Game game;
	
	@Override
	public void create() {
		game = this;
		uiskin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		assets = new AssetManager();
		
		setScreen(new GameScreen());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		uiskin.dispose();
		assets.dispose();
	}
}
