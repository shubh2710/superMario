package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mysupermario.shubh.MainSuperMario;
import scenes.Hud;
import screens.PlayScreen;

public class Brick  extends InterActiveTiledObject {

	public Brick(PlayScreen screen,MapObject object){
		super(screen,object);
		fixture.setUserData(this);
		setCategoryFilter(MainSuperMario.BIRCK_BIT);
	}

	@Override
	public void onHeadHit(Mario mario) {
		// TODO Auto-generated method stub
		if(mario.isBig()){
		setCategoryFilter(MainSuperMario.DESTORYED_BIT);
		getCall().setTile(null);
		Hud.addScore(200);
		//MainSuperMario.manager.get("breakblock.wav",Sound.class).play();
		MainSuperMario.brick.play();
		}else {
			//MainSuperMario.manager.get("bump.wav",Sound.class).play();
			MainSuperMario.bump.play();
		}
	}
	public void dispose(){
	
	}
}
