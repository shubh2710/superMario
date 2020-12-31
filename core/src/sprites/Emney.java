package sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import screens.PlayScreen;

public abstract class Emney extends Sprite{
	protected World world;
	protected PlayScreen screen;
	public Body b2body;
	public Vector2 velocity;
	public Emney(PlayScreen screen,float x,float y){
		this.world=screen.getworld();
		this.screen=screen;
		setPosition(x,y);
		defineEnemy();
		velocity=new Vector2(1,0);
		b2body.setActive(false);
	}
	protected abstract void defineEnemy();
	public  abstract void hitOnHead(Mario mario);
	public abstract void dispose();
	public void reverceVeloctiy(boolean x, boolean y){
		if(x){
			velocity.x=-velocity.x;
		}
		if(y){
			velocity.y=-velocity.y;
		}
	}
	public abstract void update(float dt);
	public abstract void onEnmeyHit(Emney userData);
	public void destroy(){
		screen.deadBodies.add(b2body);
		screen.enemeys.removeValue(this, true);
		System.out.println("destroy muhr");
		}
}
