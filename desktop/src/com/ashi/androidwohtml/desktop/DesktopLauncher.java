package com.ashi.androidwohtml.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ashi.androidwohtml.androidwohtml;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = androidwohtml.WINDOW_HEIGHT;
		config.width = androidwohtml.WINDOW_WIDTH;
		new LwjglApplication(new androidwohtml(), config);
	}
}
