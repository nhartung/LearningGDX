package nickhartung.learninggdx;

import nickhartung.libgdx.utilities.BaseObject;
import nickhartung.libgdx.utilities.SpriteDrawable;

/**
 * Created by Nick on 3/14/2015.
 */
public class SpriteRenderComponent extends RenderComponent {

    private SpriteDrawable mSprite;

    public SpriteRenderComponent() {
        super();
        this.mSprite = new SpriteDrawable();
        this.setPhase( ComponentPhases.DRAW.ordinal() );
    }

    @Override
    public void update( float timeDelta, BaseObject parent ) {
        if( mSprite != null ) {
            RenderSystem renderSystem = ObjectRegistry.sRenderSystem;
            if( renderSystem != null ) {
                final GameObject gameObject = (GameObject)parent;
                renderSystem.scheduleForDraw( mSprite, gameObject.getPosition(), this.mPriority );
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        mSprite = null;
    }

    public void setSpriteDrawable( final SpriteDrawable pSprite ) {
        this.mSprite = pSprite;
    }
}
