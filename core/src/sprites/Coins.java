package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mysupermario.shubh.MainSuperMario;

import scenes.Hud;
import screens.PlayScreen;

public class Coins extends InterActiveTiledObject {

	private static TiledMapTileSet tileset;
	private final int BLACK_COIN=28;
	public Coins(PlayScreen screen,MapObject object){
		super(screen,object);
		tileset=map.getTileSets().getTileSet("5257");
		fixture.setUserData(this);
		setCategoryFilter(MainSuperMario.COIN_BIT);
	} 
	@Override
	public void onHeadHit(Mario mario) {
		// TODO Auto-generated method stub
		if(getCall().getTile().getId()==BLACK_COIN){
			//MainSuperMario.manager.get("bump.wav",Sound.class).play();
			MainSuperMario.bump.play();
			Hud.addScore(0);
		}else{
			if(object.getProperties().containsKey("mushroom"))
			screen.spawnItem(new Itemdef(new Vector2(body.getPosition().x,body.getPosition().y+16/MainSuperMario.PPM),Mushroom.class));
			//MainSuperMario.manager.get("coin.wav",Sound.class).play();
			MainSuperMario.coin.play();
			getCall().setTile(tileset.getTile(BLACK_COIN));
			Hud.addScore(100);
		}
	}
	public void dispose(){
	}
}
