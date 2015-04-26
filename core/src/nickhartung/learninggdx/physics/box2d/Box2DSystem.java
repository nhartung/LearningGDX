package nickhartung.learninggdx.physics.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import nickhartung.learninggdx.GDXTop;
import nickhartung.learninggdx.GameObject;
import nickhartung.libgdx.utilities.BaseObject;
import nickhartung.utilities.FixedSizeArray;

/**
 * Created by guntrah on 4/9/2015.
 */
public class Box2DSystem extends BaseObject {

    private int mVelocityIterations;
    private int mPositionIterations;
    private World mWorld;

    private int mBodyCount;
    private int mMaxBodies;
    private FixedSizeArray<Body> mUpdateQueue;
    private int mNumQueuedObjects;
    private HashElement[] mObjectHash;
    private Body mNullBody;
    private Body mTombstoneBody;
    // This would need to be made non-static if we were to ever have multiple Box2DSystems in separate threads.
    private static FixedSizeArray<FixtureDef> sFixtureWorker = new FixedSizeArray<FixtureDef>( 1 );

    public Box2DSystem( final Vector2 pGravity, final boolean pSleep, final int pVelocityIterations, final int pPositionIterations, final int pNumBodies ) {
        this.mWorld = new World( pGravity, pSleep );
        this.mVelocityIterations = pVelocityIterations;
        this.mPositionIterations = pPositionIterations;
        this.mMaxBodies = pNumBodies;
        this.mUpdateQueue = new FixedSizeArray<Body>( pNumBodies );

        // Make the hash twice the size needed to avoid many collisions
        final int hashSize = this.mMaxBodies * 2;
        this.mObjectHash = new HashElement[ hashSize ];
        // Create our null and tombstone bodies. This is necessary because we cannot construct bodies without calling
        // "createBody" on our Box2D World.
        BodyDef def         = new BodyDef();
        this.mNullBody      = this.mWorld.createBody( def );
        this.mTombstoneBody = this.mWorld.createBody( def );
        for( int i = 0; i < hashSize; i++ ) {
            this.mObjectHash[ i ] = new HashElement();
            this.mObjectHash[ i ].key = this.mNullBody;
        }
    }

    public Body createBody( final BodyDef pBodyDef, final FixedSizeArray<FixtureDef> pFixtureDefs, final GameObject pHostObject ) {
        Body retBody = null;
        if( this.mBodyCount < this.mMaxBodies ) {
            retBody = this.mWorld.createBody( pBodyDef );
            final int count = pFixtureDefs.getCount();
            Object[] fixtures = pFixtureDefs.getArray();
            for( int i = 0; i < count; i++ ) {
                final FixtureDef fixture = (FixtureDef)fixtures[ i ];
                retBody.createFixture( fixture );
            }
            this.addToHash( retBody, pHostObject );
        } else {
            Gdx.app.error( "Box2DSystem", "Box2d Bodies have been exhausted!" );
        }
        return retBody;
    }

    public Body createBody( final BodyDef pBodyDef, final FixtureDef pFixtureDef, final GameObject pHostObject  ) {
        sFixtureWorker.clear();
        sFixtureWorker.add( pFixtureDef );
        return this.createBody( pBodyDef, sFixtureWorker, pHostObject );
    }

    public void destroyBody( final Body pBody ) {
        this.removeFromHash( pBody );
        this.mWorld.destroyBody( pBody );
    }

    public World getWorld() {
        return this.mWorld;
    }

    public void updateObjectPosition( final Body pBody ) {
        GameObject object = this.getObjectFromHash( pBody );
        Vector2 bodyPosition = pBody.getPosition();
        object.getPosition().set( bodyPosition.x * GDXTop.PIXELS_PER_METER, bodyPosition.y * GDXTop.PIXELS_PER_METER );
    }

    /**
     *  Schedules a body (used as a key into the Hash) whose linked GameObject should be updated to the position of
     *  this body.
     *
     * @param pBody The body to use as the key in the Hash
     */
    public void scheduleForUpdate( final Body pBody ) {
        this.mUpdateQueue.add( pBody );
    }

    @Override
    public void update( final float pTimeDelta, final BaseObject pParent ) {
        this.mWorld.step( pTimeDelta, this.mVelocityIterations, this.mPositionIterations );
        final int numBodies = this.mUpdateQueue.getCount();
        final Object[] objects = this.mUpdateQueue.getArray();
        for( int i = 0; i < numBodies; i++ ) {
            final Body body = (Body)objects[ i ];
            this.updateObjectPosition( body );
        }
        this.mUpdateQueue.clear();
    }

    @Override
    public void reset() {
        this.mWorld.dispose();
    }

    private GameObject getObjectFromHash( final Body pKey ) {
        GameObject retObject = null;
        final int index = this.getHashIndex( pKey );
        final int realIndex = this.findFirstKey( index, pKey );
        if( realIndex != -1 ) {
            retObject = this.mObjectHash[ realIndex ].value;
        }
        return retObject;
    }

    private void addToHash( final Body pKey, final GameObject pValue ) {
        final int index = this.getHashIndex( pKey );
        final int realIndex = this.findFirstKey( index, this.mNullBody );
        if( realIndex != -1 ) {
            this.mObjectHash[ realIndex ].key   = pKey;
            this.mObjectHash[ realIndex ].value = pValue;
        }
    }

    private void removeFromHash( final Body pKey ) {
        final int index = this.getHashIndex( pKey );
        final int realIndex = this.findFirstKey( index, pKey );
        if( realIndex != -1 ) {
            this.mObjectHash[ realIndex ].key   = this.mTombstoneBody;
            this.mObjectHash[ realIndex ].value = null;
        }
    }

    private int getHashIndex( final Body pKey ) {
        return pKey.hashCode() % this.mObjectHash.length;
    }

    private int findFirstKey( final int pIndex, final Body pKey ) {
        int index = -1;
        for (int i = 0; i < this.mObjectHash.length; i++) {
            final int actualIndex = ( pIndex + i ) % this.mObjectHash.length;
            if( this.mObjectHash[ actualIndex ].key.equals( pKey ) ) {
                index = actualIndex;
                break;
            } else if( this.mObjectHash[ actualIndex ].key.equals( this.mNullBody ) ) {
                Gdx.app.error( "Box2DSystem", "Object does not exist in Hash!" );
                break;
            }
        }
        return index;
    }

    private class HashElement {
        public Body       key;
        public GameObject value;

        public HashElement() {
            this.key   = null;
            this.value = null;
        }

        public HashElement( final Body pKey, final GameObject pValue ) {
            this.key   = pKey;
            this.value = pValue;
        }
    }
}
