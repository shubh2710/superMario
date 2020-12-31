package com.mysupermario.shubh;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import screens.PlayScreen;

public class MainSuperMario extends Game {
	public SpriteBatch batch;
	public static final float PPM=100;
	public static final int V_WIDTH=400;
	public static final int V_HIGTH=208;
	
	public static final short NOTHING_BIT=0;
	public static final short GROUND_BIT=1;
	public static final short MARIO_BIT=2;
	public static final short BIRCK_BIT=4;
	public static final short COIN_BIT=8;
	public static final short DESTORYED_BIT=16;
	public static final short OBJECT_BIT=32;
	public static final short ENMEY_BIT = 64;
	public static final short ENMEY_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short MARIO_HEAD_BIT = 512;
	public static final short BULET_BIT = 1024;
	public static final short WINNING_BIT=2048;
	public static int LEVEL=1;
	
	//private AssetManager manager;
	public static Music music;
	public static Sound coin,bump,brick,jump,die,powerup,powerdown,winning,fireball;
	@Override
	public void create () {
		batch = new SpriteBatch();
		fireball=Gdx.audio.newSound(Gdx.files.internal("sounds/fireball.ogg"));
		winning=Gdx.audio.newSound(Gdx.files.internal("sounds/winning.ogg"));
		powerdown=Gdx.audio.newSound(Gdx.files.internal("sounds/powerup.ogg"));
		powerup=Gdx.audio.newSound(Gdx.files.internal("sounds/powerup.ogg"));
		die=Gdx.audio.newSound(Gdx.files.internal("sounds/mariodie.ogg"));
		jump=Gdx.audio.newSound(Gdx.files.internal("sounds/jump.ogg"));
		brick=Gdx.audio.newSound(Gdx.files.internal("sounds/breakblock.ogg"));
		coin=Gdx.audio.newSound(Gdx.files.internal("sounds/coin.ogg"));
		bump=Gdx.audio.newSound(Gdx.files.internal("sounds/bump.ogg"));
		music=Gdx.audio.newMusic(Gdx.files.internal("sounds/mario.MP3"));
		music.setLooping(true);
		music.setVolume(0.7f);
		music.play();
		// manager =new AssetManager();
		// manager.load("mario.mp3",Music.class);
		 //manager.load("breakblock.wav",Sound.class);
		 //manager.load("coin.wav",Sound.class);
		 //manager.load("bump.wav",Sound.class);
		 //manager.load("jump.wav",Sound.class);
		 //manager.finishLoading();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//manager.dispose();
		music.dispose();
		coin.dispose();
		bump.dispose();
		brick.dispose();
		jump.dispose();
		die.dispose();
		powerup.dispose();
		winning.dispose();
		fireball.dispose();
		powerdown.dispose();
		Gdx.app.exit();
	}
}
