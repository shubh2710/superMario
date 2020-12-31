package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mysupermario.shubh.MainSuperMario;

import screens.PlayScreen;

public abstract class Items  extends Sprite{

	protected World world;
	protected PlayScreen screen;
	protected Body body;
	protected boolean destroyed;
	protected boolean todestroy;
	protected Vector2 velocity;
	public Items(PlayScreen screen,float x,float y) {
		this.world=screen.getworld();
		this.screen=screen;
		setPosition(x,y);
		setBounds(getX(),getY(),16/MainSuperMario.PPM,16/MainSuperMario.PPM);
		defineItem();
		todestroy=false;
		destroyed=false;
	}
	protected abstract void defineItem();
	public abstract void use(Mario mario);
	public void draw(Batch batch){
			super.draw(batch);
	}
	public void update(float dt){
		if(todestroy && !destroyed){
			destroyed=true;
		}
	}
	public void reverceVeloctiy(boolean x, boolean y){
		if(x){
			velocity.x=-velocity.x;
		}
		if(y){
			velocity.y=-velocity.y;
		}
	}
	public void destroy(){
		todestroy=true;
		screen.deadBodies.add(body);
		screen.items.removeValue(this, true);
		System.out.println("destroy muhr");
	}

}

