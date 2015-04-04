package nickhartung.learninggdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import nickhartung.libgdx.utilities.BaseObject;

/**
 * Created by Nick on 3/29/2015.
 */
public class Box2DUpdateObjectComponent extends GameComponent {

    private Body mBody;

    public Box2DUpdateObjectComponent() {
        super();
        setPhase( GameComponent.ComponentPhases.THINK.ordinal() );
    }

    @Override
    public void reset() {
        if( this.mBody != null ) {
            returnBody();
        }
    }

    @Override
    public void update( final float pTimeDelta, final BaseObject pParent ) {
        final GameObject object = (GameObject)pParent;
        object.setPosition( this.mBody.getPosition() );
        object.setVelocity( this.mBody.getLinearVelocity() );
    }

    public void setBody( final Body pBody ) {
        this.mBody = pBody;
    }

    private void returnBody() {
        final World world = ObjectRegistry.box2Dworld;
        world.destroyBody( this.mBody );
        this.mBody = null;
    }
}
