/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nickhartung.libgdx.utilities;

import nickhartung.utilities.FixedSizeArray;

/**
 * ObjectManagers are "group nodes" in the game graph.  They contain child objects, and updating
 * an object manager invokes update on its children.  ObjectManagers themselves are derived from
 * BaseObject, so they may be strung together into a hierarchy of objects.  ObjectManager may
 * be specialized to implement special types of traversals (e.g. PhasedObjectManager sorts its
 * children).
 */

public class ObjectManager extends BaseObject {
    protected static final int DEFAULT_ARRAY_SIZE = 64;

    private FixedSizeArray<BaseObject> mObjects;
    private FixedSizeArray<BaseObject> mPendingAdditions;
    private FixedSizeArray<BaseObject> mPendingRemovals;

    public ObjectManager() {
        super();
        this.mObjects = new FixedSizeArray<BaseObject>( DEFAULT_ARRAY_SIZE );
        this.mPendingAdditions = new FixedSizeArray<BaseObject>( DEFAULT_ARRAY_SIZE );
        this.mPendingRemovals = new FixedSizeArray<BaseObject>( DEFAULT_ARRAY_SIZE );
    }

    public ObjectManager( final int arraySize ) {
        super();
        this.mObjects = new FixedSizeArray<BaseObject>( arraySize );
        this.mPendingAdditions = new FixedSizeArray<BaseObject>( arraySize );
        this.mPendingRemovals = new FixedSizeArray<BaseObject>( arraySize );
    }

    @Override
    public void reset() {
        commitUpdates();
        final int count = this.mObjects.getCount();
        for( int i = 0; i < count; i++ ) {
            BaseObject object = this.mObjects.get( i );
            object.reset();
        }
    }

    public void commitUpdates() {
        final int additionCount = this.mPendingAdditions.getCount();
        if( additionCount > 0 ) {
            final Object[] additionsArray = this.mPendingAdditions.getArray();
            for( int i = 0; i < additionCount; i++ ) {
                BaseObject object = (BaseObject)additionsArray[ i ];
                this.mObjects.add( object );
            }
            this.mPendingAdditions.clear();
        }

        final int removalCount = this.mPendingRemovals.getCount();
        if( removalCount > 0 ) {
            final Object[] removalsArray = this.mPendingRemovals.getArray();

            for( int i = 0; i < removalCount; i++ ) {
                BaseObject object = (BaseObject)removalsArray[ i ];
                this.mObjects.remove( object, true );
            }
            this.mPendingRemovals.clear();
        }
    }

    @Override
    public void update( final float timeDelta, final BaseObject parent ) {
        commitUpdates();
        final int count = this.mObjects.getCount();
        if( count > 0 ) {
            final Object[] objectArray = this.mObjects.getArray();
            for( int i = 0; i < count; i++ ) {
                BaseObject object = (BaseObject)objectArray[ i ];
                object.update( timeDelta, this );
            }
        }
    }

    public final FixedSizeArray<BaseObject> getObjects() {
        return this.mObjects;
    }

    public final int getCount() {
        return this.mObjects.getCount();
    }

    /**
     * Returns the count after the next commitUpdates() is called.
     */
    public final int getConcreteCount() {
        return this.mObjects.getCount() + this.mPendingAdditions.getCount() - this.mPendingRemovals.getCount();
    }

    public final BaseObject get( final int index ) {
        return this.mObjects.get( index );
    }

    public void add( final BaseObject object ) {
        this.mPendingAdditions.add( object );
    }

    public void remove( final BaseObject object ) {
        this.mPendingRemovals.add( object );
    }

    public void removeAll() {
        final int count = this.mObjects.getCount();
        final Object[] objectArray = this.mObjects.getArray();
        for( int i = 0; i < count; i++ ) {
            this.mPendingRemovals.add( (BaseObject)objectArray[ i ] );
        }
        this.mPendingAdditions.clear();
    }

    /**
     * Finds a child object by its type.  Note that this may invoke the class loader and therefore
     * may be slow.
     *
     * @param classObject The class type to search for (e.g. BaseObject.class).
     * @return
     */
    public <T> T findByClass( final Class<T> classObject ) {
        T object = null;
        final int count = this.mObjects.getCount();
        for( int i = 0; i < count; i++ ) {
            BaseObject currentObject = this.mObjects.get( i );
            if( currentObject.getClass() == classObject ) {
                object = classObject.cast( currentObject );
                break;
            }
        }
        return object;
    }

    protected FixedSizeArray<BaseObject> getPendingObjects() {
        return this.mPendingAdditions;
    }

}

