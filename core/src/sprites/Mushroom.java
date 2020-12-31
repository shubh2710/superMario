package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mysupermario.shubh.MainSuperMario;

import screens.PlayScreen;

public class Mushroom extends Items{

	PlayScreen screen;
	
	public Mushroom(PlayScreen screen,float x,float y) {
		super(screen,x,y);
		this.screen=screen;
		setRegion(screen.getAtlas().findRegion("mashum"),0,0,16,16);
		velocity=new Vector2(0.7f,0);
	}
	@Override
	protected void defineItem() {
		// TODO Auto-generated method stub
		BodyDef bdef=new BodyDef();
		bdef.position.set(getX(),getY());
		bdef.type=BodyDef.BodyType.DynamicBody;
		if(!world.isLocked())
		body=world.createBody(bdef);
		FixtureDef fdef=new FixtureDef();
		CircleShape shape =new CircleShape();
		shape.setRadius(6/MainSuperMario.PPM);
		fdef.filter.categoryBits= MainSuperMario.ITEM_BIT;
		fdef.filter.maskBits= MainSuperMario.MARIO_BIT |
				 MainSuperMario.OBJECT_BIT|
				 MainSuperMario.GROUND_BIT|
				 MainSuperMario.COIN_BIT|
				 MainSuperMario.BIRCK_BIT;
		fdef.shape=shape;
		body.createFixture(fdef).setUserData(this);
	}
	@Override
	public void use(Mario mario) {
		if(!todestroy){
		if(mario.isBig()){
			mario.canFire=true;
		}else
		mario.grow();
		MainSuperMario.powerup.play();
		}
	}
	public void update(float dt){
		super.update(dt);
		setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
		velocity.y=body.getLinearVelocity().y;
		body.setLinearVelocity(velocity);
	}
	public void destroy(){
		todestroy=true;
		screen.deadBodies.add(body);
		screen.items.removeValue(this, true);
		System.out.println("destroy muhr");
	}
	
	



}
