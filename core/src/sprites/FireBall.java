package sprites;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mysupermario.shubh.MainSuperMario;
import screens.PlayScreen;

public class FireBall extends Sprite {

    PlayScreen screen;
    World world;
    Array<TextureRegion> frames;
    Animation fireAnimation;
    float stateTime;
    boolean destroyed;
    boolean setToDestroy;
    boolean fireRight;

    Body b2body;
    public FireBall(PlayScreen screen, float x, float y, boolean fireRight){
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getworld();
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("smallmario"), 8, 0, 8, 8));
        }
        fireAnimation = new Animation(0.2f, frames);
         setRegion((TextureRegion) fireAnimation.getKeyFrame(0));
        setBounds(x, y, 6 / MainSuperMario.PPM, 6 / MainSuperMario.PPM);
        defineFireBall();
    }  
 
    public void defineFireBall(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 12 /MainSuperMario.PPM : getX() - 12 /MainSuperMario.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!world.isLocked())
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3 / MainSuperMario.PPM);
        fdef.filter.categoryBits = MainSuperMario.BULET_BIT;
        fdef.filter.maskBits = MainSuperMario.GROUND_BIT |
        		MainSuperMario.COIN_BIT |
        		MainSuperMario.BIRCK_BIT |
        		MainSuperMario.ENMEY_BIT |
        		MainSuperMario.OBJECT_BIT;

        fdef.shape = shape;
        fdef.restitution = 1;
        fdef.friction = 0;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearVelocity(new Vector2(fireRight ? 2.5f : -2.5f, 2.5f));
    }

    public void update(float dt){
        stateTime += dt;
        setRegion((TextureRegion) fireAnimation.getKeyFrame(stateTime, true));
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if((stateTime > 2 || setToDestroy) && !destroyed) {
        	if(!world.isLocked()){
        	b2body.setUserData(null);
            }
        	Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run () {
					world.destroyBody(b2body);
					System.out.println("fire ball destryed");
				}
			});
        	//screen.bodyToRemove.add(b2body);
            destroyed = true;
        }
        if(b2body.getLinearVelocity().y > 2f)
            b2body.setLinearVelocity(b2body.getLinearVelocity().x, 2f);
        if((fireRight && b2body.getLinearVelocity().x < 0) || (!fireRight && b2body.getLinearVelocity().x > 0))
            setToDestroy();
    }

    public void setToDestroy(){
        setToDestroy = true;
    }
    public boolean isDestroyed(){
        return destroyed;
    }
	public Body getBody() {
		// TODO Auto-generated method stub
		return b2body;
	}


}

