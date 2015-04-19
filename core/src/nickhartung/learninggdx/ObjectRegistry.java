package nickhartung.learninggdx;

import box2dLight.RayHandler;
import nickhartung.learninggdx.physics.box2d.Box2DSystem;
import nickhartung.libgdx.render.RenderSystem;

/**
 * Created by Nick on 3/16/2015.
 */
public class ObjectRegistry {

    public static RenderSystem renderSystem;
    public static RayHandler   rayHandler;
    public static InputSystem  inputSystem;
    public static Box2DSystem  box2DSystem;
}
