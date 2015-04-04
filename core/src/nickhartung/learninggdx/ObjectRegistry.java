package nickhartung.learninggdx;

import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;
import nickhartung.libgdx.render.RenderSystem;

/**
 * Created by Nick on 3/16/2015.
 */
public class ObjectRegistry {

    public static RenderSystem renderSystem;
    public static RayHandler   rayHandler;
    public static InputSystem  inputSystem;
    public static World        box2Dworld;
}
