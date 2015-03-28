package nickhartung.learninggdx;

import com.badlogic.gdx.math.Vector2;

import box2dLight.Light;
import nickhartung.libgdx.utilities.BaseObject;

/**
 * Created by Nick on 3/24/2015.
 */
public class LightComponent extends GameComponent {

    private Light mLight;
    private Vector2 mOffset;

    public LightComponent() {
        this.mOffset = new Vector2();
    }

    @Override
    public void update( final float timeDelta, final BaseObject parent ) {
        assert ( parent != null );
        final GameObject object = (GameObject)parent;
        final GameObject.ActionType action = object.getCurrentAction();
        if( action != GameObject.ActionType.INVALID ) {
            final Vector2 position = object.getPosition();
            this.mLight.setPosition( position.x + this.mOffset.x, position.y + this.mOffset.y );
            this.mLight.setActive( true );
        } else {
            this.mLight.setActive( false );
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.mOffset.setZero();
        this.mLight.remove();
        this.mLight = null;
    }

    public void setLight( final Light pLight ) {
        this.mLight = pLight;
    }

    public void setOffset( final float pX, final float pY ) {
        this.mOffset.set( pX, pY );
    }
}
