package nickhartung.learninggdx.physics.box2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import nickhartung.learninggdx.GameObject;
import nickhartung.learninggdx.physics.CollisionComponent;
import nickhartung.libgdx.utilities.BaseObject;
import nickhartung.utilities.FixedSizeArray;

/**
 * Created by guntrah on 4/9/2015.
 */
public class Box2DSystem extends BaseObject {

    private int mVelocityIterations;
    private int mPositionIterations;
    private World mWorld;

    private int mObjectCount;
    private int mMaxObjects;
    private FixedSizeArray<GameObject> mUpdateQueue;
    private int mNumQueuedObjects;
    private HashElement[] mObjectHash;
    private GameObject mNullObject;
    private GameObject mTombstoneObject;
    // These would need to be made non-static if we were to ever have multiple Box2DSystems in separate threads.
    private static FixedSizeArray<FixtureDef> sFixtureWorker = new FixedSizeArray<FixtureDef>( 1 );
    private static FixedSizeArray<CollisionComponent> sCollisionWorker = new FixedSizeArray<CollisionComponent>( 1 );

    public Box2DSystem( final Vector2 pGravity,
                        final boolean pSleep,
                        final int pVelocityIterations,
                        final int pPositionIterations,
                        final int pNumObjects,
                        final ContactListener pContactListener )
    {
        this.mWorld = new World( pGravity, pSleep );
        this.mVelocityIterations = pVelocityIterations;
        this.mPositionIterations = pPositionIterations;
        this.mMaxObjects = pNumObjects;
        this.mUpdateQueue = new FixedSizeArray<GameObject>( pNumObjects );

        // Make the hash twice the size needed to avoid many collisions
        final int hashSize = this.mMaxObjects * 2;
        this.mObjectHash = new HashElement[ hashSize ];
        // Create our null and tombstone objects.
        this.mNullObject      = new GameObject();
        this.mTombstoneObject = new GameObject();
        for( int i = 0; i < hashSize; i++ ) {
            this.mObjectHash[ i ]     = new HashElement();
            this.mObjectHash[ i ].key = this.mNullObject;
        }

        if( pContactListener != null ) {
            this.mWorld.setContactListener( pContactListener );
        }
    }

    public Body createBody( final BodyDef pBodyDef,
                            final FixedSizeArray<FixtureDef> pFixtureDefs,
                            final FixedSizeArray<CollisionComponent> pCollisions,
                            final GameObject pHostObject )
    {
        Body retBody = null;
        if( this.mObjectCount < this.mMaxObjects ) {
            retBody = this.mWorld.createBody( pBodyDef );
            final int defCount       = pFixtureDefs.getCount();
            Object[] fixtureDefs     = pFixtureDefs.getArray();
            final int collisionCount = pCollisions.getCount();
            Object[] collisions      = pCollisions.getArray();
            for( int i = 0; i < defCount; i++ ) {
                final FixtureDef fixtureDef = (FixtureDef)fixtureDefs[ i ];
                final Fixture fixture = retBody.createFixture( fixtureDef );
                if( i < collisionCount ) {
                    final CollisionComponent collision = (CollisionComponent)collisions[ i ];
                    fixture.setUserData( collision );
                } else {
                    Gdx.app.debug( "Box2DSystem", "Fixture does not have collision component. Is this intentional?" );
                }
            }
            this.addToHash( pHostObject, retBody );
            retBody.setUserData( pHostObject );
            this.mObjectCount++;
        } else {
            Gdx.app.error( "Box2DSystem", "Box2d Bodies have been exhausted!" );
        }
        return retBody;
    }

    public Body createBody( final BodyDef pBodyDef,
                            final FixtureDef pFixtureDef,
                            final CollisionComponent pCollision,
                            final GameObject pHostObject  ) {
        sFixtureWorker.clear();
        sCollisionWorker.clear();
        sFixtureWorker.add( pFixtureDef );
        sCollisionWorker.add( pCollision );
        return this.createBody( pBodyDef, sFixtureWorker, sCollisionWorker, pHostObject );
    }

    public void destroyBody( final GameObject pObject ) {
        final Body body = this.removeFromHash( pObject );
        if( body != null ) {
            this.mObjectCount--;
            this.mWorld.destroyBody( body );
        }
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

    private Body getBodyFromHash( final GameObject pKey ) {
        Body retBody = null;
        final int index = this.getHashIndex( pKey );
        final int realIndex = this.findFirstKey( index, pKey );
        if( realIndex != -1 ) {
            retBody = this.mObjectHash[ realIndex ].value;
        }
        return retBody;
    }

    private void addToHash( final GameObject pKey, final Body pValue ) {
        final int index = this.getHashIndex( pKey );
        final int realIndex = this.findFirstKey( index, this.mNullObject );
        if( realIndex != -1 ) {
            this.mObjectHash[ realIndex ].key   = pKey;
            this.mObjectHash[ realIndex ].value = pValue;
        }
    }

    /**
     *
     * @param pKey The GameObject to use as the key for the Hash
     * @return The body removed from the Hash, if found, null otherwise.
     */
    private Body removeFromHash( final GameObject pKey ) {
        Body retBody = null;
        final int index     = this.getHashIndex( pKey );
        final int realIndex = this.findFirstKey( index, pKey );
        if( realIndex != -1 ) {
            retBody = this.mObjectHash[ realIndex ].value;
            this.mObjectHash[ realIndex ].key   = this.mTombstoneObject;
            this.mObjectHash[ realIndex ].value = null;
        }
        return retBody;
    }

    private int getHashIndex( final GameObject pKey ) {
        return pKey.hashCode() % this.mObjectHash.length;
    }

    private int findFirstKey( final int pIndex, final GameObject pKey ) {
        int index = -1;
        for (int i = 0; i < this.mObjectHash.length; i++) {
            final int actualIndex = ( pIndex + i ) % this.mObjectHash.length;
            final GameObject object = this.mObjectHash[ actualIndex ].key;
            final boolean isNull = object.equals( this.mNullObject );
            if( object.equals( pKey ) ) {
                index = actualIndex;
                break;
            } else if( object.equals( this.mTombstoneObject ) && isNull ) {
                // Accounting for the case when we are searching for an open spot in the hash, and the open spot
                // is a tombstone object.
                index = actualIndex;
            } else if( isNull ) {
                Gdx.app.error( "Box2DSystem", "Object does not exist in Hash!" );
                break;
            }
        }
        return index;
    }

    private class HashElement {
        public GameObject     key;
        public Body           value;

        public HashElement() {
            this.key   = null;
            this.value = null;
        }

        public HashElement( final GameObject pKey, final Body pValue ) {
            this.key   = pKey;
            this.value = pValue;
        }
    }
}
