package sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mysupermario.shubh.MainSuperMario;

import screens.PlayScreen;

public abstract class InterActiveTiledObject {
	protected Fixture fixture;
	protected World world;
	protected TiledMap map;
	protected TiledMapTile tile;
	protected Rectangle bounds;
	protected Body body;
	protected PlayScreen screen;
	protected MapObject object;
	
	
	
	public InterActiveTiledObject(PlayScreen screen,MapObject object){
	this.object=object;
	this.screen=screen;
	this.world=screen.getworld();
	this.map=screen.getmap();
	this.bounds=((RectangleMapObject)object).getRectangle();
	BodyDef bdef=new BodyDef();
	FixtureDef fdef=new FixtureDef();
	PolygonShape shape=new PolygonShape();
	bdef.type=BodyDef.BodyType.StaticBody;
	bdef.position.set((bounds.getX()+bounds.getWidth()/2)/MainSuperMario.PPM,(bounds.getY()+bounds.getHeight()/2)/MainSuperMario.PPM);
	body=world.createBody(bdef);
	shape.setAsBox(bounds.getWidth()/2/MainSuperMario.PPM,bounds.getHeight()/2/MainSuperMario.PPM);
	fdef.shape=shape;
	fixture=body.createFixture(fdef);
	}
	public abstract void onHeadHit(Mario mario);
	public void setCategoryFilter(short bit){
		Filter filter=new Filter();
		filter.categoryBits=bit;
		fixture.setFilterData(filter);
	}
	public TiledMapTileLayer.Cell getCall(){
		TiledMapTileLayer layer= (TiledMapTileLayer) map.getLayers().get(1);
		return layer.getCell((int)(body.getPosition().x*MainSuperMario.PPM/16), (int)(body.getPosition().y*MainSuperMario.PPM/16));
		
	}

}
