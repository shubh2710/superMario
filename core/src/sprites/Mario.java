package sprites;

import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mysupermario.shubh.MainSuperMario;
import screens.PlayScreen;
import sprites.Tortol.State;

public class Mario extends Sprite{
	public enum State{FALLING,JUMPING,STANDING,RUNNING,GROWING,DEAD,IMMORTAL,WINNING};
	public State currentState;
	public State previousState;
	public World world;
	public Body b2body;
	private TextureRegion marioStand;
	private Animation marioRun;
	private TextureRegion marioJump;
	private TextureRegion bigmarioStand;
	private Animation bigmarioRun;
	public boolean canFire;
	private TextureRegion bigmarioJump;
	private TextureRegion marioDead;
	private Animation growMario;
	private Animation imortalMario;
	private boolean runGrowAnimation;
	private boolean marioIsBig;
	private LinkedBlockingQueue<Itemdef> itemToSpawn;
	private float stateTimer;
	private boolean runningRight;
	private boolean isSmallMarioDead;
	private boolean isTimeToDefineBigMario;
	private boolean timeToRedefineMario;
	private boolean isMarioDead;
	private float lifeLineTime;
	private Array<FireBall> fireballs;
	private PlayScreen screen;
	public boolean canDie;
	public boolean isComplete;
	private Vector2 currentposition;
	private boolean isWin;
	public Mario(PlayScreen screen) {
		this.screen=screen;
		this.world=screen.getworld();
		currentState=State.STANDING;
		previousState=State.STANDING;
		stateTimer=0;
		runningRight=true;
		Array<TextureRegion> frames=new Array<TextureRegion>(); 
		for(int i=1;i<4;i++)
			frames.add(new TextureRegion(screen.getAtlas().findRegion("smallmario"),i*16,0,16,16));
		marioRun=new Animation(0.1f,frames);
		frames.clear();
		for(int i=1;i<4;i++)
			frames.add(new TextureRegion(screen.getAtlas().findRegion("bigmario"),i*16 ,0,16,32));
		bigmarioRun=new Animation(0.1f,frames);
		frames.clear();
		
		//grow
		frames.add(new TextureRegion(screen.getAtlas().findRegion("bigmario"),240,0,16,32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("bigmario"),0,0,16,32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("bigmario"),240,0,16,32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("bigmario"),0,0,16,32));
		growMario=new Animation(0.2f,frames);
		frames.clear();
		frames.add(new TextureRegion(screen.getAtlas().findRegion("smallmario"),0,0,16,16));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("smallmario"),0,0,16,16));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("smallmario"),0,0,16,16));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("smallmario"),0,0,0,16));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("smallmario"),0,0,16,16));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("smallmario"),0,0,0,16));
		imortalMario=new Animation(0.2f,frames);
		
		marioJump=new TextureRegion(screen.getAtlas().findRegion("smallmario"),77,0,16,16);
		bigmarioJump=new TextureRegion(screen.getAtlas().findRegion("bigmario"),80,0,16,32);
		marioDead=new TextureRegion(screen.getAtlas().findRegion("smallmario"),93,0,16,16);
		//stand
		marioStand=new TextureRegion(screen.getAtlas().findRegion("smallmario"),0,0,16,16);
		bigmarioStand=new TextureRegion(screen.getAtlas().findRegion("bigmario"),0,0,16,32);
		defineMario();
		setBounds(0,0,16/MainSuperMario.PPM,16/MainSuperMario.PPM);
		setRegion(marioStand);
		fireballs = new Array<FireBall>();
		itemToSpawn =new LinkedBlockingQueue<Itemdef>();
		isSmallMarioDead=false;
		canDie=true;
		lifeLineTime=0;
		canFire=false;
		isComplete=false;
	}

	public void spawnItem(Itemdef idef){
		try{
		itemToSpawn.add(idef);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void handleSpawninItes(){
		if(!itemToSpawn.isEmpty()){
			Itemdef idef=itemToSpawn.poll();
			try{
			if(idef.type== FireBall.class){
				fireballs.add(new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
			}
		 	}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void grow(){
		runGrowAnimation=true;
		isSmallMarioDead=true;
		isTimeToDefineBigMario=true;
		//screen.deadBodies.add(b2body);
		setBounds(getX(),getY(),getWidth(),getHeight()*2);
	}
	public void update(float dt){
		handleSpawninItes();
		if(marioIsBig)
			setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2-6/MainSuperMario.PPM);
		else
			setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
		if(b2body.getPosition().y-getHeight()/2<0 && !isMarioDead){
			MainSuperMario.die.play();
			isMarioDead=true;
			MainSuperMario.music.stop();
			isMarioDead=true;
			Filter filter=new Filter();
			filter.maskBits=MainSuperMario.NOTHING_BIT;
			for(Fixture fixture:b2body.getFixtureList()){
				fixture.setFilterData(filter);
			}
		}
		setRegion(getFrame(dt));
		if(isTimeToDefineBigMario){
			defineBigMario();
			marioIsBig=true;
			}
		if(timeToRedefineMario){
			redefineMario();
			marioIsBig=false;
		}
		
		   for(FireBall  ball : fireballs) {
			   if(ball!=null && ball.destroyed) {
	                  fireballs.removeValue(ball,true);
	         }
	            else
	            	ball.update(dt);
	        }
		   lifeLine(dt);
	}
	private void redefineMario() {
		Vector2 currentposition=b2body.getPosition();
		BodyDef bdef=new BodyDef();
		bdef.position.set(currentposition);
		bdef.type=BodyDef.BodyType.DynamicBody;
		b2body=world.createBody(bdef);
		FixtureDef fdef=new FixtureDef();
		CircleShape shape =new CircleShape();
		shape.setRadius(5/MainSuperMario.PPM);
		fdef.filter.categoryBits=MainSuperMario.MARIO_BIT;
		fdef.filter.maskBits=MainSuperMario.GROUND_BIT |
								MainSuperMario.COIN_BIT |
								MainSuperMario.BIRCK_BIT |
								MainSuperMario.ENMEY_BIT|
								MainSuperMario.WINNING_BIT|
								MainSuperMario.OBJECT_BIT|
								MainSuperMario.ENMEY_HEAD_BIT|
								MainSuperMario.ITEM_BIT;
		fdef.shape=shape;
		b2body.createFixture(fdef).setUserData(this);
		EdgeShape head=new EdgeShape();
		head.set(new Vector2(-2/MainSuperMario.PPM, 6/MainSuperMario.PPM),new Vector2(2/MainSuperMario.PPM, 6/MainSuperMario.PPM));
		fdef.filter.categoryBits=MainSuperMario.MARIO_HEAD_BIT;
		fdef.shape=head;
		fdef.isSensor=true;
		b2body.createFixture(fdef).setUserData(this);
		marioIsBig=false;
		timeToRedefineMario=false;
		setBounds(0,0,16/MainSuperMario.PPM,16/MainSuperMario.PPM);
	}
	private void defineBigMario(){
		currentposition=b2body.getPosition();
		BodyDef bdef=new BodyDef();
		bdef.position.set(currentposition.add(0,10/MainSuperMario.PPM));
		bdef.type=BodyDef.BodyType.DynamicBody;
		b2body=world.createBody(bdef);
		FixtureDef fdef=new FixtureDef();
		CircleShape shape =new CircleShape();
		shape.setRadius(5/MainSuperMario.PPM);
		fdef.filter.categoryBits=MainSuperMario.MARIO_BIT;
		fdef.filter.maskBits=MainSuperMario.GROUND_BIT |
								MainSuperMario.COIN_BIT |
								MainSuperMario.BIRCK_BIT |
								MainSuperMario.ENMEY_BIT|
								MainSuperMario.WINNING_BIT|
								MainSuperMario.OBJECT_BIT|
								MainSuperMario.ENMEY_HEAD_BIT|
								MainSuperMario.ITEM_BIT;
		fdef.shape=shape;
		b2body.createFixture(fdef).setUserData(this);
		shape.setPosition(new Vector2(0,-14/MainSuperMario.PPM));
		b2body.createFixture(fdef).setUserData(this);
		
		EdgeShape head=new EdgeShape();
		head.set(new Vector2(-2/MainSuperMario.PPM, 6/MainSuperMario.PPM),new Vector2(2/MainSuperMario.PPM, 6/MainSuperMario.PPM));
		fdef.filter.categoryBits=MainSuperMario.MARIO_HEAD_BIT;
		fdef.shape=head;
		fdef.isSensor=true;
		b2body.createFixture(fdef).setUserData(this);
		marioIsBig=true;
		setBounds(0,0,16/MainSuperMario.PPM,32/MainSuperMario.PPM);
		isTimeToDefineBigMario=false;
	}
	private TextureRegion getFrame(float dt) {
		currentState=getState();
		TextureRegion region;
		switch(currentState){
		case IMMORTAL:
			region=(TextureRegion) imortalMario.getKeyFrame(stateTimer,true);
			break;
		case GROWING:
			region=(TextureRegion) growMario.getKeyFrame(stateTimer);
			if(growMario.isAnimationFinished(stateTimer))
				runGrowAnimation=false;
				break;
		case JUMPING:
			region= marioIsBig? bigmarioJump:marioJump;
			break;
		case DEAD:
			region= marioDead;
			break;
		case RUNNING: 
			region=marioIsBig?(TextureRegion) bigmarioRun.getKeyFrame(stateTimer,true):(TextureRegion) marioRun.getKeyFrame(stateTimer,true);
			break;
		case FALLING:
		case STANDING:
			default:
			region = marioIsBig? bigmarioStand:marioStand;
			break;
		
		}
		if((b2body.getLinearVelocity().x<0 || !runningRight)&& !region.isFlipX()){
			region.flip(true, false);
			runningRight=false;
		}else if((b2body.getLinearVelocity().x>0 || runningRight)&& region.isFlipX()){
			region.flip(true, false);
			runningRight=true;
		}
		stateTimer=currentState==previousState ? stateTimer +dt:0;
		previousState= currentState;
		return region;
	}
	private State getState() {
		if(isWin)
			return State.WINNING;
		if(!canDie)
			return State.IMMORTAL;
		else if(isMarioDead)
			return State.DEAD;
		else if(runGrowAnimation)
		return State.GROWING;
		else if(b2body.getLinearVelocity().y>0 ||  b2body.getLinearVelocity().y<0 && previousState==State.JUMPING)
		return State.JUMPING;
		else if(b2body.getLinearVelocity().y<0)
			return State.FALLING;
			else if(b2body.getLinearVelocity().x!=0)
				return State.RUNNING;
			else
				return State.STANDING;
	}
	private void defineMario() {
		BodyDef bdef=new BodyDef();
		bdef.position.set(32/MainSuperMario.PPM,32/MainSuperMario.PPM);
		bdef.type=BodyDef.BodyType.DynamicBody;
		b2body=world.createBody(bdef);
		FixtureDef fdef=new FixtureDef();
		CircleShape shape =new CircleShape();
		shape.setRadius(5/MainSuperMario.PPM);
		fdef.filter.categoryBits=MainSuperMario.MARIO_BIT;
		fdef.filter.maskBits=MainSuperMario.GROUND_BIT |
								MainSuperMario.COIN_BIT |
								MainSuperMario.BIRCK_BIT |
								MainSuperMario.ENMEY_BIT|
								MainSuperMario.OBJECT_BIT|
								MainSuperMario.WINNING_BIT|
								MainSuperMario.ENMEY_HEAD_BIT|
								MainSuperMario.ITEM_BIT;
		fdef.shape=shape;
		b2body.createFixture(fdef).setUserData(this);
		EdgeShape head=new EdgeShape();
		head.set(new Vector2(-2/MainSuperMario.PPM, 6/MainSuperMario.PPM),new Vector2(2/MainSuperMario.PPM, 6/MainSuperMario.PPM));
		fdef.filter.categoryBits=MainSuperMario.MARIO_HEAD_BIT;
		fdef.shape=head;
		fdef.isSensor=true;
		b2body.createFixture(fdef).setUserData(this);
	}
	public boolean isBig() {
		return marioIsBig;
	}
	public void hit(Emney enmey) {
		if(enmey instanceof Tortol && ((Tortol)enmey).getCurrentState()==Tortol.State.STANDING_SHELL){
			((Tortol)enmey).kick(this.getX()<=enmey.getX() ?Tortol.KICK_RIGHT_SPEED:Tortol.KICK_LEFT_SPEED);
		}else{
	 	if(marioIsBig){
	 	lifeLineTime=0;
	 	canDie=false;
	 	canFire=false;
		MainSuperMario.powerdown.play();
		//screen.deadBodies.add(b2body);
		marioIsBig=false;
		isSmallMarioDead=false;
		timeToRedefineMario=true;
		setBounds(getX(),getY(),getWidth(),getHeight()/2);
		}else if(canDie){
			MainSuperMario.die.play();
			MainSuperMario.music.stop();
			isMarioDead=true;
			Filter filter=new Filter();
			filter.maskBits=MainSuperMario.NOTHING_BIT;
			for(Fixture fixture:b2body.getFixtureList()){
				fixture.setFilterData(filter);
			}
			b2body.applyLinearImpulse(new Vector2(0,4f), b2body.getWorldCenter(), true);
			}
		}
	}
	   public void fire(){
		   if(canFire){
		   spawnItem(new Itemdef(new Vector2(b2body.getPosition().x,b2body.getPosition().y+16/MainSuperMario.PPM),FireBall.class));
		   MainSuperMario.fireball.play();
		   }
	    }
	public boolean isDead(){
		return isMarioDead;
	}
	public boolean isSmallMarioDead(){
		return isSmallMarioDead;
	}
	public float getStateTimer(){
		return stateTimer; 
	}
	   public void draw(Batch batch){
	         super.draw(batch);
	        for(int i=0;i<fireballs.size;i++){
	        	FireBall ball=fireballs.get(i);
	        	if(!ball.isDestroyed())
		        	 ball.draw(batch);
	        	else
	        		fireballs.removeValue(ball, true);
	        }
	    }
	   public void destroyMario(){
		   if(isTimeToDefineBigMario || timeToRedefineMario){
			   screen.deadBodies.add(b2body);
			   isSmallMarioDead=true;
			   System.out.println("mario body destroyed by 1");
		   }
		   else{
		   }
		}
	   public void bighit(Emney enmey){
		   if(enmey instanceof Tortol && ((Tortol)enmey).getCurrentState()==Tortol.State.STANDING_SHELL){
			}else{
				if(isTimeToDefineBigMario || timeToRedefineMario){
		 		screen.deadBodies.add(b2body);
		 		System.out.println("mario body destroyed by 2");
		 		}
			}
		}
	   public void lifeLine(float dt){
		   lifeLineTime+=dt;
		   if(lifeLineTime<10 && !canDie){
			   canDie=false;
			   Filter filter=new Filter();
			   filter.maskBits=MainSuperMario.GROUND_BIT |
						MainSuperMario.COIN_BIT |
						MainSuperMario.BIRCK_BIT |
						//MainSuperMario.ENMEY_BIT|
						MainSuperMario.OBJECT_BIT|
						MainSuperMario.ENMEY_HEAD_BIT|
						MainSuperMario.WINNING_BIT|
						MainSuperMario.ITEM_BIT;
		   }
		   else{
			   canDie=true;
			   Filter filter=new Filter();
			   filter.maskBits=MainSuperMario.GROUND_BIT |
						MainSuperMario.COIN_BIT |
						MainSuperMario.BIRCK_BIT |
						MainSuperMario.ENMEY_BIT|
						MainSuperMario.OBJECT_BIT|
						MainSuperMario.ENMEY_HEAD_BIT|
						MainSuperMario.WINNING_BIT|
						MainSuperMario.ITEM_BIT;
			  // System.out.println(" not immortal"+lifeLineTime);
			   }
	   }
	   public void win(){
		   if(!isComplete){
		   isWin=true;
		   isComplete=true;
		   stateTimer=0;
		   MainSuperMario.LEVEL++;
		   MainSuperMario.music.stop();
		   MainSuperMario.winning.play();
		   }
	   }

}
