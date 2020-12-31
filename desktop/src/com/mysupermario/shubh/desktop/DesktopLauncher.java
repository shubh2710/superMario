package com.mysupermario.shubh.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mysupermario.shubh.MainSuperMario;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.title = MyGdxGame.TITALe;
        config.width =MainSuperMario.V_WIDTH;
        config.height = MainSuperMario.V_HIGTH;
		new LwjglApplication(new MainSuperMario(), config);
	}
}
