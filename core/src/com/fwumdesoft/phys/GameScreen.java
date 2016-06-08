package com.fwumdesoft.phys;

import javax.swing.JOptionPane;
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
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.fwumdesoft.phys.Main.Assets;
import com.fwumdesoft.phys.actors.AirMolecule;
import com.fwumdesoft.phys.actors.HitboxActor;
import com.fwumdesoft.phys.actors.Transmitter;
import com.fwumdesoft.phys.actors.Wave;

public class GameScreen extends ScreenAdapter {
	private int curLevel = 0;
	private Stage stage;
	private Level level;
	private float airDensity = 0.03f;
	private Toolbox tools;
	private Window wndObserver, wndToolbox;
	private int maxLevels;
	
	/** {@code true} if transmitters have already been fired on this level.
	 * Gets reset by a call to resetLevel(). */
	private boolean transmitted;
	
	@Override
	public void show() {
		maxLevels = Gdx.files.local("levels/").list().length;
		curLevel = Integer.parseInt(Gdx.files.local("currentlevel").readString()) - 1;
		
		stage = new Stage(new ExtendViewport(1000f, 1000f * ((float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth())));
		Gdx.input.setInputProcessor(stage);
		
		loadNextLevel();	
	}
	
	private void loadNextLevel() {
		curLevel++;
		Gdx.files.local("currentlevel").writeString(Integer.toString(curLevel), false);
		if(curLevel > maxLevels) {
			JOptionPane.showMessageDialog(null, "You Won!");
			Gdx.files.local("currentlevel").writeString("1", false);
			Gdx.app.exit();
		} else {
			resetLevel();
		}
	}
	
	private void resetLevel() {
		//reset instance vars
		stage.clear();
		stage.addListener(new InputManager());
		transmitted = false;
		level = Level.loadFromFile(Integer.toString(curLevel));
		if(!level.getHint().equals(""))
			JOptionPane.showMessageDialog(null, level.getHint());
		
		//setup level with static objects and air
		generateAir(airDensity);
		level.getAllActors().forEach(actor -> { //add appropriate listeners to actors
			//adding generic listeners
			actor.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					if(button == Buttons.LEFT) {
						stage.setKeyboardFocus(event.getListenerActor());
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
						HitboxActor hActor = (HitboxActor)actor;
						wndObserver.clearChildren();
						wndObserver.add(new Label(hActor.observerString(), Main.uiskin)).center();
						wndObserver.pack();
					}
				}
			});
			if(actor.isMovable()) { //add specific listeners if the actor is movable
				actor.addListener(new DragListener() {
					@Override
					public void drag(InputEvent event, float x, float y, int pointer) {
						event.getListenerActor().addAction(Actions.moveToAligned(event.getStageX(), event.getStageY(), Align.center, 0.1f, Interpolation.linear));
					}
				});
				actor.addListener(new InputListener() {
					@Override
					public boolean keyDown(InputEvent event, int keycode) {
						if(keycode == Keys.DEL || keycode == Keys.FORWARD_DEL) {
							tools.putBack(actor);
							actor.remove();
							return true;
						}
						return false;
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
						if(keycode == Keys.LEFT || keycode == Keys.A) {
							actor.addAction(Actions.forever(Actions.rotateBy(1f)));
							return true;
						} else if(keycode == Keys.RIGHT || keycode == Keys.D) {
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
		List<HitboxActor> toolList = tools.getAsList();
		TextButton btnCreate = new TextButton("Create", Main.uiskin);
		TextButton btnRemove = new TextButton("Remove", Main.uiskin);
		btnRemove.setVisible(false);
		btnCreate.addListener(new ClickListener(Buttons.LEFT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(toolList.getSelected() == null) return;
				tools.takeTool(toolList.getSelected());
				stage.addActor(toolList.getSelected());
				btnRemove.setVisible(true);
				btnCreate.setVisible(false);
			}
		});
		btnRemove.addListener(new ClickListener(Buttons.LEFT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(toolList.getSelected() == null) return;
				tools.putBack(toolList.getSelected());
				toolList.getSelected().remove();
				btnCreate.setVisible(true);
				btnRemove.setVisible(false);
			}
		});
		toolList.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				boolean isInToolbox = tools.getTools().get(toolList.getSelected());
				if(isInToolbox) {
					btnCreate.setVisible(true);
					btnRemove.setVisible(false);
				} else {
					btnCreate.setVisible(false);
					btnRemove.setVisible(true);
				}
			}
		});
		wndToolbox.add(toolList).minWidth(200).minHeight(200).pad(10f);
		VerticalGroup grpButtons = new VerticalGroup();
		grpButtons.addActor(btnCreate);
		grpButtons.addActor(btnRemove);
		wndToolbox.add(grpButtons);
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
			Main.asset.get(Assets.FAIL_LEVEL_SOUND).play();
			JOptionPane.showMessageDialog(null, "Reset Level");
			resetLevel();
			Gdx.app.log("GameScreen", "Reset current level");
		} else if(numWaves == 0) {
			Main.asset.get(Assets.BEAT_LEVEL_SOUND).play();
			JOptionPane.showMessageDialog(null, "Advance to next level");
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
			case Keys.R:
				Main.asset.get(Assets.FAIL_LEVEL_SOUND).play();
				stage.addAction(Actions.run(() -> resetLevel()));
				return true;
			}
			return false;
		}
	}
}
