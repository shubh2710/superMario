package tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mysupermario.shubh.MainSuperMario;

import screens.PlayScreen;
import sprites.Brick;
import sprites.Coins;
import sprites.Emney;
import sprites.Goomba;
import sprites.Tortol;

public class B2WolrdCreator {

	private Array<Goomba> goombas;
	private Array<Tortol> turtles;
	public B2WolrdCreator(PlayScreen screen){
		
		World world=screen.getworld();
		TiledMap map=screen.getmap();
		BodyDef bdef=new BodyDef();
		PolygonShape shape=new PolygonShape();
		FixtureDef fdef=new FixtureDef();
		Body body;
		// coin
		for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
			new Coins(screen,object);
		}
		//brick
		for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
			new Brick(screen,object);
		}
		//pipe
		for(MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
			Rectangle rect=((RectangleMapObject)object).getRectangle();
			bdef.type=BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX()+rect.getWidth()/2)/MainSuperMario.PPM,(rect.getY()+rect.getHeight()/2)/MainSuperMario.PPM);
			body=world.createBody(bdef);
			shape.setAsBox(rect.getWidth()/2/MainSuperMario.PPM, rect.getHeight()/2/MainSuperMario.PPM);
			fdef.shape=shape;
			fdef.filter.categoryBits=MainSuperMario.OBJECT_BIT;
			body.createFixture(fdef);
		}
		//winning
		for(MapObject object: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
			Rectangle rect=((RectangleMapObject)object).getRectangle();
			bdef.type=BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX()+rect.getWidth()/2)/MainSuperMario.PPM,(rect.getY()+rect.getHeight()/2)/MainSuperMario.PPM);
			body=world.createBody(bdef);
			shape.setAsBox(rect.getWidth()/2/MainSuperMario.PPM, rect.getHeight()/2/MainSuperMario.PPM);
			fdef.shape=shape;
			fdef.filter.categoryBits=MainSuperMario.WINNING_BIT;
			body.createFixture(fdef);
		}
		for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
			Rectangle rect=((RectangleMapObject)object).getRectangle();
			bdef.type=BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX()+rect.getWidth()/2)/MainSuperMario.PPM,(rect.getY()+rect.getHeight()/2)/MainSuperMario.PPM);
			body=world.createBody(bdef);
			shape.setAsBox(rect.getWidth()/2/MainSuperMario.PPM, rect.getHeight()/2/MainSuperMario.PPM);
			fdef.shape=shape;
			fdef.filter.categoryBits=MainSuperMario.GROUND_BIT;
			body.createFixture(fdef);
		}
		goombas =new Array<Goomba>(); 
		for(MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
			Rectangle rect=((RectangleMapObject)object).getRectangle();
			goombas.add(new Goomba(screen,rect.getX()/MainSuperMario.PPM,rect.getY()/MainSuperMario.PPM));
			
		}
		turtles =new Array<Tortol>(); 
		for(MapObject object: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
			Rectangle rect=((RectangleMapObject)object).getRectangle();
			turtles.add(new Tortol(screen,rect.getX()/MainSuperMario.PPM,rect.getY()/MainSuperMario.PPM));
			
		}
	}
	public Array<Goomba> getGoombas() {
		return goombas;
	}
	public Array<Tortol> getTurtles() {
		return turtles;
	}
	public Array<Emney> getEnemies() {
		Array<Emney> enemies=new Array<Emney>();
		enemies.addAll(goombas);
		enemies.addAll(turtles);
		return enemies;
	}

}
