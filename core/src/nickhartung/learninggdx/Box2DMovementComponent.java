package nickhartung.learninggdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import nickhartung.libgdx.utilities.BaseObject;

/**
 * Created by Nick on 3/29/2015.
 */
public class Box2DMovementComponent extends GameComponent {

    private Body mBody;

    public Box2DMovementComponent() {
        super();
        setPhase( GameComponent.ComponentPhases.MOVEMENT.ordinal() );
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
        final Vector2 targetVelocity = object.getTargetVelocity();
        final Vector2 currentVelocity = object.getVelocity();
        final Vector2 thrust = targetVelocity.sub( currentVelocity );
        //this.mBody.applyForceToCenter( thrust.scl( object.getAcceleration() ), true );
        this.mBody.applyLinearImpulse( thrust.scl( object.getAcceleration() ), this.mBody.getWorldCenter(), true );
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
