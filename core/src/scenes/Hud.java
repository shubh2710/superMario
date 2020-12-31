package scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mysupermario.shubh.MainSuperMario;

public class Hud implements Disposable {

	public Stage stage;
	private Viewport viewport;
	private Integer worldTimer;
	private float timeCount;
	private static Integer score;
	Label countdownLable;
	static	Label scoreLabel;
	Label timeLable;
	Label levelLable;
	Label marioLable;
	Label worldLable;
	
	public Hud(SpriteBatch sb){
		worldTimer=300;
		timeCount=0;
		score=0;
		viewport=new FitViewport(MainSuperMario.V_WIDTH,MainSuperMario.V_HIGTH,new OrthographicCamera());
		stage=new Stage(viewport,sb);
		Table table=new Table();
		table.top();
		table.setFillParent(true);
		 countdownLable=new Label(String.format("%03d",worldTimer),new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		 scoreLabel=new  Label(String.format("%06d",score),new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		 timeLable=new Label("TIME",new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		 levelLable=new Label("1-"+MainSuperMario.LEVEL,new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		marioLable=new Label("MARIO",new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		worldLable=new Label("WORLD",new Label.LabelStyle(new BitmapFont(),Color.WHITE));

		table.add(marioLable).expandX().padTop(10);
		table.add(worldLable).expandX().padTop(10);
		table.add(timeLable).expandX().padTop(10);
		table.row();
		table.add(scoreLabel).expandX();
		table.add(levelLable).expandX();
		table.add(countdownLable).expandX();
		stage.addActor(table);	
	}
	public void update(float dt){
		timeCount+=dt;
		if(timeCount>=1){
			worldTimer--;
			countdownLable.setText(String.format("%03d",worldTimer));
			timeCount=0;
		}
	}
	public static void addScore(int value){
		score+=value;
		scoreLabel.setText(String.format("%06d",score));	
	}

	@Override
	public void dispose() {
		stage.dispose();
		
	}
}
