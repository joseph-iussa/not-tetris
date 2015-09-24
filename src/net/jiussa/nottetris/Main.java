package net.jiussa.nottetris;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.APP_WIDTH;
        config.height = Constants.APP_HEIGHT;
        config.resizable = false;
        new LwjglApplication(new Game(), config);
    }
}