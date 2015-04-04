package nickhartung.learninggdx;

import nickhartung.libgdx.utilities.BaseObject;

/**
 * Created by Nick on 3/28/2015.
 */
public class DumbMovementComponent extends GameComponent {

    public float mMaxSpeed;
    public float mAcceleration;
    private boolean mFlip;

    public DumbMovementComponent() {
        super();
        setPhase( ComponentPhases.MOVEMENT.ordinal() );
    }

    @Override
    public void update( final float pTimeDelta, final BaseObject pParent ) {
        final GameObject object = (GameObject)pParent;
        object.getAcceleration().set( this.mAcceleration, this.mAcceleration );
        if( object.getPosition().x > 900.0f && !this.mFlip ) {
            this.mFlip = !this.mFlip;
        } else if( object.getPosition().x < 0.0f && this.mFlip ) {
            this.mFlip = !this.mFlip;
        }
        if( !this.mFlip ) {
            object.getTargetVelocity().set( this.mMaxSpeed, 0.0f );
        } else {
            object.getTargetVelocity().set( this.mMaxSpeed * -1.0f, 0.0f );
        }
    }

    @Override
    public void reset() {
        this.mMaxSpeed     = 0.0f;
        this.mAcceleration = 0.0f;
        this.mFlip         = false;
    }

    public void setMovementSpeed( final float pSpeed, final float pAcceleration ) {
        this.mMaxSpeed = pSpeed;
        this.mAcceleration = pAcceleration;
    }
}
