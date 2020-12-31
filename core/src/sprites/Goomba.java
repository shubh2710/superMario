package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mysupermario.shubh.MainSuperMario;

import screens.PlayScreen;

public class Goomba extends Emney {

	private float stateTime;
	private Animation walkAnimation;
	private Array<TextureRegion> frames;
	private boolean setTodestroy;
	private boolean destroy;
	public Goomba(PlayScreen screen ,float x,float y){
		super(screen,x,y);
		frames=new Array<TextureRegion>();
		for(int i=0;i<2;i++)
			frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"),i*18,0,18,16));
			walkAnimation=new Animation(0.4f,frames);
			stateTime=0;
			setBounds(getX(),getY(),16/MainSuperMario.PPM,16/MainSuperMario.PPM);
			setTodestroy=false;
			destroy=false;
	}
	public void update(float dt){
		stateTime+=dt;
		if(setTodestroy && !destroy){
			//world.destroyBody(b2body);
			//screen.deadBodies.add(b2body);
			//screen.enemeys.removeValue(this, true);
			destroy=true;
			setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"),16,0,16,16));
			stateTime=0;
		}else if(!destroy){
			b2body.setLinearVelocity(velocity);
		setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
		setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime,true));
		}
	}
	@Override
	protected void defineEnemy() {
		// TODO Auto-generated method stub
		BodyDef bdef=new BodyDef();
		bdef.position.set(getX(),getY());
		bdef.type=BodyDef.BodyType.DynamicBody;
		if(!world.isLocked())
		b2body=world.createBody(bdef);
		
		FixtureDef fdef=new FixtureDef();
		CircleShape shape =new CircleShape();
		shape.setRadius(6/MainSuperMario.PPM);
		fdef.filter.categoryBits=MainSuperMario.ENMEY_BIT;
		fdef.filter.maskBits=MainSuperMario.GROUND_BIT|
								MainSuperMario.COIN_BIT |
								MainSuperMario.BIRCK_BIT|
								MainSuperMario.ENMEY_BIT|
								MainSuperMario.OBJECT_BIT|
								MainSuperMario.BULET_BIT|
								MainSuperMario.MARIO_BIT;
		fdef.shape=shape;
		b2body.createFixture(fdef).setUserData(this);
		PolygonShape head=new PolygonShape();
		Vector2[] vertice=new Vector2[4];
		vertice[0]=new Vector2(-6,7).scl(1/MainSuperMario.PPM);
		vertice[1]=new Vector2(6,7).scl(1/MainSuperMario.PPM);
		vertice[2]=new Vector2(-3,3).scl(1/MainSuperMario.PPM);
		vertice[3]=new Vector2(3,3).scl(1/MainSuperMario.PPM);
		head.set(vertice);
		fdef.shape=head;
		fdef.restitution=0.5f;
		fdef.filter.categoryBits=MainSuperMario.ENMEY_HEAD_BIT;
		b2body.createFixture(fdef).setUserData(this);
		
	}
	@Override
	public void hitOnHead(Mario mario) {
		// TODO Auto-generated method stub
		setTodestroy=true;
	}
	@Override
	public void draw(Batch batch) {
		if(!destroy || stateTime<1){
			super.draw(batch);
		}
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onEnmeyHit(Emney enmey) {
		if(enmey instanceof Tortol &&((Tortol) enmey).currentState== Tortol.State.MOVING_SHELL){
			setTodestroy=true;
			b2body.setUserData(null);
			screen.deadBodies.add(b2body);
			screen.enemeys.removeValue(this, true);
		}else 
			reverceVeloctiy(true,false);
	}
	public void destroy(){
		screen.deadBodies.add(b2body);
		screen.enemeys.removeValue(this, true);
		System.out.println("destroy goomba");
	}

}
