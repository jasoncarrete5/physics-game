package com.fwumdesoft.phys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
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
		stage = new Stage(new FillViewport(400f, 400f * ((float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth())));
		Gdx.input.setInputProcessor(stage);
		Table ui = new Table();
		ui.setFillParent(true);
		stage.addActor(ui);
		level = new Level();
		
		//set up current actor window
		Window wndActorSettings = new Window("Actor Settings", Main.uiskin);
		CheckBox chkFixedPosition = new CheckBox("Fixed Position", Main.uiskin);
		CheckBox chkFixedRotation = new CheckBox("Fixed Rotation", Main.uiskin);
		wndActorSettings.add(chkFixedPosition);
		wndActorSettings.row().padTop(2);
		wndActorSettings.add(chkFixedRotation);
		wndActorSettings.row().padTop(5);
		
		DragListener moveWhenDragged = new DragListener() {
			private Vector2 tmp = new Vector2();
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				wndActorSettings.setUserObject(event.getListenerActor());
				return super.touchDown(event, x, y, pointer, button);
			}
			
			@Override
			public void drag(InputEvent event, float x, float y, int pointer) {
				tmp = stage.screenToStageCoordinates(tmp.set(x, y));
				event.getListenerActor().setPosition(tmp.x, tmp.y);
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
				level.add(refl);
			}
		});
		wndActors.add(btnReflector);
		wndActors.pack();
		stage.addActor(wndActors);
	}
	
	private void save() {
		
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
	}
	
	@Override
	public void hide() {
		//add all actors in the stage to the level and save them
		for(Actor a : stage.getActors()) {
			level.add(a);
		}
		level.writeLevel("lvl.json");
		Gdx.app.log("EditorScreen", "Saved level");
		
		dispose();
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
}
