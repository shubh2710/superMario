package controllers;

import javax.swing.text.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mysupermario.shubh.MainSuperMario;

public class Controller {

	Viewport gamePort;
	Stage stage;
	boolean uppressed,downpressed,leftpressed,rightpressed,pluspressed,minuspressed,crosspressed,checkpressed;
	OrthographicCamera cam;
	private MainSuperMario game;
	private Object sb;
	public Controller(SpriteBatch batch) {
		cam=new OrthographicCamera();
		gamePort=new FitViewport(MainSuperMario.V_WIDTH,MainSuperMario.V_HIGTH,cam);
		stage=new Stage(gamePort,batch);
		Gdx.input.setInputProcessor(stage);
		setupnevigation();
		setupControl();
	}
	public boolean isUppressed() {
		return uppressed; 
	}
	public boolean isDownpressed() {
		return downpressed;
	}
	public boolean isLeftpressed() {
		return leftpressed;
	}
	public boolean isRightpressed() {
		return rightpressed;
	}
	public void resize(int Widht,int Height){
		gamePort.update(Widht, Height);
	}
	public void dispose() {
		stage.dispose();	
	}
	public void draw(){
		stage.draw();
	}
	public void setupnevigation(){
		Table table=new Table();
		table.left().bottom();
		table.setFillParent(true);
		Image up=new Image(new Texture("up.png"));
		up.setSize(30, 30);
		up.addListener(new InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				uppressed=true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				uppressed=false;
			}
			
		});
		Image right=new Image(new Texture("right.png"));
		right.setSize(30, 30);
		right.addListener(new InputListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				rightpressed=true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
							rightpressed=false;
			}
			
		});
		Image down=new Image(new Texture("down.png"));
		down.setSize(30, 30);
		down.addListener(new InputListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				downpressed=true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				downpressed=false;
			}
			
		});
		Image left=new Image(new Texture("left.png"));
		left.setSize(30, 30);
		left.addListener(new InputListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				leftpressed=true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				leftpressed=false;
			}
			
		});
		table.add();
		table.add(up).size(up.getWidth(),up.getHeight());
		table.row().pad(5,5,5,5);
		table.add(left).size(left.getWidth(),left.getHeight());
		table.add();
		table.add(right).size(right.getWidth(),right.getHeight());
		table.add();
		table.row().padBottom(5);
		table.add();
		table.add(down).size(down.getWidth(),down.getHeight());
		table.add();
		stage.addActor(table);
	}
	public void setupControl(){
		Table table=new Table();
		table.right().bottom();
		table.setFillParent(true);
		Image plus=new Image(new Texture("plus.png"));
		plus.setSize(30, 30);
		plus.addListener(new InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				pluspressed=true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				pluspressed=false;
			}
			
		});
		Image minus=new Image(new Texture("minus.png"));
		minus.setSize(30, 30);
		minus.addListener(new InputListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				minuspressed=true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				minuspressed=false;
			}
			
		});
		Image check=new Image(new Texture("check.png"));
		check.setSize(30, 30);
		check.addListener(new InputListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				checkpressed=true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				checkpressed=false;
			}
			
		});
		Image cross=new Image(new Texture("cross.png"));
		cross.setSize(30, 30);
		cross.addListener(new InputListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				crosspressed=true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				crosspressed=false;
			}
			
		});
		table.add();
		table.add(minus).size(minus.getWidth(),minus.getHeight());
		table.row();
		table.add(plus).size(plus.getWidth(),plus.getHeight());
		table.add();
		table.add(cross).size(cross.getWidth(),cross.getHeight());
		table.add();
		table.row();
		table.add();
		table.add(check).size(check.getWidth(),check.getHeight());
		table.add();
		stage.addActor(table);

	}
	public boolean isPluspressed() {
		return pluspressed;
	}
	public boolean isMinuspressed() {
		return minuspressed;
	}
	public boolean isCrosspressed() {
		return crosspressed;
	}
	public boolean isCheckpressed() {
		return checkpressed;
	}
	
}
