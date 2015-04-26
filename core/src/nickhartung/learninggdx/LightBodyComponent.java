package nickhartung.learninggdx;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import nickhartung.libgdx.utilities.BaseObject;

/**
 * Created by Nick on 3/28/2015.
 */
public class LightBodyComponent extends GameComponent {

    private Body mBody;
    private Vector2 mOffset;

    public LightBodyComponent() {
        super();
        setPhase( ComponentPhases.POST_COLLISION.ordinal() );
        this.mOffset = new Vector2();
    }

    @Override
    public void update( final float pTimeDelta, final BaseObject pParent ) {
        if( this.mBody != null ) {
            final GameObject object = (GameObject)pParent;
            final Vector2 position = object.getPosition();
            this.mBody.setTransform( position.x + this.mOffset.x, position.y + this.mOffset.y, 0.0f );
        }
    }

    @Override
    public void reset() {
        if( this.mBody != null ) {
            returnBody();
        }
        this.mOffset.setZero();
    }

    public void createBody( final BodyDef pBodyDef, final FixtureDef pFixtureDef ) {
        final World world = ObjectRegistry.box2DSystem.getWorld();
        if( this.mBody != null ) {
            returnBody();
        }
        this.mBody = world.createBody( pBodyDef );
        this.mBody.createFixture( pFixtureDef );
    }

    public void setOffset( final float pOffsetX, final float pOffsetY ) {
        this.mOffset.set( pOffsetX, pOffsetY );
    }

    private void returnBody() {
        final World world = ObjectRegistry.box2DSystem.getWorld();
        world.destroyBody( this.mBody );
        this.mBody = null;
    }
}
