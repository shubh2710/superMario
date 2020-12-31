package tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mysupermario.shubh.MainSuperMario;

import screens.PlayScreen;
import sprites.Emney;
import sprites.FireBall;
import sprites.Goomba;
import sprites.InterActiveTiledObject;
import sprites.Items;
import sprites.Mario;
import sprites.Mushroom;
import sprites.Tortol;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
	Fixture fixA=contact.getFixtureA();
	Fixture fixB=contact.getFixtureB();
	int cdef=fixA.getFilterData().categoryBits |fixB.getFilterData().categoryBits;
	switch(cdef){
	case MainSuperMario.MARIO_HEAD_BIT | MainSuperMario.BIRCK_BIT:
	case MainSuperMario.MARIO_HEAD_BIT | MainSuperMario.COIN_BIT:
		if(fixA.getFilterData().categoryBits==MainSuperMario.MARIO_HEAD_BIT)
			((InterActiveTiledObject) fixB.getUserData()).onHeadHit((Mario)fixA.getUserData());
		else
			((InterActiveTiledObject) fixA.getUserData()).onHeadHit((Mario)fixB.getUserData());
		break;
		case MainSuperMario.ENMEY_BIT | MainSuperMario.OBJECT_BIT:
			if(fixA.getFilterData().categoryBits==MainSuperMario.ENMEY_BIT)
				((Emney)fixA.getUserData()).reverceVeloctiy(true, false);
			else 
				((Emney)fixB.getUserData()).reverceVeloctiy(true, false);
			break;
		case MainSuperMario.ENMEY_HEAD_BIT | MainSuperMario.OBJECT_BIT:
			if(fixA.getFilterData().categoryBits==MainSuperMario.ENMEY_BIT)
				((Emney)fixA.getUserData()).reverceVeloctiy(true, false);
			else 
				((Emney)fixB.getUserData()).reverceVeloctiy(true, false);
			break;
		case MainSuperMario.MARIO_BIT | MainSuperMario.ENMEY_BIT:
			if(fixA.getFilterData().categoryBits==MainSuperMario.MARIO_BIT){
				((Mario)fixA.getUserData()).hit((Emney)fixB.getUserData());
			}
			else{
				((Mario)fixB.getUserData()).hit((Emney)fixA.getUserData());
				}
			break;
		case MainSuperMario.MARIO_BIT | MainSuperMario.WINNING_BIT:
			if(fixA.getFilterData().categoryBits==MainSuperMario.MARIO_BIT){
				((Mario)fixA.getUserData()).win();
			}
			else{
				((Mario)fixB.getUserData()).win();
				}
			break;
		case MainSuperMario.ENMEY_HEAD_BIT | MainSuperMario.MARIO_BIT:
			if(fixA.getFilterData().categoryBits==MainSuperMario.MARIO_BIT){
				((Emney)fixB.getUserData()).hitOnHead((Mario)fixA.getUserData());
			}
			else 
				((Emney)fixA.getUserData()).hitOnHead((Mario)fixB.getUserData());	
			break;
		case MainSuperMario.ENMEY_BIT | MainSuperMario.ENMEY_BIT:
			((Emney)fixA.getUserData()).onEnmeyHit((Emney)fixB.getUserData());
			((Emney)fixB.getUserData()).onEnmeyHit((Emney)fixA.getUserData());
		break;
		case MainSuperMario.ENMEY_HEAD_BIT | MainSuperMario.ENMEY_BIT:
			((Emney)fixA.getUserData()).onEnmeyHit((Emney)fixB.getUserData());
			((Emney)fixB.getUserData()).onEnmeyHit((Emney)fixA.getUserData());
		break;
		case MainSuperMario.ENMEY_HEAD_BIT | MainSuperMario.ENMEY_HEAD_BIT:
			((Emney)fixA.getUserData()).onEnmeyHit((Emney)fixB.getUserData());
			((Emney)fixB.getUserData()).onEnmeyHit((Emney)fixA.getUserData());
		break;
		case MainSuperMario.ENMEY_BIT | MainSuperMario.COIN_BIT:
			if(fixA.getFilterData().categoryBits==MainSuperMario.ENMEY_BIT)
				((Emney)fixA.getUserData()).reverceVeloctiy(true, false);
			else
			((Emney)fixB.getUserData()).reverceVeloctiy(true, false);
			break;
		case MainSuperMario.ENMEY_BIT | MainSuperMario.BIRCK_BIT:
			if(fixA.getFilterData().categoryBits==MainSuperMario.ENMEY_BIT)
				((Emney)fixA.getUserData()).reverceVeloctiy(true, false);
			else
			((Emney)fixB.getUserData()).reverceVeloctiy(true, false);
			break;
		case MainSuperMario.ITEM_BIT | MainSuperMario.OBJECT_BIT:
			if(fixA.getFilterData().categoryBits==MainSuperMario.ITEM_BIT)
				((Items)fixA.getUserData()).reverceVeloctiy(true, false);
			else 
				((Items)fixB.getUserData()).reverceVeloctiy(true, false);
			break;
		case MainSuperMario.ITEM_BIT | MainSuperMario.MARIO_BIT:
			if(fixA.getFilterData().categoryBits==MainSuperMario.ITEM_BIT){
				((Items)fixA.getUserData()).use((Mario)fixB.getUserData());
				}
			else{
				((Items)fixB.getUserData()).use((Mario)fixA.getUserData());
				}
			break;
		case    MainSuperMario.BULET_BIT |MainSuperMario.OBJECT_BIT:
			  if(fixA.getFilterData().categoryBits == MainSuperMario.BULET_BIT)
                  ((FireBall)fixA.getUserData()).setToDestroy();
              else
                  ((FireBall)fixB.getUserData()).setToDestroy();
              break;
	}
}
	@Override
	public void endContact(Contact contact) { 
		// TODO Auto-generated method stub
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		Fixture fixA=contact.getFixtureA();
		Fixture fixB=contact.getFixtureB();
		int cdef=fixA.getFilterData().categoryBits |fixB.getFilterData().categoryBits;
		switch(cdef){
		case MainSuperMario.ITEM_BIT | MainSuperMario.MARIO_BIT:
			if(fixA.getFilterData().categoryBits==MainSuperMario.ITEM_BIT){
				(((Mario)fixB.getUserData())).destroyMario();
				((Mushroom)fixA.getUserData()).destroy();
				}
			else{
				(((Mario)fixA.getUserData())).destroyMario();
				((Mushroom)fixB.getUserData()).destroy();
				}
			break;
		case MainSuperMario.ENMEY_HEAD_BIT | MainSuperMario.MARIO_BIT:
			if(fixA.getFilterData().categoryBits==MainSuperMario.MARIO_BIT){
				if((Emney)fixB.getUserData()  instanceof Goomba)
				((Goomba)fixB.getUserData()).destroy();
			}
			else {
				if((Emney)fixA.getUserData()  instanceof Goomba)
				((Goomba)fixA.getUserData()).destroy();
				}
			break;
		case MainSuperMario.MARIO_BIT | MainSuperMario.ENMEY_BIT:
			if(fixA.getFilterData().categoryBits==MainSuperMario.MARIO_BIT){
				((Mario)fixA.getUserData()).bighit((Emney)fixB.getUserData());
			}
			else{
				((Mario)fixB.getUserData()).bighit((Emney)fixA.getUserData());
				}
			break;
		case    MainSuperMario.BULET_BIT |MainSuperMario.ENMEY_BIT:
			  if(fixA.getFilterData().categoryBits == MainSuperMario.BULET_BIT){
                ((FireBall)fixA.getUserData()).setToDestroy();
                if((Emney)fixB.getUserData()  instanceof Goomba)
    				((Goomba)fixB.getUserData()).destroy();
			  	if((Emney)fixB.getUserData()  instanceof Tortol)
  				((Tortol)fixB.getUserData()).destroy();
			  	}
            else{
                ((FireBall)fixB.getUserData()).setToDestroy();
			  	if((Emney)fixA.getUserData()  instanceof Goomba)
  				((Goomba)fixA.getUserData()).destroy();
			  if((Emney)fixA.getUserData()  instanceof Tortol)
  				((Tortol)fixA.getUserData()).destroy();
            }
            break;
		}

	}
}
