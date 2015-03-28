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

import nickhartung.libgdx.render.DrawableObject;
import nickhartung.libgdx.utilities.AllocationGuard;

/**
 * A single animation frame.  Frames contain a DrawableObject, and a hold time.
 */
public class AnimationFrame extends AllocationGuard {

    private DrawableObject mDrawable;
    private float mHoldTime;

    public AnimationFrame( final DrawableObject pDrawable, final float pAnimationHoldTime ) {
        super();
        this.mDrawable = pDrawable;
        this.mHoldTime = pAnimationHoldTime;
    }

    public float getHoldTime() {
        return this.mHoldTime;
    }

    public void setHoldTime( final float pHoldTime ) {
        this.mHoldTime = pHoldTime;
    }

    public DrawableObject getDrawable() {
        return this.mDrawable;
    }

    public void setDrawable( final DrawableObject pDrawable ) {
        this.mDrawable = pDrawable;
    }
}

