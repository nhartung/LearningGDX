package nickhartung.learninggdx.physics.box2d;

import com.badlogic.gdx.physics.box2d.World;

import nickhartung.learninggdx.GameComponent;
import nickhartung.libgdx.utilities.BaseObject;

/**
 * Created by Nick on 4/4/2015.
 */
public class Box2DWorldComponent extends GameComponent {

    private World mWorld;

    public Box2DWorldComponent() {
        super();
        this.setPhase( ComponentPhases.MOVEMENT.ordinal() );
    }

    @Override
    public void reset() {
        this.mWorld.dispose();
        this.mWorld = null;
    }

    @Override
    public void update( final float pTimeDelta, final BaseObject pParent ) {
        assert( this.mWorld != null );
        //this.mWorld.step( pTimeDelta, 6, 2 );
        this.mWorld.step( pTimeDelta, 6, 2 );
    }

    public void setWorld( final World pWorld ) {
        this.mWorld = pWorld;
    }
}
