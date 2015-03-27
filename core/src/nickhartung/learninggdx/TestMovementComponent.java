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
        assert ( parent != null );
        float targetX = 0.0f;
        float targetY = 0.0f;
        GameObject parentObject = (GameObject)parent;
        InputSystem input = ObjectRegistry.sInputSystem;
        parentObject.getAcceleration().set( this.mAcceleration, this.mAcceleration );
        if( input.moveUp() ) {
            targetY = this.mMaxSpeed;
        }
        if( input.moveDown() ) {
            targetY = this.mMaxSpeed * -1;
        }
        if( input.moveRight() ) {
            targetX = this.mMaxSpeed;
        }
        if( input.moveLeft() ) {
            targetX = this.mMaxSpeed * -1;
        }
        parentObject.getTargetVelocity().set( targetX, targetY );
    }

    @Override
    public void reset() {
        this.mMaxSpeed = 0.0f;
        this.mAcceleration = 0.0f;
    }
}
