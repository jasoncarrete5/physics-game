package com.fwumdesoft.phys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.fwumdesoft.phys.actors.AirMolecule;
import com.fwumdesoft.phys.actors.Transmitter;
import com.fwumdesoft.phys.actors.Wave;

public class GameScreen extends ScreenAdapter {
	private int curLevel = 0;
	private Stage stage;
	private Level level;
	private float airDensity = 0.03f;
	private Toolbox tools;
	private Window wndObserver, wndToolbox;
	
	/** {@code true} if transmitters have already been fired on this level.
	 * Gets reset by a call to resetLevel(). */
	private boolean transmitted;
	
	@Override
	public void show() {
		stage = new Stage(new ExtendViewport(1000f, 1000f * ((float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth())));
		Gdx.input.setInputProcessor(stage);
		
		loadNextLevel();
	}
	
	private void loadNextLevel() {
		curLevel++;
		resetLevel();
	}
	
	private void resetLevel() {
		//reset instance vars
		stage.clear();
		stage.addListener(new InputManager());
		transmitted = false;
		level = Level.loadFromFile(Integer.toString(curLevel));
		
		//TODO allow the user to select and rotate actors that are rotatable but not movable
		//setup level with static objects and air
		generateAir(airDensity);
		level.getAllActors().forEach(actor -> { //add appropriate listeners to actors
			//adding generic listeners
			actor.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					if(button == Buttons.LEFT) {
						stage.setKeyboardFocus(event.getListenerActor());
						wndObserver.clearChildren();
						wndObserver.add(new Label(event.getListenerActor().toString(), Main.uiskin)).center();
						wndObserver.pack();
						event.stop();
						return true;
					}
					return false;
				}
			});
			actor.addListener(new FocusListener() {
				@Override
				public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
					if(!focused) {
						event.getListenerActor().setDebug(false);
					} else {
						event.getListenerActor().setDebug(true);
					}
				}
			});
			if(actor.isMovable()) { //add specific listeners if the actor is movable
				actor.addListener(new DragListener() {
					@Override
					public void drag(InputEvent event, float x, float y, int pointer) {
						event.getListenerActor().addAction(Actions.moveTo(event.getStageX(), event.getStageY(), 0.1f, Interpolation.linear));
					}
				});
			} else {
				stage.addActor(actor); //only add actors to the stage if they aren't movable
			}
			if(actor.isRotatable()) { //add specific listeners if the actor is rotatable
				actor.addListener(new InputListener() {
					@Override
					public boolean keyDown(InputEvent event, int keycode) {
						Actor actor = event.getListenerActor();
						if(keycode == Keys.LEFT) {
							actor.addAction(Actions.forever(Actions.rotateBy(1f)));
							return true;
						} else if(keycode == Keys.RIGHT) {
							actor.addAction(Actions.forever(Actions.rotateBy(-1f)));
							return true;
						}
						return false;
					}

					@Override
					public boolean keyUp(InputEvent event, int keycode) {
						if(actor.hasActions()) {
							actor.clearActions();
							return true;
						}
						return false;
					}
				});
			}
		});
		tools = new Toolbox(level.getNotFixedPositionActors());
		Gdx.app.debug("GameScreen", tools.toString());
		
		//reset windows
		wndObserver = new Window("Observer", Main.uiskin);
		wndObserver.setVisible(false);
		stage.addActor(wndObserver);
		
		wndToolbox = new Window("Toolbox", Main.uiskin);
		wndToolbox.setVisible(false);
		wndToolbox.add(tools.getAsList()).minWidth(200).minHeight(200).pad(20f);
		wndToolbox.pack();
		stage.addActor(wndToolbox);
	}
	
	/**
	 * Generates AirMolecule objects on the screen with a specified density and adds them
	 * to the stage.
	 * @param density Density of AirMolecules in molecules/unit^2
	 */
	private void generateAir(float density) {
		float worldWidth = stage.getWidth(), worldHeight = stage.getHeight();
		float area = worldWidth * worldHeight;
		int totalAir = MathUtils.ceil(area * density);
		int cellLength = MathUtils.ceil((float)Math.sqrt(1f / density));
		
		for(int minY = 0; minY < worldHeight; minY += cellLength) {
			for(int minX = 0; minX < worldWidth; minX += cellLength) {
				float randX = MathUtils.random(minX, minX + cellLength);
				float randY = MathUtils.random(minY, minY + cellLength);
				AirMolecule airMolecule = Pools.get(AirMolecule.class, totalAir).obtain();
				airMolecule.setPosition(randX, randY, Align.center);
				airMolecule.markPosition(new Vector2(airMolecule.getX(), airMolecule.getY())); //marks original position
				airMolecule.toBack();
				stage.addActor(airMolecule);
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		//regenerate air (maybe)
//		Array<Actor> remove = new Array<>();
//		stage.getActors().forEach(actor -> {
//			if(actor instanceof AirMolecule) {
//				remove.add(actor);
//			}
//		});
//		remove.forEach(actor -> actor.remove());
//		generateAir(airDensity);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
		
//		***** check victory conditions *****
		int numWaves = level.getTransmitters().size;
		boolean reset = false;
		for(Actor actor : stage.getActors()) {
			if(actor instanceof Wave) {
				Wave wave = (Wave)actor;
				if(wave.wasSuccessful()) {
					numWaves--;
				} else if(!wave.isAlive()) {
					reset = true;
					break;
				}
			}
		}
		if(reset) {
			resetLevel();
			Gdx.app.log("GameScreen", "Reset current level");
		} else if(numWaves == 0) {
			loadNextLevel();
			Gdx.app.log("GameScreen", "Loaded next level");
		}
	}
	
	@Override
	public void hide() {
		dispose();
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
	
	private class InputManager extends InputListener {
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			if(stage.getKeyboardFocus() != null) {
				stage.setKeyboardFocus(null);
				wndObserver.clearChildren();
				return true;
			}
			return false;
		}
		
		@Override
		public boolean keyDown(InputEvent event, int keycode) {
			switch(keycode)
			{
			case Keys.SPACE: //tells all transmitters to transmit a wave
				if(transmitted) break; //only allow transmitters to be transmitted once per level
				for(int i = 0; i < stage.getActors().size; i++) {
					Actor actor = stage.getActors().get(i);
					if(actor instanceof Transmitter) {
						Transmitter trans = (Transmitter)actor;
						trans.transmit();
					}
				}
				transmitted = true;
				Gdx.app.log("GameScreen.InputManager", "Transmitters fired");
				return true;
			case Keys.T: //toggle the toolbox window
				wndToolbox.setVisible(!wndToolbox.isVisible());
				return true;
			case Keys.O: //toggle the observer window
				wndObserver.setVisible(!wndObserver.isVisible());
				return true;
			}
			return false;
		}
	}
}
