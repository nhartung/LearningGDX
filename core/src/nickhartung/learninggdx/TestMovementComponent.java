package nickhartung.learninggdx;

import nickhartung.libgdx.utilities.BaseObject;

/**
 * Created by Nick on 3/21/2015.
 */
public class TestMovementComponent extends GameComponent {

    public float mMaxSpeed;
    public float mAcceleration;

    public TestMovementComponent() {
        super();
        this.reset();
        this.setPhase( GameComponent.ComponentPhases.THINK.ordinal() );
    }

    public void setMovementSpeed( final float pSpeed, final float pAcceleration ) {
        this.mMaxSpeed = pSpeed;
        this.mAcceleration = pAcceleration;
    }

    @Override
    public void update( float timeDelta, BaseObject parent ) {
        assert( parent != null );
        GameObject parentObject = (GameObject)parent;
        parentObject.getTargetVelocity().set( this.mMaxSpeed, 0.0f );
        parentObject.getAcceleration().set( this.mAcceleration, 0.0f );
    }

    @Override
    public void reset() {
        this.mMaxSpeed     = 0.0f;
        this.mAcceleration = 0.0f;
    }
}
