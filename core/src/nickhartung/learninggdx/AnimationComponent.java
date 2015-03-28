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
import nickhartung.libgdx.utilities.PhasedObjectManager;

/**
 * Provides an interface for controlling a sprite with animations.  Manages a list of animations
 * and provides a drawable surface with the correct animation frame to a render component each
 * frame.  Also manages horizontal and vertical flipping.
 */
public class AnimationComponent extends GameComponent {

    private PhasedObjectManager mAnimations;
    private float mAnimationTime;
    private int mCurrentAnimationIndex;
    private RenderComponent mRenderComponent;
    private Animation mCurrentAnimation;
    private boolean mAnimationsDirty;

    public AnimationComponent() {
        super();
        this.mAnimations = new PhasedObjectManager();

        reset();
        setPhase( ComponentPhases.PRE_DRAW.ordinal() );
    }

    @Override
    public void reset() {
        this.mCurrentAnimationIndex = -1;
        this.mAnimations.removeAll();
        this.mAnimations.commitUpdates();
        this.mAnimationTime = 0.0f;
        this.mRenderComponent = null;
        this.mCurrentAnimation = null;
        this.mAnimationsDirty = false;
    }

    @Override
    public void update( final float pTimeDelta, final BaseObject pParent ) {
        this.mAnimationTime += pTimeDelta;
        final PhasedObjectManager animations = this.mAnimations;
        final int currentAnimIndex = this.mCurrentAnimationIndex;

        if( this.mAnimationsDirty ) {
            animations.commitUpdates();
            this.mAnimationsDirty = false;
        }
        boolean validFrameAvailable = false;
        if( animations.getCount() > 0 && currentAnimIndex != -1 ) {
            Animation currentAnimation = this.mCurrentAnimation;

            if( currentAnimation == null && currentAnimIndex != -1 ) {
                currentAnimation = findAnimation( currentAnimIndex );
                if( currentAnimation == null ) {
                    // We were asked to play an animation that doesn't exist.  Revert to our
                    // default animation.
                    // TODO: throw an assert here?
                    this.mCurrentAnimation = (Animation)animations.get( 0 );
                    currentAnimation = this.mCurrentAnimation;
                } else {
                    this.mCurrentAnimation = currentAnimation;
                }
            }

            GameObject parentObject = (GameObject)pParent;
            final AnimationFrame currentFrame = currentAnimation.getFrame( this.mAnimationTime );
            if( currentFrame != null ) {
                validFrameAvailable = true;
                final RenderComponent render = this.mRenderComponent;
                if( render != null ) {
                    render.setDrawable( currentFrame.getDrawable() );
                }
            }
        }
        if( !validFrameAvailable ) {
            // No current frame = draw nothing!
            if( this.mRenderComponent != null ) {
                this.mRenderComponent.setDrawable( null );
            }
        }
    }

    public final void playAnimation( final int pIndex ) {
        if( this.mCurrentAnimationIndex != pIndex ) {
            this.mAnimationTime = 0;
            this.mCurrentAnimationIndex = pIndex;
            this.mCurrentAnimation = null;
        }
    }

    public final Animation findAnimation( final int pIndex ) {
        return (Animation)this.mAnimations.find( pIndex );
    }

    public final void addAnimation( final Animation pAnimation ) {
        this.mAnimations.add( pAnimation );
        this.mAnimationsDirty = true;
    }

    public final boolean animationFinished() {
        boolean result = false;
        if( this.mCurrentAnimation != null
                && !this.mCurrentAnimation.getLoop()
                && this.mAnimationTime > this.mCurrentAnimation.getLength() ) {
            result = true;
        }
        return result;
    }

    public final void setRenderComponent( final RenderComponent pComponent ) {
        this.mRenderComponent = pComponent;
    }

    public final float getCurrentAnimationTime() {
        return this.mAnimationTime;
    }

    public final void setCurrentAnimationTime( final float pTime ) {
        this.mAnimationTime = pTime;
    }

    public final int getCurrentAnimation() {
        return this.mCurrentAnimationIndex;
    }

    public final int getAnimationCount() {
        return this.mAnimations.getConcreteCount();
    }
}