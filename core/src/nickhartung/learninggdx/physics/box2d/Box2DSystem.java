package nickhartung.learninggdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import nickhartung.libgdx.utilities.BaseObject;
import nickhartung.utilities.FixedSizeArray;

/**
 * Created by guntrah on 4/9/2015.
 */
public class Box2DSystem extends BaseObject {

    private int mVelocityIterations;
    private int mPositionIterations;
    private World mWorld;

    public Box2DSystem( final Vector2 pGravity, final boolean pSleep, final int pVelocityIterations, final int pPositionIterations ) {
        this.mWorld = new World( pGravity, pSleep );
        this.mVelocityIterations = pVelocityIterations;
        this.mPositionIterations = pPositionIterations;
    }

    public Body createBody( final BodyDef pBodyDef, final FixedSizeArray<FixtureDef> pFixtureDefs ) {
        Body retBody = this.mWorld.createBody( pBodyDef );
        final int count = pFixtureDefs.getCount();
        Object[] fixtures = pFixtureDefs.getArray();
        for( int i = 0; i < count; i++ ) {
            final FixtureDef fixture = (FixtureDef)fixtures[ i ];
            retBody.createFixture( fixture );
        }
        return retBody;
    }

    public Body createBody( final BodyDef pBodyDef, final FixtureDef pFixtureDef ) {
        Body retBody = this.mWorld.createBody( pBodyDef );
        retBody.createFixture( pFixtureDef );
        return retBody;
    }

    public void destroyBody( final Body pBody ) {
        this.mWorld.destroyBody( pBody );
    }

    public World getWorld() {
        return this.mWorld;
    }

    @Override
    public void update( final float pTimeDelta, final BaseObject pParent ) {
        this.mWorld.step( pTimeDelta, this.mVelocityIterations, this.mPositionIterations );
    }

    @Override
    public void reset() {
        this.mWorld.dispose();
    }
}
