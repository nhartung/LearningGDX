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

import nickhartung.libgdx.utilities.BaseObject;

public class TestAnimationComponent extends GameComponent {

    public enum Animations {
        IDLE,
        MOVE,
    }

    private enum AnimationState {
        IDLING,
        MOVING,
    }

    private AnimationComponent mAnimation;
    private AnimationState mState;

    public TestAnimationComponent() {
        super();
        setPhase( ComponentPhases.ANIMATION.ordinal() );
        reset();
    }

    @Override
    public void reset() {
        mState = AnimationState.IDLING;
        mAnimation = null;
    }

    @Override
    public void update( final float pTimeDelta, final BaseObject pParent ) {
        if( mAnimation != null ) {
            GameObject parentObject = (GameObject)pParent;
            final float velocityX = parentObject.getVelocity().x;

            final GameObject.ActionType currentAction = parentObject.getCurrentAction();

            switch( mState ) {
                case IDLING:
                    mAnimation.playAnimation( Animations.IDLE.ordinal() );
                    if( Math.abs( velocityX ) > 0.0f ) {
                        mState = AnimationState.MOVING;
                    }
                    break;

                case MOVING:
                    mAnimation.playAnimation( Animations.MOVE.ordinal() );
                    final float targetVelocityX = parentObject.getTargetVelocity().x;
                    if( Math.abs( velocityX ) == 0.0f ) {
                        mState = AnimationState.IDLING;
                    }
                    break;
            }
        }
    }

    public void setAnimation( final AnimationComponent pAnimation ) {
        mAnimation = pAnimation;
    }
}
