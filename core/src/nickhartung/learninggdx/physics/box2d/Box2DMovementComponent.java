package nickhartung.learninggdx.physics.box2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import nickhartung.learninggdx.GDXTop;
import nickhartung.learninggdx.GameComponent;
import nickhartung.learninggdx.GameObject;
import nickhartung.learninggdx.ObjectRegistry;
import nickhartung.libgdx.utilities.BaseObject;
import nickhartung.libgdx.utilities.Interpolator;

/**
 * Created by Nick on 3/29/2015.
 */
public class Box2DMovementComponent extends GameComponent {

    private Body mBody;
    // If multiple game components were ever running in different threads, this would need
    // to be non-static.
    private static Interpolator sInterpolator = new Interpolator();

    public Box2DMovementComponent() {
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
        object.setPosition( this.mBody.getPosition().scl( GDXTop.PIXELS_PER_METER ) );

        sInterpolator.set( object.getVelocity().x, object.getTargetVelocity().x,
                           object.getAcceleration().x );
        sInterpolator.interpolate( pTimeDelta );
        float newVelocityX = sInterpolator.getCurrent();

        sInterpolator.set( object.getVelocity().y, object.getTargetVelocity().y,
                           object.getAcceleration().y );
        sInterpolator.interpolate( pTimeDelta );
        float newVelocityY = sInterpolator.getCurrent();

        System.out.println( this.mBody.getMass() );
        final float dx = newVelocityX - object.getVelocity().x;
        final float dy = newVelocityY - object.getVelocity().y;
        final float mass = this.mBody.getMass();
        this.mBody.applyForce( mass * ( dx / pTimeDelta ),
                                       mass * ( dy / pTimeDelta ),
                                       this.mBody.getWorldCenter().x, this.mBody.getWorldCenter().y, true );
        //this.mBody.setLinearVelocity( newVelocityX, newVelocityY );

        object.getVelocity().set( newVelocityX, newVelocityY );


        //System.out.println( "Box2D: ---------------" );
        //System.out.println( "Velocity X: " + this.mBody.getLinearVelocity().x + " Velocity Y: " + this.mBody.getLinearVelocity().y );
        //System.out.println( "Position X: " + object.getPosition().x + " Position Y: " + object.getPosition().y );
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
