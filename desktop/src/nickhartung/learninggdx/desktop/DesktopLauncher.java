package nickhartung.learninggdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import nickhartung.learninggdx.GDXTop;

public class DesktopLauncher {
	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.height  = 540;
        config.width   = 960;
        config.title   = "LearningGDX";
        config.useGL30 = false;

        new LwjglApplication(new GDXTop(), config);
	}
}
