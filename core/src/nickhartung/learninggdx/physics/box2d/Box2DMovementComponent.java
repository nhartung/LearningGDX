package nickhartung.learninggdx.physics.box2d;

import com.badlogic.gdx.physics.box2d.Body;

import nickhartung.learninggdx.GameComponent;
import nickhartung.learninggdx.GameObject;
import nickhartung.learninggdx.ObjectRegistry;
import nickhartung.libgdx.utilities.BaseObject;
import nickhartung.libgdx.utilities.Interpolator;

/**
 * Created by Nick on 3/29/2015.
 */
public class Box2DMovementComponent extends GameComponent {

    // If multiple game components were ever running in different threads, this would need
    // to be non-static.
    private static Interpolator sInterpolator = new Interpolator();
    private Body mBody;

    public Box2DMovementComponent() {
        super();
        setPhase( GameComponent.ComponentPhases.THINK.ordinal() );
    }

    @Override
    public void reset() {
        this.mBody = null;
    }

    @Override
    public void update( final float pTimeDelta, final BaseObject pParent ) {

        Box2DSystem box2DSystem = ObjectRegistry.box2DSystem;
        final GameObject object = (GameObject)pParent;

        sInterpolator.set( object.getVelocity().x, object.getTargetVelocity().x,
                           object.getAcceleration().x );
        sInterpolator.interpolate( pTimeDelta );
        float newVelocityX = sInterpolator.getCurrent();

        sInterpolator.set( object.getVelocity().y, object.getTargetVelocity().y,
                           object.getAcceleration().y );
        sInterpolator.interpolate( pTimeDelta );
        float newVelocityY = sInterpolator.getCurrent();

        final float dx = newVelocityX - object.getVelocity().x;
        final float dy = newVelocityY - object.getVelocity().y;
        final float mass = this.mBody.getMass();
        this.mBody.applyForce( mass * ( dx / pTimeDelta ),
                               mass * ( dy / pTimeDelta ),
                               this.mBody.getWorldCenter().x, this.mBody.getWorldCenter().y, true );
        //this.mBody.setLinearVelocity( newVelocityX, newVelocityY );

        object.getVelocity().set( newVelocityX, newVelocityY );
    }

    public void setBody( final Body pBody ) {
        this.mBody = pBody;
    }
}
