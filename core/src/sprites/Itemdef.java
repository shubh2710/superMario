package sprites;

import com.badlogic.gdx.math.Vector2;

public class Itemdef {

	public Vector2 position;
	public Class<?> type;
	public Itemdef(Vector2 position,Class<?> type) {
		this.position=position;
		this.type=type;
	}
}
