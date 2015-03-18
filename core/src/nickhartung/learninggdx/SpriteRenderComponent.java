package nickhartung.learninggdx;

import com.badlogic.gdx.graphics.g2d.Sprite;

import nickhartung.libgdx.utilities.BaseObject;

/**
 * Created by Nick on 3/14/2015.
 */
public class SpriteRenderComponent extends GameComponent {

    private Sprite mSprite;

    public SpriteRenderComponent() {
        super();
        this.setPhase( ComponentPhases.DRAW.ordinal() );
    }

    @Override
    public void update( float timeDelta, BaseObject parent ) {
        if( mSprite != null ) {
            RenderSystem renderSystem = ObjectRegistry.sRenderSystem;
            if( renderSystem != null ) {
                renderSystem.scheduleForDraw( mSprite );
            }
        }
    }

    @Override
    public void reset() {
        mSprite = null;
    }

    public void setSprite( final Sprite pSprite ) {
        this.mSprite = pSprite;
    }

    public final Sprite getSprite() {
        return this.mSprite;
    }
}
