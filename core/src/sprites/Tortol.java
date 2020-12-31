package sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mysupermario.shubh.MainSuperMario;

import screens.PlayScreen;

public class Tortol extends Emney {

	public enum State {WALKING,STANDING_SHELL,MOVING_SHELL,DEAD};
	public State currentState;
	public State previousState;
	private float stateTime;
	private Animation walkAnimation;
	private Array<TextureRegion> frames;
	private boolean setTodestroy;
	private boolean destroy;
	public static final int KICK_LEFT_SPEED=-2;
	public static final int KICK_RIGHT_SPEED=2;
	private TextureRegion shell;
	private float deadRotationDeegre;
	public Tortol(PlayScreen screen ,float x,float y) {
		super(screen, x, y);
		frames =new Array<TextureRegion>();
		frames.add(new TextureRegion(screen.getAtlas().findRegion("duck"),0,0,16,20));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("duck"),16,0,16,20));
		shell=new TextureRegion(screen.getAtlas().findRegion("duck"),64,0,16,20);
		walkAnimation=new Animation(0.2f,frames);
		currentState=previousState=State.WALKING;
		deadRotationDeegre=0;
		setBounds(getX(),getY(),16/MainSuperMario.PPM,20/MainSuperMario.PPM);
		
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
		fdef.restitution=1.5f;
		fdef.filter.categoryBits=MainSuperMario.ENMEY_HEAD_BIT;
		b2body.createFixture(fdef).setUserData(this);
	}

	@Override
	public void hitOnHead(Mario mario) {
		// TODO Auto-generated method stub
		if(currentState!=State.STANDING_SHELL){
			currentState=State.STANDING_SHELL;
			velocity.x=0;
		}
		else{
			kick(mario.getX()<= this.getX() ? KICK_RIGHT_SPEED :KICK_LEFT_SPEED);
		}
	} 
	public void kick(int speed){
		velocity.x=speed;
		currentState=State.MOVING_SHELL;
	}
	public State getCurrentState(){
		return currentState;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	public TextureRegion getFrames(float dt){
		TextureRegion region;
		switch(currentState){
		case MOVING_SHELL:
		case STANDING_SHELL:
			region=shell;
			break;
		case WALKING:
			default:
				region=(TextureRegion) walkAnimation.getKeyFrame(stateTime,true);
				break;
		}
		if(velocity.x>0 && region.isFlipX()==false){
			region.flip(true, false);
		}
		if(velocity.x<0 && region.isFlipX()==true){
			region.flip(true, false);
		}
		stateTime=currentState==previousState ? stateTime +dt:0;
		previousState= currentState;
		return region;
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		setRegion(getFrames(dt));
		if(currentState==State.STANDING_SHELL && stateTime>5){
			currentState=State.WALKING;
			velocity.x=1;
		}
		setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-8/MainSuperMario.PPM);
		if(currentState==State.DEAD){
			deadRotationDeegre+=3;
			rotate(deadRotationDeegre);
			if(stateTime >5 && !destroy){
				screen.deadBodies.add(b2body);
				screen.enemeys.removeValue(this, true);
				b2body.setUserData(null);
				destroy=true;
			}
		}else
		b2body.setLinearVelocity(velocity);
	}

	@Override
	public void onEnmeyHit(Emney enemy) {
		if(enemy instanceof Tortol){
			if(((Tortol) enemy).currentState==State.MOVING_SHELL && currentState !=State.MOVING_SHELL){
				killed();
			}else if(currentState==State.MOVING_SHELL && ((Tortol) enemy).currentState==State.WALKING)
				return;
			else reverceVeloctiy(true,false);
		}
		else if (currentState !=State.MOVING_SHELL)
			reverceVeloctiy(true,false);
		
	}
	public void killed(){
		currentState=State.DEAD;
		Filter filter=new Filter();
		filter.maskBits=MainSuperMario.NOTHING_BIT;
		for(Fixture fixture : b2body.getFixtureList()){
			fixture.setFilterData(filter);
			b2body.applyLinearImpulse(new Vector2(0,2f), b2body.getWorldCenter(), true);
		}
	}
	public void draw(Batch batch){
		if(!destroy)
		super.draw(batch);	
	}
	public void destroy(){
		killed();
	}

}
