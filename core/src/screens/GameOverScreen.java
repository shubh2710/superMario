package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mysupermario.shubh.MainSuperMario;

public class GameOverScreen implements Screen{
	public Stage stage;
	private Viewport viewport;
	private Game game;
	public GameOverScreen(Game game) {
		this.game=game;
		viewport=new FitViewport(MainSuperMario.V_WIDTH,MainSuperMario.V_HIGTH,new OrthographicCamera());
		stage=new Stage(viewport,(( MainSuperMario ) game).batch);
		Label.LabelStyle font=new Label.LabelStyle(new BitmapFont(),Color.WHITE);
		Table table=new Table();
		table.center();
		table.setFillParent(true);
		 Label gameover=new Label("GAME OVER",font);
		 Label playAgain=new Label("Play Again",font);
		 table.add(gameover).expandX();
		 table.row();
		 table.add(playAgain).expandX().pad(10);
		 stage.addActor(table);
	} 

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		if(Gdx.input.justTouched()){
			game.setScreen(new PlayScreen((MainSuperMario)game));
			MainSuperMario.music.play();
			MainSuperMario.music.setLooping(true);
			MainSuperMario.music.setVolume(0.7f);
		}
		Gdx.gl.glClearColor(0, 0, 0,0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	stage.dispose();
	}

}
