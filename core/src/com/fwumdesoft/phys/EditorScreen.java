package com.fwumdesoft.phys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.fwumdesoft.phys.actors.Reflector;

/**
 * The level editor screen.
 */
public class EditorScreen extends ScreenAdapter {
	private Stage stage;
	private Level level;
	
	@Override
	public void show() {
		stage = new Stage(new FillViewport(1000f, 1000f * ((float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth())));
		Gdx.input.setInputProcessor(stage);
		
		//ask the user to load a level or create a new level
		Dialog newLevel = new Dialog("Level Editor", Main.uiskin) {
			@Override
			protected void result(Object object) {
//				TextField txtFileName = (TextField)object;
//				FileHandle lvlFile = Gdx.files.external("physics-game/levels/" + txtFileName.getText());
//				if(lvlFile.exists()) {
//					level = Level.loadFromFile(txtFileName.getText());
//				} else {
//					level = new Level();
//					level.setName(txtFileName.getText());
//				}
			}
		};
		TextField txtFileName = new TextField("", Main.uiskin);
		txtFileName.setMessageText("filename");
		newLevel.button("Open Level", new Runnable() {
			public void run() {
				FileHandle lvlFile = Gdx.files.local("levels/" + txtFileName.getText());
				if(!lvlFile.exists()) {
					new Dialog("Error", Main.uiskin).text("Failed to load file").button("Ok").show(stage);
				} else {
					level = Level.loadFromFile(lvlFile.name());
				}
			}
		});
		newLevel.button("New Level", new Runnable() {
			public void run() {
				FileHandle lvlFile = Gdx.files.local("levels/" + txtFileName.getText());
				lvlFile.delete();
				level = new Level();
				level.setName(lvlFile.name());
			}
		});
		newLevel.add(txtFileName);
		newLevel.show(stage);
		
		//set upactor settings window
		Window wndActorSettings = new Window("Actor Settings", Main.uiskin);
		CheckBox chkFixedPosition = new CheckBox("Fixed Position", Main.uiskin);
		chkFixedPosition.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CheckBox chkBox = (CheckBox)event.getListenerActor();
				Actor a = (Actor)event.getListenerActor().getParent().getUserObject();
				if(a instanceof TransformType) {
					TransformType tActor = (TransformType)a;
					if(chkBox.isChecked()) {
						tActor.setFixed((byte)(tActor.getFixed() | TransformType.positionFixed));
					} else {
						tActor.setFixed((byte)(tActor.getFixed() & ~TransformType.positionFixed));
					}
				}
			}
		});
		CheckBox chkFixedRotation = new CheckBox("Fixed Rotation", Main.uiskin);
		chkFixedRotation.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				CheckBox chkBox = (CheckBox)event.getListenerActor();
				Actor a = (Actor)event.getListenerActor().getParent().getUserObject();
				if(a instanceof TransformType) {
					TransformType tActor = (TransformType)a;
					if(chkBox.isChecked()) {
						tActor.setFixed((byte)(tActor.getFixed() | TransformType.rotationFixed));
					} else {
						tActor.setFixed((byte)(tActor.getFixed() & ~TransformType.rotationFixed));
					}
				}
			}
		});
		wndActorSettings.add(chkFixedPosition);
		wndActorSettings.row().padTop(2);
		wndActorSettings.add(chkFixedRotation);
		wndActorSettings.row().padTop(5);
		wndActorSettings.pack();
		wndActorSettings.setPosition(0, stage.getHeight(), Align.topLeft);
		
		DragListener moveWhenDragged = new DragListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				wndActorSettings.setUserObject(event.getListenerActor());
				return super.touchDown(event, x, y, pointer, button);
			}
			
			@Override
			public void drag(InputEvent event, float x, float y, int pointer) {
				Action moveAction = Actions.moveToAligned(event.getStageX(), event.getStageY(), Align.center, 0.01f, Interpolation.linear);
				event.getListenerActor().addAction(moveAction);
			}
		};
		
		//set up actor window
		Window wndActors = new Window("Actors", Main.uiskin);
		TextButton btnReflector = new TextButton("Reflector", Main.uiskin);
		btnReflector.addListener(new ClickListener(Buttons.LEFT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Reflector refl = new Reflector();
				refl.addListener(moveWhenDragged);
				refl.setPosition(stage.getWidth() / 2, stage.getHeight() / 2, Align.center);
				stage.addActor(refl);
//				level.add(refl);
			}
		});
		wndActors.add(btnReflector);
		wndActors.pack();
		
		stage.addActor(wndActors);
		stage.addActor(wndActorSettings);
	}
	
	private void save() {
		FileHandle lvlFile = Gdx.files.external("physics-game/levels/" + level.getName());
		lvlFile.delete();
		level.writeLevel();
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
	}
	
	@Override
	public void hide() {
		save();
		dispose();
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
}
