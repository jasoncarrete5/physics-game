package com.fwumdesoft.phys;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Main extends Game {
	/** Contains AssetDescriptor constants for easy access of assets. */
	public static class Assets {
		public static final AssetDescriptor<Texture> AIR = new AssetDescriptor<>("textures/air.png", Texture.class);
		public static final AssetDescriptor<Texture> REFLECTOR = new AssetDescriptor<>("textures/reflector.png", Texture.class);
		public static final AssetDescriptor<Texture> REFRACTOR = new AssetDescriptor<>("textures/refractor.png", Texture.class);
		public static final AssetDescriptor<Texture> WALL = new AssetDescriptor<>("textures/wall.png", Texture.class);
		public static final AssetDescriptor<Texture> TRANSMITTER = new AssetDescriptor<>("textures/transmitter.png", Texture.class);
	}
	
	public static AssetManager asset;
	public static Skin uiskin;
	public static Game game;
	
	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		game = this;
		uiskin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		asset = new AssetManager();
		asset.load(Assets.AIR);
		asset.load(Assets.REFLECTOR);
		asset.load(Assets.REFRACTOR);
		asset.load(Assets.WALL);
		asset.load(Assets.TRANSMITTER);
		asset.finishLoading();
		
		setScreen(new MainMenuScreen());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		uiskin.dispose();
		asset.dispose();
	}
}
