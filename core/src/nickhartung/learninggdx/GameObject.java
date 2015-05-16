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
package nickhartung.learninggdx;

import com.badlogic.gdx.math.Vector2;

import nickhartung.libgdx.utilities.BaseObject;
import nickhartung.libgdx.utilities.PhasedObject;
import nickhartung.libgdx.utilities.PhasedObjectManager;

/**
 * GameObject defines any object that resides in the game world (character, background, special
 * effect, enemy, etc).  It is a collection of GameComponents which implement its behavior;
 * GameObjects themselves have no intrinsic behavior.  GameObjects are also "bags of data" that
 * components can use to share state (direct component-to-component communication is discouraged).
 */
public class GameObject extends PhasedObjectManager {
    public static final int ALL_PHASES = -1;

    // These fields are managed by components.
    private Vector2 mPosition;
    private Vector2 mVelocity;
    private Vector2 mTargetVelocity;
    private Vector2 mAcceleration;
    private Vector2 mImpulse;
    private Vector2 mFacingDirection;

    private int mStartPhase;
    private int mEndPhase;

    public float width;
    public float height;

    private static final int DEFAULT_LIFE = 1;

    public enum ActionType {
        IDLE,
        MOVE,
        INVALID,
    }

    private ActionType mCurrentAction;

    public GameObject() {
        super();

        this.mPosition = new Vector2();
        this.mVelocity = new Vector2();
        this.mTargetVelocity = new Vector2();
        this.mAcceleration = new Vector2();
        this.mImpulse = new Vector2();

        this.mFacingDirection = new Vector2( 1, 0 );

        reset();
    }

    @Override
    public void update( final float timeDelta, final BaseObject parent ) {
        commitUpdates();
        final int count = this.getObjects().getCount();
        if( count > 0 ) {
            final Object[] objectArray = this.getObjects().getArray();
            for( int i = 0; i < count; i++ ) {
                PhasedObject object = (PhasedObject)objectArray[ i ];
                final int phase = object.phase;
                if( phase >= this.mStartPhase && phase <= this.mEndPhase ) {
                    object.update( timeDelta, this );
                }
            }
        }
    }

    @Override
    public void reset() {
        removeAll();
        commitUpdates();

        this.mPosition.setZero();
        this.mVelocity.setZero();
        this.mTargetVelocity.setZero();
        this.mAcceleration.setZero();
        this.mImpulse.setZero();
        this.mFacingDirection.set( 1.0f, 1.0f );

        this.mCurrentAction = ActionType.INVALID;
        this.width = 0.0f;
        this.height = 0.0f;
        this.setUpdatePhases( ALL_PHASES, ALL_PHASES );
    }

    public final Vector2 getPosition() {
        return this.mPosition;
    }

    public final void setPosition( final Vector2 position ) {
        this.mPosition.set( position );
    }

    public final float getCenteredPositionX() {
        return this.mPosition.x + ( width / 2.0f );
    }

    public final float getCenteredPositionY() {
        return this.mPosition.y + ( height / 2.0f );
    }

    public final Vector2 getVelocity() {
        return this.mVelocity;
    }

    public final void setVelocity( final Vector2 velocity ) {
        this.mVelocity.set( velocity );
    }

    public final Vector2 getTargetVelocity() {
        return this.mTargetVelocity;
    }

    public final void setTargetVelocity( final Vector2 targetVelocity ) {
        this.mTargetVelocity.set( targetVelocity );
    }

    public final Vector2 getAcceleration() {
        return this.mAcceleration;
    }

    public final void setAcceleration( final Vector2 acceleration ) {
        this.mAcceleration.set( acceleration );
    }

    public final Vector2 getImpulse() {
        return this.mImpulse;
    }

    public final void setImpulse( final Vector2 impulse ) {
        this.mImpulse.set( impulse );
    }

    public final ActionType getCurrentAction() {
        return this.mCurrentAction;
    }

    public final void setCurrentAction( final ActionType type ) {
        this.mCurrentAction = type;
    }

    public final Vector2 getFacingDirection() {
        return this.mFacingDirection;
    }

    public final void setFacingDirection( final Vector2 pFacingDirection ) {
        this.mFacingDirection = pFacingDirection;
    }

    public void setUpdatePhases( final int pStartPhase, final int pEndPhase ) {
        if( pStartPhase == ALL_PHASES ) {
            this.mStartPhase = 0;
        } else {
            this.mStartPhase = pStartPhase;
        }
        if( pEndPhase == ALL_PHASES ) {
            this.mEndPhase = Integer.MAX_VALUE;
        } else {
            this.mEndPhase   = pEndPhase;
        }
    }
}
