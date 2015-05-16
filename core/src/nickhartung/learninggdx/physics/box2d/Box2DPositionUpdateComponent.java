package nickhartung.learninggdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import nickhartung.learninggdx.GDXTop;
import nickhartung.learninggdx.GameComponent;
import nickhartung.learninggdx.GameObject;
import nickhartung.libgdx.utilities.BaseObject;

/**
 * Created by guntrah on 5/16/2015.
 */
public class Box2DPositionUpdateComponent extends GameComponent {

    private Body mBody;

    public Box2DPositionUpdateComponent() {
        super();
        setPhase( GameComponent.ComponentPhases.MOVEMENT.ordinal() );
    }

    @Override
    public void reset() {
        this.mBody = null;
    }

    @Override
    public void update( final float pTimeDelta, final BaseObject pParent ) {
        final GameObject object = (GameObject)pParent;
        Vector2 bodyPosition = this.mBody.getPosition();
        object.getPosition().set( bodyPosition.x * GDXTop.PIXELS_PER_METER, bodyPosition.y * GDXTop.PIXELS_PER_METER );
    }

    public void setBody( final Body pBody ) {
        this.mBody = pBody;
    }
}
