package screens;


import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mysupermario.shubh.MainSuperMario;

import controllers.Controller;
import scenes.Hud;
import sprites.Emney;
import sprites.Goomba;
import sprites.Itemdef;
import sprites.Items;
import sprites.Mario;
import sprites.Mushroom;
import tools.B2WolrdCreator;
import tools.WorldContactListener;

public class PlayScreen implements Screen{
	private MainSuperMario game;
	private Hud hud;
	private TmxMapLoader maploader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer render;
	private OrthographicCamera gamecam;
	private Viewport gamePort;
	private World world;
	private Box2DDebugRenderer b2dr;
	private Mario player;
	private  TextureAtlas atlas;
	private Music music;
	private  boolean ispressed;
	private  boolean ispressedB;
	private Sound jump;
	public Array<Items> items;
	public Array<Body> bodyToRemove;
	public Array<Emney> enemeys;
	public LinkedBlockingQueue<Itemdef> itemToSpawn;
	public CopyOnWriteArrayList<Mario> entities;
	Controller controller;
	private B2WolrdCreator creator;
	public LinkedBlockingQueue<Body> deadBodies;
	public PlayScreen(MainSuperMario game){
		atlas=new TextureAtlas("supermariopack.atlas");
		this.game=game;
		controller=new Controller(game.batch);
		gamecam=new OrthographicCamera();
		gamePort=new FitViewport(MainSuperMario.V_WIDTH/MainSuperMario.PPM,MainSuperMario.V_HIGTH/MainSuperMario.PPM,gamecam);
		hud=new Hud(game.batch);
		//b2dr=new Box2DDebugRenderer();
		maploader=new TmxMapLoader();
		switch(MainSuperMario.LEVEL){
		case 1:
			map=maploader.load("marioworld.tmx");
			break;
		case 2:
			map=maploader.load("marioworld2.tmx");
			break;
		case 3:
			map=maploader.load("marioworld3.tmx");
			break;
		case 4:
			map=maploader.load("marioworld4.tmx");
			break;
		default:
				map=maploader.load("marioworld.tmx");
				MainSuperMario.LEVEL=1;
		}
		render=new OrthogonalTiledMapRenderer(map,1/MainSuperMario.PPM);
		gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2,0);
		world=new World(new Vector2(0,-10),true);
		creator=new B2WolrdCreator(this);
		player=new Mario(this);
		world.setContactListener(new WorldContactListener());
		//music=MainSuperMario.manager.get("mario.mp3",Music.class);
		items=new Array<Items>();
		entities = new CopyOnWriteArrayList<Mario>();
		itemToSpawn =new LinkedBlockingQueue<Itemdef>();
		deadBodies=new LinkedBlockingQueue<Body>();
		ispressed=false;
		ispressedB=false;
		bodyToRemove=new Array<Body>();
		enemeys=new Array<Emney>();
		enemeys=creator.getEnemies();
	}
	public void spawnItem(Itemdef idef){
		itemToSpawn.add(idef);	
	}
	public boolean gameOver(){
		if(player.currentState==Mario.State.DEAD && player.getStateTimer()>3){
			return true;
		}else return false;
	}
	public boolean isComplete(){
		if(player.currentState==Mario.State.WINNING && player.getStateTimer()>3){
			return true;
		}else return false;
	}
	public void handleSpawninItes(){
		if(!itemToSpawn.isEmpty()){
			Itemdef idef=itemToSpawn.poll();
			try{
			if(idef.type== Mushroom.class){
				items.add(new Mushroom(this,idef.position.x,idef.position.y));
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public TextureAtlas getAtlas(){
		return atlas;
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	public void update(float dt){
			hendleInput(dt);
			hendleInputforKeypad(dt);
			removeDeadBodies();
			handleSpawninItes();
			world.step(1/60f,6 ,2 );
			
			if(!world.isLocked()){
	            }
	        	Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run () {
						for(Body body:bodyToRemove){
							if(body!=null){
								body.setUserData(null);
								world.destroyBody(body);
								bodyToRemove.removeValue(body, true);
								}
						}
					}
				});
			player.update(dt);
			for(Emney enmey:enemeys){
				enmey.update(dt);
				if(enmey.getX()< player.getX()+300/MainSuperMario.PPM){
					try{
					enmey.b2body.setActive(true);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			
			for(Items item:items){
				item.update(dt);
			}
			hud.update(dt);
			if(player.currentState!=Mario.State.DEAD)
			gamecam.position.x=player.b2body.getPosition().x;
			gamecam.update();
			render.setView(gamecam);
			
	}
	private void hendleInput(float dt) {
		if(player.currentState!=Mario.State.DEAD ){
			if(controller.isCrosspressed() && !ispressed  && player.currentState!=Mario.State.JUMPING){
				player.b2body.applyLinearImpulse(new Vector2(0,4f), player.b2body.getWorldCenter(), true);
				//MainSuperMario.manager.get("jump.wav",Sound.class).play();
				MainSuperMario.jump.play();
				ispressed=true;
				}else if(!controller.isCrosspressed()){
					ispressed=false;
				}
		if(controller.isRightpressed() && player.b2body.getLinearVelocity().x<=2){
			player.b2body.applyLinearImpulse(new Vector2 (0.1f,0), player.b2body.getWorldCenter(),true);
			}
		if(controller.isLeftpressed() && player.b2body.getLinearVelocity().x>=-2){
			player.b2body.applyLinearImpulse(new Vector2 (-0.1f,0), player.b2body.getWorldCenter(),true);
			}
		if(controller.isPluspressed() && !ispressedB){
			ispressedB=true;
			player.fire();
			}else if(!controller.isPluspressed()){
				ispressedB=false;
				}
		}
}
	
	
	private void hendleInputforKeypad(float dt) {
		if(player.currentState!=Mario.State.DEAD ){
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.currentState!=Mario.State.JUMPING){
				player.b2body.applyLinearImpulse(new Vector2(0,4.1f), player.b2body.getWorldCenter(), true);
				//MainSuperMario.manager.get("jump.wav",Sound.class).play();
				MainSuperMario.jump.play();
				ispressed=true;
			}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x<=2){
			player.b2body.applyLinearImpulse(new Vector2 (0.1f,0), player.b2body.getWorldCenter(),true);
			}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x>=-2){
			player.b2body.applyLinearImpulse(new Vector2 (-0.1f,0), player.b2body.getWorldCenter(),true);
			}
		 if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
             player.fire();
		} 
	}
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		update(delta);
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		render.render();
		//b2dr.render(world, gamecam.combined);
		game.batch.setProjectionMatrix(gamecam.combined);
		game.batch.begin();
		player.draw(game.batch);
		for(Emney enmey:enemeys)
			enmey.draw(game.batch);
		for(Items item:items){
			item.draw(game.batch);
			}
		game.batch.end();
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
		if(gameOver()){
			game.setScreen(new GameOverScreen(game));
			dispose();
		}
		if(isComplete()){
			game.setScreen(new Winning(game));
			dispose();
		}
			
		controller.draw();
	}
	
	public TiledMap getmap(){
		return map;
	}
	public World  getworld(){
		return world;
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		gamePort.update(width, height);
		controller.resize(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		map.dispose();
		render.dispose();
		world.dispose();
		//b2dr.dispose();
		hud.dispose();
	}
	   private void removeDeadBodies(){
		      for(int i = 0; i<deadBodies.size();i++){
		         Body body = deadBodies.poll();
		         if(!world.isLocked() && body != null){
		        	 body.setUserData(null);
		            removeBodySafely(body);
		            deadBodies.remove(body);
		         }
		      }
		   }

		   private void removeBodySafely(Body body) {
		      final Array<JointEdge> list = body.getJointList();
		      while (list.size > 0) {
		         world.destroyJoint(list.get(0).joint);
		      }
		      world.destroyBody(body);
		      
		   }
}
