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
package nickhartung.learninggdx.physics.standalone;

import nickhartung.learninggdx.GameComponent;
import nickhartung.learninggdx.GameObject;
import nickhartung.libgdx.utilities.BaseObject;
import nickhartung.libgdx.utilities.Interpolator;

/**
 * A game component that implements velocity-based movement.
 */
public class MovementComponent extends GameComponent {
    // If multiple game components were ever running in different threads, this would need
    // to be non-static.
    private static Interpolator sInterpolator = new Interpolator();

    public MovementComponent() {
        super();
        setPhase( ComponentPhases.MOVEMENT.ordinal() );
    }

    @Override
    public void reset() {

    }

    @Override
    public void update( final float timeDelta, final BaseObject parent ) {
        GameObject object = (GameObject)parent;

        sInterpolator.set( object.getVelocity().x, object.getTargetVelocity().x,
                           object.getAcceleration().x );
        float offsetX = sInterpolator.interpolate( timeDelta );
        float newX = object.getPosition().x + offsetX;
        float newVelocityX = sInterpolator.getCurrent();

        sInterpolator.set( object.getVelocity().y, object.getTargetVelocity().y,
                           object.getAcceleration().y );
        float offsetY = sInterpolator.interpolate( timeDelta );
        float newY = object.getPosition().y + offsetY;
        float newVelocityY = sInterpolator.getCurrent();

        object.getPosition().set( newX, newY );

        object.getVelocity().set( newVelocityX, newVelocityY );
    }

}
