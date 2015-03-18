package nickhartung.learninggdx;

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

import com.badlogic.gdx.math.Vector2;

import nickhartung.libgdx.utilities.PhasedObjectManager;

/**
 * GameObject defines any object that resides in the game world (character, background, special
 * effect, enemy, etc).  It is a collection of GameComponents which implement its behavior;
 * GameObjects themselves have no intrinsic behavior.  GameObjects are also "bags of data" that
 * components can use to share state (direct component-to-component communication is discouraged).
 */
public class GameObject extends PhasedObjectManager {
    // These fields are managed by components.
    private Vector2 mPosition;
    private Vector2 mVelocity;
    private Vector2 mTargetVelocity;
    private Vector2 mAcceleration;
    private Vector2 mImpulse;
    private Vector2 mFacingDirection;

    public float width;
    public float height;

    private static final int DEFAULT_LIFE = 1;

    public enum ActionType {
        INVALID,
    }

    private ActionType mCurrentAction;

    public GameObject() {
        super();

        mPosition = new Vector2();
        mVelocity = new Vector2();
        mTargetVelocity = new Vector2();
        mAcceleration = new Vector2();
        mImpulse = new Vector2();

        mFacingDirection = new Vector2(1, 0);

        reset();
    }

    @Override
    public void reset() {
        removeAll();
        commitUpdates();

        mPosition.setZero();
        mVelocity.setZero();
        mTargetVelocity.setZero();
        mAcceleration.setZero();
        mImpulse.setZero();
        mFacingDirection.set(1.0f, 1.0f);

        mCurrentAction = ActionType.INVALID;
        width = 0.0f;
        height = 0.0f;
    }

    public final Vector2 getPosition() {
        return mPosition;
    }

    public final void setPosition( final Vector2 position ) {
        mPosition.set(position);
    }

    public final float getCenteredPositionX() {
        return mPosition.x + (width / 2.0f);
    }

    public final float getCenteredPositionY() {
        return mPosition.y + (height / 2.0f);
    }

    public final Vector2 getVelocity() {
        return mVelocity;
    }

    public final void setVelocity( final Vector2 velocity ) {
        mVelocity.set(velocity);
    }

    public final Vector2 getTargetVelocity() {
        return mTargetVelocity;
    }

    public final void setTargetVelocity( final Vector2 targetVelocity ) {
        mTargetVelocity.set(targetVelocity);
    }

    public final Vector2 getAcceleration() {
        return mAcceleration;
    }

    public final void setAcceleration( final Vector2 acceleration ) {
        mAcceleration.set(acceleration);
    }

    public final Vector2 getImpulse() {
        return mImpulse;
    }

    public final void setImpulse( final Vector2 impulse ) {
        mImpulse.set(impulse);
    }

    public final ActionType getCurrentAction() {
        return mCurrentAction;
    }

    public final void setCurrentAction( final ActionType type ) {
        mCurrentAction = type;
    }

    public final Vector2 getFacingDirection() {
        return this.mFacingDirection;
    }

    public final void setFacingDirection( final Vector2 pFacingDirection ) {
        this.mFacingDirection = pFacingDirection;
    }
}
